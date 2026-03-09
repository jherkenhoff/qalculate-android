package com.jherkenhoff.qalculate.domain

import androidx.compose.ui.text.input.TextFieldValue
import com.jherkenhoff.qalculate.model.CalculatorAction

class CalcActionEnableLogic(
    private val input: TextFieldValue
) {
    fun isEnabled(calculatorAction: CalculatorAction) : Boolean {
        return true
    }
}