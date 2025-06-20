package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.qalculate.data.AutocompleteRepository
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
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val calculator: Calculator,
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val printUseCase: PrintUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val calculationsRepository: CalculationsRepository,
    private val autocompleteRepository: AutocompleteRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    private val _focusedCalculationUuid = MutableStateFlow<UUID?>(null)
    val focusedCalculationUuid = _focusedCalculationUuid.asStateFlow()

    private val _inputTextFieldValue = MutableStateFlow(TextFieldValue(""))
    val inputTextFieldValue = _inputTextFieldValue.asStateFlow()

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

    fun submitCalculation(uuid: UUID) {
        viewModelScope.launch {
            val newUuid = calculationsRepository.appendCalculation(
                Calculation(
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    TextFieldValue(""),
                    "0",
                    "0"
                )
            )

            _focusedCalculationUuid.value = newUuid
        }
    }


    fun onCalculationFocusChange(uuid: UUID) {
        _focusedCalculationUuid.value = uuid
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

    fun updateCalculation(uuid: UUID, input: TextFieldValue) {
        viewModelScope.launch {
            val parsed = parseUseCase(input.text, userPreferences.value)
            val mathStructure = calculateUseCase(input.text, userPreferences.value)
            val result = printUseCase(mathStructure, userPreferences.value)

            calculationsRepository.updateCalculation(uuid, Calculation(
                LocalDateTime.now(),
                LocalDateTime.now(),
                input,
                parsed,
                result
            ))
        }
    }

    fun updateInput(input: TextFieldValue) = _inputTextFieldValue.update { input }
    fun clearInput() = _inputTextFieldValue.update { TextFieldValue("") }

    fun acceptAutocomplete(beforeCursorText: String, afterCursorText: String) {

        val textBefore = inputTextFieldValue.value.getTextBeforeSelection(inputTextFieldValue.value.text.length).toString()
        val textAfter = inputTextFieldValue.value.getTextAfterSelection(inputTextFieldValue.value.text.length).toString()


        val pattern = Regex("([a-zA-Z_]+$)")

        val match = pattern.find(textBefore)

        val textBeforeWithoutRelevant = pattern.split(textBefore).first()

        updateInput(TextFieldValue(
            text = textBeforeWithoutRelevant + beforeCursorText + afterCursorText + textAfter,
            selection = TextRange(textBeforeWithoutRelevant.length + beforeCursorText.length)
        ))
    }

    fun insertText(preCursorText: String, postCursorText: String = "") {

        focusedCalculationUuid.value?.let { uuid ->
            calculations.value[uuid]?.input?.let { inputTextFieldValue ->
                val maxChars = inputTextFieldValue.text.length
                val textBeforeSelection = inputTextFieldValue.getTextBeforeSelection(maxChars)
                val textAfterSelection = inputTextFieldValue.getTextAfterSelection(maxChars)
                val newText = "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection"
                val newCursorPosition = textBeforeSelection.length + preCursorText.length

                updateCalculation(uuid, TextFieldValue(
                    text = newText,
                    selection = TextRange(newCursorPosition)
                ))
            }


        }
    }

    fun removeLastChar() {
        val maxChars = inputTextFieldValue.value.text.length
        val textBeforeSelection = inputTextFieldValue.value.getTextBeforeSelection(maxChars)
        val textAfterSelection = inputTextFieldValue.value.getTextAfterSelection(maxChars)
        val selectedText = inputTextFieldValue.value.getSelectedText()

        var newText: String
        var newCursorPosition: Int

        if (selectedText.isEmpty()) {
            if (textBeforeSelection.isEmpty()) {
                return
            } else {
                newText = "${textBeforeSelection.dropLast(1)}$textAfterSelection"
                newCursorPosition = textBeforeSelection.length - 1
            }
        } else {
            newText = "$textBeforeSelection$textAfterSelection"
            newCursorPosition = textBeforeSelection.length
        }

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }
}