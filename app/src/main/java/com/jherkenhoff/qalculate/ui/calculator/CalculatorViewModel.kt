package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.qalculate.data.UserPreferencesRepository
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.data.repository.CalculationHistoryStore
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.domain.AutocompleteUseCase
import com.jherkenhoff.qalculate.domain.CalculateUseCase
import com.jherkenhoff.qalculate.domain.ParseUseCase
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.Action
import com.jherkenhoff.qalculate.model.UndoManager
import com.jherkenhoff.qalculate.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val calculationHistoryStore: CalculationHistoryStore
) : ViewModel() {
    private val _inputTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val inputTextFieldValue = _inputTextFieldValue.asStateFlow()

    val parsedString = combine(_inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        return@combine parseUseCase(inputTextFieldValue.text, userPreferences)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val resultString = combine(_inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        return@combine calculateUseCase(inputTextFieldValue.text, userPreferences)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val autocompleteResult = combine(_inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        autocompleteUseCase(inputTextFieldValue, userPreferences)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = AutocompleteResult()
    )

    val userPreferences = userPreferencesRepository.userPreferencesFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserPreferences()
    )

    val calculationHistory = calculationHistoryStore.allItems().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val undoManager = UndoManager<TextFieldValue>()
    val undoState = undoManager.state

    private val _autocompleteDismissed = MutableStateFlow(false)
    val autocompleteDismissed = _autocompleteDismissed.asStateFlow()

    fun updateUserPreferences(userPreferences: UserPreferences) {
        viewModelScope.launch {
            userPreferencesRepository.updateUserPreferences(userPreferences)
        }
    }

    fun submitCalculation() {
        viewModelScope.launch {
            calculationHistoryStore.addItem(
                CalculationHistoryItemData(
                    input = inputTextFieldValue.value.text,
                    parsed = parsedString.value,
                    result = resultString.value,
                    created = LocalDateTime.now()
                )
            )
        }
        updateInput(
            inputTextFieldValue.value.copy(selection = TextRange(0, inputTextFieldValue.value.text.length))
        )
        undoManager.clear()
    }

    fun deleteCalculation(item: CalculationHistoryItemData) {
        viewModelScope.launch {
            calculationHistoryStore.deleteItem(item)
        }
    }

    fun handleKeyAction(action: Action) {
        when (action) {
            is Action.InsertText -> insertText(action)
            is Action.Backspace -> removeChars(action.nChars)
            is Action.Return -> submitCalculation()
            is Action.ClearAll -> clearInput()
            is Action.MoveCursor -> moveCursor(action.chars)
            is Action.Undo -> undo()
            is Action.Redo -> redo()
            is Action.StoreAsVariable -> null
        }
    }

    fun undo() {
        val undoState = undoManager.undo(_inputTextFieldValue.value)
        if (undoState !== null)
            _inputTextFieldValue.update { undoState }
    }

    fun redo() {
        val redoState = undoManager.redo(_inputTextFieldValue.value)
        if (redoState !== null)
            _inputTextFieldValue.update { redoState }
    }

    fun moveCursor(chars: Int) {
        val newCursorPosition = (inputTextFieldValue.value.selection.end + chars).coerceIn(0, inputTextFieldValue.value.text.length)
        updateInput(inputTextFieldValue.value.copy(selection = TextRange(newCursorPosition)))
    }

    fun updateInput(input: TextFieldValue) {
        undoManager.snapshot(_inputTextFieldValue.value)
        _inputTextFieldValue.update { input }
    }

    fun clearInput() {
        updateInput(TextFieldValue(""))
    }

    fun insertText(action: Action.InsertText) {
        val maxChars = inputTextFieldValue.value.text.length
        val textBeforeSelection = inputTextFieldValue.value.getTextBeforeSelection(maxChars)
        val selectedText = inputTextFieldValue.value.getSelectedText()
        val textAfterSelection = inputTextFieldValue.value.getTextAfterSelection(maxChars)

        with(action) {
            if (selectedText.isNotEmpty()) {
                    val newText = when (selectionPolicy) {
                        Action.InsertText.SelectionPolicy.REPLACE -> "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection"
                        Action.InsertText.SelectionPolicy.SURROUND -> "$textBeforeSelection$preCursorText$selectedText$postCursorText$textAfterSelection"
                        Action.InsertText.SelectionPolicy.PARENTHESES -> "$textBeforeSelection($selectedText)$preCursorText$postCursorText$textAfterSelection"
                    }

                    val newSelection = when (selectionPolicy) {
                        Action.InsertText.SelectionPolicy.REPLACE -> TextRange(textBeforeSelection.length + preCursorText.length)
                        Action.InsertText.SelectionPolicy.SURROUND -> TextRange(textBeforeSelection.length, newText.length - textAfterSelection.length)
                        Action.InsertText.SelectionPolicy.PARENTHESES -> TextRange(newText.length - postCursorText.length - textAfterSelection.length)
                    }

                    updateInput(TextFieldValue(
                        text = newText,
                        selection = newSelection
                    ))
            } else {
                updateInput(TextFieldValue(
                    text = "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection",
                    selection = TextRange(textBeforeSelection.length + preCursorText.length)
                ))
            }
        }
    }

    fun insertText(preCursorText: String, postCursorText: String = "") {
        val maxChars = inputTextFieldValue.value.text.length
        val textBeforeSelection = inputTextFieldValue.value.getTextBeforeSelection(maxChars)
        val textAfterSelection = inputTextFieldValue.value.getTextAfterSelection(maxChars)
        val newText = "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length + preCursorText.length

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }

    fun removeChars(nChars: Int) {
        val maxChars = inputTextFieldValue.value.text.length
        val textBeforeSelection = inputTextFieldValue.value.getTextBeforeSelection(maxChars).dropLast(nChars)
        val textAfterSelection = inputTextFieldValue.value.getTextAfterSelection(maxChars)
        val newText = "$textBeforeSelection$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }

    fun acceptAutocomplete(autocompleteItem: AutocompleteItem) {
        insertText(autocompleteItem.typeBeforeCursor, autocompleteItem.typeAfterCursor)
    }
}