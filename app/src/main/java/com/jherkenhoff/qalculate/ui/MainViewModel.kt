package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants.TAG_TYPE_HTML
import com.jherkenhoff.libqalculate.libqalculateConstants.TAG_TYPE_TERMINAL
import com.jherkenhoff.qalculate.data.CalculationHistoryRepository
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import com.jherkenhoff.qalculate.domain.CalculateUseCase
import com.jherkenhoff.qalculate.domain.ParseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val calculator: Calculator,
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val calculationHistoryRepository: CalculationHistoryRepository
) : ViewModel() {

    val calculationHistory = calculationHistoryRepository
        .observeCalculationHistory()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val parsedString : MutableState<String> = mutableStateOf("")
    val resultString : MutableState<String> = mutableStateOf("")

    val inputTextFieldValue : MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(""))

    fun submitCalculation() {
        calculationHistoryRepository.appendCalculation(
            CalculationHistoryItem(
                LocalDateTime.now(),
                inputTextFieldValue.value.text,
                parsedString.value,
                resultString.value
            )
        )
    }

    fun updateInput(input: TextFieldValue) {
        inputTextFieldValue.value = input
        recalculate()
    }

    fun onQuickKeyPressed(quickKeyText: String) {
        val maxChars = inputTextFieldValue.value.text.length
        val textBeforeSelection = inputTextFieldValue.value.getTextBeforeSelection(maxChars)
        val textAfterSelection = inputTextFieldValue.value.getTextAfterSelection(maxChars)
        val newText = "$textBeforeSelection$quickKeyText$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length + quickKeyText.length


        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }

    private fun recalculate() {

        val parsedMathStructure = parseUseCase(inputTextFieldValue.value.text)

        val calculatedMathStructure = calculateUseCase(inputTextFieldValue.value.text)

        val parsedPrintOptions = PrintOptions()
        parsedPrintOptions.place_units_separately = false
        parsedPrintOptions.preserve_format = true
        parsedPrintOptions.use_unicode_signs = true
        parsedString.value = calculator.print(parsedMathStructure, 2000, parsedPrintOptions, true, 1, TAG_TYPE_HTML)

        val resultPo = PrintOptions()
        resultPo.interval_display = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
        resultPo.use_unicode_signs = true
        resultString.value = calculator.print(calculatedMathStructure, 2000, resultPo, true, 1, TAG_TYPE_HTML)
    }
}

private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start,
        this.selection.end,
        newString
    )
    val newSelection = TextRange(
        start = this.selection.end+newText.length,
        end = this.selection.end+newText.length
    )

    return this.copy(text = newText, selection = newSelection)
}