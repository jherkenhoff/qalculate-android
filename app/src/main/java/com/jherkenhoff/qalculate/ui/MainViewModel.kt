package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
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
        val ms = calculator.parse(input, parseOptions)

        val parsedPrintOptions = PrintOptions()
        parsedPrintOptions.place_units_separately = false
        parsedPrintOptions.preserve_format = true
        parsedString.value = calculator.print(ms, 2000, parsedPrintOptions)

        val evaluationOptions = EvaluationOptions()
        calculator.calculate(ms, 2000, evaluationOptions)

        val resultPo = PrintOptions()
        resultPo.interval_display = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
        resultString.value = calculator.print(ms, 2000, resultPo)
    }
}