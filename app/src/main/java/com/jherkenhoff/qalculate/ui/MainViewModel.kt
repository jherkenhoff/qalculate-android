package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants.TAG_TYPE_HTML
import com.jherkenhoff.libqalculate.libqalculateConstants.TAG_TYPE_TERMINAL
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val calculator: Calculator
) : ViewModel() {
    val parsedString : MutableState<String> = mutableStateOf("")
    val resultString : MutableState<String> = mutableStateOf("")

    fun calculate(input: String) {

        val parseOptions = ParseOptions()
        parseOptions.preserve_format = true
        val ms = calculator.parse(input, parseOptions)

        val parsedPrintOptions = PrintOptions()
        parsedPrintOptions.place_units_separately = false
        parsedPrintOptions.preserve_format = true
        parsedPrintOptions.use_unicode_signs = true
        parsedString.value = calculator.print(ms, 2000, parsedPrintOptions, true, 1, TAG_TYPE_HTML)

        val evaluationOptions = EvaluationOptions()
        calculator.calculate(ms, 2000, evaluationOptions)

        val resultPo = PrintOptions()
        resultPo.interval_display = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
        resultPo.use_unicode_signs = true
        resultString.value = calculator.print(ms, 2000, resultPo, true, 1, TAG_TYPE_HTML)
    }
}