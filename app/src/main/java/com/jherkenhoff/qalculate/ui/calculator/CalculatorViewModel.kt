package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.qalculate.data.AutocompleteRepository
import com.jherkenhoff.qalculate.data.CalculatorRepository
import com.jherkenhoff.qalculate.data.UserPreferencesRepository
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.data.repository.CalculationHistoryStore
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.domain.AutocompleteUseCase
import com.jherkenhoff.qalculate.domain.CalculateUseCase
import com.jherkenhoff.qalculate.domain.ParseUseCase
import com.jherkenhoff.qalculate.model.Action
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.UndoManager
import com.jherkenhoff.qalculate.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class InternalTextFieldValue(
    val textFieldValue: TextFieldValue,
    val doAutocomplete: Boolean
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val autocompleteRepository: AutocompleteRepository,
    private val calculationHistoryStore: CalculationHistoryStore,
    private val calculatorRepository: CalculatorRepository
) : ViewModel() {
    private val _internalInputTextFieldValue = MutableStateFlow(InternalTextFieldValue(TextFieldValue(), false))

    val inputTextFieldValue = _internalInputTextFieldValue.map { it.textFieldValue }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = TextFieldValue()
    )

    val parsedString = combine(inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        return@combine parseUseCase(inputTextFieldValue.text, userPreferences)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val resultString = combine(inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        return@combine calculateUseCase(inputTextFieldValue.text, userPreferences)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val autocompleteResult = combine(autocompleteRepository.trie , _internalInputTextFieldValue, ) { autocompleteTrie, internalInputTextFieldValue ->
        if (internalInputTextFieldValue.doAutocomplete)
            autocompleteUseCase(autocompleteTrie, internalInputTextFieldValue.textFieldValue)
        else
            AutocompleteResult()
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

    fun clearCalculationHistory() {
        viewModelScope.launch {
            calculationHistoryStore.deleteAll()
        }
    }

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

        viewModelScope.launch {
            val unformattedResultString = calculateUseCase(inputTextFieldValue.value.text, userPreferences.value, format = false)
            Log.d("moin", unformattedResultString)
            calculatorRepository.setAnsExpression(unformattedResultString)
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
        val undoState = undoManager.undo(inputTextFieldValue.value)
        if (undoState !== null)
            _internalInputTextFieldValue.update { InternalTextFieldValue(undoState, false) }
    }

    fun redo() {
        val redoState = undoManager.redo(inputTextFieldValue.value)
        if (redoState !== null)
            _internalInputTextFieldValue.update { InternalTextFieldValue(redoState, false) }
    }

    fun moveCursor(chars: Int) {
        val newCursorPosition = (inputTextFieldValue.value.selection.end + chars).coerceIn(0, inputTextFieldValue.value.text.length)
        updateInput(inputTextFieldValue.value.copy(selection = TextRange(newCursorPosition)))
    }

    fun updateInput(input: TextFieldValue, doAutocomplete: Boolean = false) {
        val textChanged = input.text != inputTextFieldValue.value.text
        if (textChanged)
            undoManager.snapshot(inputTextFieldValue.value)


        _internalInputTextFieldValue.update { InternalTextFieldValue(input, doAutocomplete && textChanged) }
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

    fun replaceRange(range: TextRange, preCursorText: String, postCursorText: String = "") {
        val newText = inputTextFieldValue.value.text.replaceRange(range.start, range.end, "$preCursorText$postCursorText")
        val newCursorPosition = range.start + preCursorText.length

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
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
        replaceRange(
            autocompleteResult.value.contextRange,
            autocompleteItem.typeBeforeCursor,
            autocompleteItem.typeAfterCursor
        )

    }
}