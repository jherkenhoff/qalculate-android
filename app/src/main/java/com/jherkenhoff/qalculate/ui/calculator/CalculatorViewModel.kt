package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.qalculate.data.UserPreferencesRepository
import com.jherkenhoff.qalculate.data.calculations.CalculationsRepository
import com.jherkenhoff.qalculate.data.model.UserPreferences
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.domain.AutocompleteUseCase
import com.jherkenhoff.qalculate.domain.CalculateUseCase
import com.jherkenhoff.qalculate.domain.ParseUseCase
import com.jherkenhoff.qalculate.domain.PrintUseCase
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.Calculation
import com.jherkenhoff.qalculate.model.KeyAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject


data class CalculatorUiState (
    val autocompleteList: List<AutocompleteItem> = emptyList(),
)
@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val printUseCase: PrintUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val calculationsRepository: CalculationsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val _inputTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val inputTextFieldValue = _inputTextFieldValue.asStateFlow()

    private var autocompleteJob: Job? = null
    private var calculateJob: Job? = null

    private var autocompleteDismissed = false

    val parsedString = combine(_inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        return@combine parseUseCase(inputTextFieldValue.text, userPreferences)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val resultString = combine(_inputTextFieldValue, userPreferencesRepository.userPreferencesFlow) { inputTextFieldValue, userPreferences ->
        val mathStructure = calculateUseCase(inputTextFieldValue.text, userPreferences)
        return@combine printUseCase(mathStructure, userPreferences)
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
        initialValue = AutocompleteResult("", emptyList())
    )

    val calculations = calculationsRepository
        .observeCalculations()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap()
        )

    val userPreferences = userPreferencesRepository.userPreferencesFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UserPreferences.getDefaultInstance()
    )

    val altKeyboardOpen = userPreferencesRepository.userPreferencesFlow.map { it.altKeyboardOpen }


    fun submitCalculation() {
        viewModelScope.launch {
            calculationsRepository.appendCalculation(
                Calculation(
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    inputTextFieldValue.value,
                    parsedString.value,
                    resultString.value
                )
            )
        }
    }

    fun toggleAltKeyboard(newState: Boolean) {
        runBlocking {
            userPreferencesRepository.setIsAltKeyboardOpen(newState)
        }
    }

    fun deleteCalculation(uuid: UUID) {
        viewModelScope.launch {
            calculationsRepository.deleteCalculation(uuid)
        }
    }

    fun handleKeyAction(keyAction: KeyAction) {
        when (keyAction) {
            is KeyAction.InsertText -> insertText(keyAction.preCursorText, keyAction.postCursorText)
            is KeyAction.Backspace -> removeChars(keyAction.nChars)
            is KeyAction.Return -> submitCalculation()
            is KeyAction.ClearAll -> clearInput()
            is KeyAction.MoveCursor -> moveCursor(keyAction.chars)
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