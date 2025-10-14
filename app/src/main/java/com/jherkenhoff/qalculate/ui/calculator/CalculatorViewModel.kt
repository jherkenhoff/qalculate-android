package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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
import com.jherkenhoff.qalculate.model.KeyAction
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
    }

    fun deleteCalculation(item: CalculationHistoryItemData) {
        viewModelScope.launch {
            calculationHistoryStore.deleteItem(item)
        }
    }

    fun handleKeyAction(keyAction: KeyAction) {
        when (keyAction) {
            is KeyAction.InsertText -> insertText(keyAction.preCursorText, keyAction.postCursorText)
            is KeyAction.Backspace -> removeChars(keyAction.nChars)
            is KeyAction.Return -> submitCalculation()
            is KeyAction.ClearAll -> clearInput()
            is KeyAction.MoveCursor -> moveCursor(keyAction.chars)
            is KeyAction.Undo -> null
            is KeyAction.Redo -> null
            is KeyAction.StoreAsVariable -> null
        }
    }

    fun moveCursor(chars: Int) {
        val newCursorPosition = inputTextFieldValue.value.selection.end + chars
        updateInput(inputTextFieldValue.value.copy(selection = TextRange(newCursorPosition)))
    }

    fun updateInput(input: TextFieldValue) = _inputTextFieldValue.update { input }
    fun clearInput() = _inputTextFieldValue.update { TextFieldValue("") }


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