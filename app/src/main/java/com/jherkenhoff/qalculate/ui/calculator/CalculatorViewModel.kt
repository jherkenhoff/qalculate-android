package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants.TAG_TYPE_HTML
import com.jherkenhoff.qalculate.data.CalculationHistoryRepository
import com.jherkenhoff.qalculate.data.ScreenSettingsRepository
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.domain.AutocompleteUseCase
import com.jherkenhoff.qalculate.domain.CalculateUseCase
import com.jherkenhoff.qalculate.domain.ParseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculator: Calculator,
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val calculationHistoryRepository: CalculationHistoryRepository,
    private val screenSettingsRepository: ScreenSettingsRepository
) : ViewModel() {

    val calculationHistory = calculationHistoryRepository
        .observeCalculationHistory()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val altKeyboardOpen = screenSettingsRepository.isAltKeyboardOpen

    var parsedString by mutableStateOf("")
        private set

    var resultString by mutableStateOf("0")
        private set

    var autocompleteResult by mutableStateOf(AutocompleteResult(success = false))
        private set

    var inputTextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    fun submitCalculation() {
        calculationHistoryRepository.appendCalculation(
            CalculationHistoryItem(
                LocalDateTime.now(),
                inputTextFieldValue.text,
                parsedString,
                resultString
            )
        )

        updateInput(TextFieldValue(""))
    }

    fun toggleAltKeyboard(newState: Boolean) {
        runBlocking {
            screenSettingsRepository.saveAltKeyboardOpen(newState)
        }
    }

    fun updateInput(input: TextFieldValue) {
        inputTextFieldValue = input
        handleAutocomplete()
        recalculate()
    }

    private fun handleAutocomplete() {
        viewModelScope.launch {
            autocompleteResult = autocompleteUseCase(inputTextFieldValue)
        }
    }

    fun acceptAutocomplete(beforeCursorText: String, afterCursorText: String) {

        // HACK: Add a space behind the accepted autocomplete text in order for the autocomplete list to dissapear
        var beforeCursorText2 = beforeCursorText
        if (afterCursorText.isEmpty()) {
            beforeCursorText2 += " "
        }

        updateInput(TextFieldValue(
            text = autocompleteResult.textBefore + beforeCursorText2 + afterCursorText + autocompleteResult.textAfter,
            selection = TextRange(autocompleteResult.textBefore.length + beforeCursorText2.length)
        ))
    }

    fun insertText(preCursorText: String, postCursorText: String = "") {
        val maxChars = inputTextFieldValue.text.length
        val textBeforeSelection = inputTextFieldValue.getTextBeforeSelection(maxChars)
        val textAfterSelection = inputTextFieldValue.getTextAfterSelection(maxChars)
        val newText = "$textBeforeSelection$preCursorText$postCursorText$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length + preCursorText.length

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }

    fun removeLastChar() {
        val maxChars = inputTextFieldValue.text.length
        val textBeforeSelection = inputTextFieldValue.getTextBeforeSelection(maxChars)
        val textAfterSelection = inputTextFieldValue.getTextAfterSelection(maxChars)
        val selectedText = inputTextFieldValue.getSelectedText()

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

    fun clearAll() {
        updateInput(TextFieldValue(
            text = "",
            selection = TextRange(0)
        ))
    }

    private fun recalculate() {

        viewModelScope.launch {

            parsedString = parseUseCase(inputTextFieldValue.text)

            val calculatedMathStructure = calculateUseCase(inputTextFieldValue.text)

            val resultPo = PrintOptions()
            resultPo.interval_display = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
            resultPo.use_unicode_signs = 1
            resultString = calculator.print(calculatedMathStructure, 2000, resultPo, true, 1, TAG_TYPE_HTML)
        }
    }
}