package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val calculator: Calculator,
    private val eo: EvaluationOptions
) : ViewModel() {

    val inputString : MutableState<String> = mutableStateOf("")
    val parsedString : MutableState<String> = mutableStateOf("")
    val resultString : MutableState<String> = mutableStateOf("")

    fun setInput(input: String) {
        inputString.value = input
        // TODO: If auto-calculation is switched on
        doCalculation()
    }
    private fun doCalculation() {

        val parse_options = ParseOptions()
        val ms = calculator.parse(inputString.value, parse_options)

        val parsed_po = PrintOptions()
        parsed_po.place_units_separately = false
        parsed_po.preserve_format = true
        parsedString.value = calculator.print(ms, 2000, parsed_po)

        calculator.calculate(ms, 2000, eo)

        val result_po = PrintOptions()
        result_po.interval_display = IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
        resultString.value = calculator.print(ms, 2000, result_po)
    }
}