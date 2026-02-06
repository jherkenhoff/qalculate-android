package com.jherkenhoff.qalculate.domain

import androidx.compose.ui.text.input.TextFieldValue
import com.jherkenhoff.qalculate.model.CalcAction

class CalcActionEnableLogic(
    private val input: TextFieldValue
) {
    fun isEnabled(calcAction: CalcAction) : Boolean {
        return true
    }
}