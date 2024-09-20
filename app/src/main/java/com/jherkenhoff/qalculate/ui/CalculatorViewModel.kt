package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import com.jherkenhoff.qalculate.domain.AutocompleteItem
import com.jherkenhoff.qalculate.domain.AutocompleteUseCase
import com.jherkenhoff.qalculate.domain.CalculateUseCase
import com.jherkenhoff.qalculate.domain.ParseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val calculator: Calculator,
    private val parseUseCase: ParseUseCase,
    private val calculateUseCase: CalculateUseCase,
    private val autocompleteUseCase: AutocompleteUseCase,
    private val calculationHistoryRepository: CalculationHistoryRepository
) : ViewModel() {

    val calculationHistory = calculationHistoryRepository
        .observeCalculationHistory()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val parsedString : MutableState<String> = mutableStateOf("0")
    val resultString : MutableState<String> = mutableStateOf("0")

    val inputTextFieldValue : MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(""))

    private val _autocompleteList = mutableStateOf<List<AutocompleteItem>>(emptyList())
    val autocompleteList = _autocompleteList

    fun submitCalculation() {
        calculationHistoryRepository.appendCalculation(
            CalculationHistoryItem(
                LocalDateTime.now(),
                inputTextFieldValue.value.text,
                parsedString.value,
                resultString.value
            )
        )

        updateInput(TextFieldValue(""))
    }

    fun updateInput(input: TextFieldValue) {
        inputTextFieldValue.value = input
        updateAutocompleteList()
        recalculate()
    }

    private fun updateAutocompleteList() {

        // https://gist.github.com/Terenfear/a84863be501d3399889455f391eeefe5
        fun <T> throttleLatest(
            intervalMs: Long = 300L,
            coroutineScope: CoroutineScope,
            destinationFunction: (T) -> Unit
        ): (T) -> Unit {
            var throttleJob: Job? = null
            var latestParam: T
            return { param: T ->
                latestParam = param
                if (throttleJob?.isCompleted != false) {
                    throttleJob = coroutineScope.launch {
                        delay(intervalMs)
                        destinationFunction(latestParam)
                    }
                }
            }
        }

        //val throttledUpdate = throttleLatest<TextFieldValue>(200L, viewModelScope) { _autocompleteList.value  = autocompleteUseCase(it) }

        //throttledUpdate(inputTextFieldValue.value)
        viewModelScope.launch {
            _autocompleteList.value = autocompleteUseCase(inputTextFieldValue.value)
        }
    }

    fun acceptAutocomplete(autocompleteString: String) {

        var textBefore = inputTextFieldValue.value.getTextBeforeSelection(inputTextFieldValue.value.text.length).toString()
        var textAfter = inputTextFieldValue.value.getTextAfterSelection(inputTextFieldValue.value.text.length).toString()


        val pattern = Regex("([a-zA-Z_]+$)")

        val match = pattern.split(textBefore).first()

        val newCursorPosition = match.length + autocompleteString.length
        val newText = "$match$autocompleteString$textAfter"

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
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

    fun onDelKeyPressed() {
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

    fun onACKeyPressed() {
        updateInput(TextFieldValue(
            text = "",
            selection = TextRange(0)
        ))
    }

    private fun recalculate() {

        viewModelScope.launch {

            val parsedMathStructure = parseUseCase(inputTextFieldValue.value.text)

            val calculatedMathStructure = calculateUseCase(inputTextFieldValue.value.text)

            val parsedPrintOptions = PrintOptions()
            parsedPrintOptions.place_units_separately = false
            parsedPrintOptions.preserve_format = true
            parsedPrintOptions.use_unicode_signs = 1
            parsedString.value = calculator.print(parsedMathStructure, 2000, parsedPrintOptions, true, 1, TAG_TYPE_HTML)

            val resultPo = PrintOptions()
            resultPo.interval_display = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
            resultPo.use_unicode_signs = 1
            resultString.value = calculator.print(calculatedMathStructure, 2000, resultPo, true, 1, TAG_TYPE_HTML)
        }
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