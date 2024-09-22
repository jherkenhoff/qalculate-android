package com.jherkenhoff.qalculate.ui

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
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import com.jherkenhoff.qalculate.domain.AutocompleteItem
import com.jherkenhoff.qalculate.domain.CalculateUseCase
import com.jherkenhoff.qalculate.domain.ParseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
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

    var parsedString by mutableStateOf("0")
        private set

    var resultString by mutableStateOf("0")
        private set

    var autocompleteText by mutableStateOf("")
        private set

    var inputTextFieldValue by mutableStateOf(TextFieldValue(""))
        private set

    var autocompleteList by mutableStateOf<List<AutocompleteItem>>(emptyList())
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

    fun updateInput(input: TextFieldValue) {
        inputTextFieldValue = input
        handleAutocomplete()
        recalculate()
    }

    private fun handleAutocomplete() {

        if (inputTextFieldValue.selection.length > 0) {
            autocompleteList = listOf()
            autocompleteText = ""
            return
        }

        var currentString = inputTextFieldValue.getTextBeforeSelection(inputTextFieldValue.text.length).toString()

        viewModelScope.launch {
            withContext(Dispatchers.Default) {

                val pattern = Regex("([a-zA-Z_]+$)")
                val match = pattern.find(currentString)

                if (match == null) {
                    autocompleteList = listOf()
                    autocompleteText = ""
                    return@withContext
                }

                currentString = match.value
                autocompleteText = currentString

                val unitList = calculator.units.filter {
                    it.title().lowercase().startsWith(currentString.lowercase())
                            || it.name().lowercase().startsWith(currentString.lowercase())
                }.map {
                    AutocompleteItem(it.title(), it.name(), it.abbreviation())
                }

                autocompleteList = unitList

                return@withContext
            }
        }
    }

    fun acceptAutocomplete(autocompleteString: String) {

        var textBefore = inputTextFieldValue.getTextBeforeSelection(inputTextFieldValue.text.length).toString()
        var textAfter = inputTextFieldValue.getTextAfterSelection(inputTextFieldValue.text.length).toString()


        val pattern = Regex("([a-zA-Z_]+$)")

        val match = pattern.split(textBefore).first()

        val newCursorPosition = match.length + autocompleteString.length
        val newText = "$match$autocompleteString$textAfter"

        updateInput(TextFieldValue(
            text = newText,
            selection = TextRange(newCursorPosition)
        ))
    }

    fun insertText(quickKeyText: String) {
        val maxChars = inputTextFieldValue.text.length
        val textBeforeSelection = inputTextFieldValue.getTextBeforeSelection(maxChars)
        val textAfterSelection = inputTextFieldValue.getTextAfterSelection(maxChars)
        val newText = "$textBeforeSelection$quickKeyText$textAfterSelection"
        val newCursorPosition = textBeforeSelection.length + quickKeyText.length


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

            val parsedMathStructure = parseUseCase(inputTextFieldValue.text)

            val calculatedMathStructure = calculateUseCase(inputTextFieldValue.text)

            val parsedPrintOptions = PrintOptions()
            parsedPrintOptions.place_units_separately = false
            parsedPrintOptions.preserve_format = true
            parsedPrintOptions.use_unicode_signs = 1
            parsedString = calculator.print(parsedMathStructure, 2000, parsedPrintOptions, true, 1, TAG_TYPE_HTML)

            val resultPo = PrintOptions()
            resultPo.interval_display = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
            resultPo.use_unicode_signs = 1
            resultString = calculator.print(calculatedMathStructure, 2000, resultPo, true, 1, TAG_TYPE_HTML)
        }
    }
}