package com.jherkenhoff.qalculate.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.PrintOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val calculator: Calculator,
    private val po: PrintOptions,
    private val eo: EvaluationOptions
) : ViewModel() {

    val inputString : MutableState<String> = mutableStateOf("")
    val resultString : MutableState<String> = mutableStateOf("")

    fun setInput(input: String) {
        inputString.value = input
        // TODO: If auto-calculation is switched on
        doCalculation()
    }

    private fun doCalculation() {
        resultString.value = calculator.calculateAndPrint(inputString.value, 2000, eo, po)
    }
}