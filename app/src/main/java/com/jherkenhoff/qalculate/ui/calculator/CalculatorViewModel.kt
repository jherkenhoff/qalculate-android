package com.jherkenhoff.qalculate.ui.calculator

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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val printUseCase: PrintUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val calculationsRepository: CalculationsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _focusedCalculationUuid = MutableStateFlow<UUID?>(null)
    val focusedCalculationUuid = _focusedCalculationUuid.asStateFlow()

    private val _autocompleteResult = MutableStateFlow<AutocompleteResult?>(null)
    val autocompleteResult = _autocompleteResult.asStateFlow()

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

    fun onCalculationFocusChange(uuid: UUID?) {
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
            _autocompleteResult.value = autocompleteUseCase(input, userPreferences.value)

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

    fun acceptAutocomplete(autocompleteItem: AutocompleteItem) {
        insertText(autocompleteItem.typeBeforeCursor, autocompleteItem.typeAfterCursor)
    }
}