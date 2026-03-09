package com.jherkenhoff.qalculate.ui.calculator

import com.jherkenhoff.qalculate.model.CalculatorAction

/**
 * Model of a calculator action, including a label de
 */
data class CalculatorKeyButtonActionSpec(
    val action: CalculatorAction,
    val label: CalculatorKeyButtonActionLabel,
    val enabled: Boolean
)