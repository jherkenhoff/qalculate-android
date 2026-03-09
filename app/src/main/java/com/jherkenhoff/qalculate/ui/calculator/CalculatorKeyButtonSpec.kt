package com.jherkenhoff.qalculate.ui.calculator

import com.jherkenhoff.qalculate.model.CalculatorAction

enum class KeyRole {
    OPERATOR, NUMBER, SYSTEM
}

sealed class CalculatorKeyButtonSpec {
    abstract val role: KeyRole

    data class Default (
        val clickAction : CalculatorAction,
        val longClickAction : CalculatorAction? = null,
        override val role: KeyRole = KeyRole.NUMBER
    ) : CalculatorKeyButtonSpec()

    data class Selector (
        val actions: List<CalculatorAction>,
        val initialSelectedIndex: Int,
        override val role: KeyRole
    ) : CalculatorKeyButtonSpec()

    data class CornerDrag (
        val centerAction: CalculatorAction,
        val topLeftAction: CalculatorAction? = null,
        val topRightAction: CalculatorAction? = null,
        val bottomLeftAction: CalculatorAction? = null,
        val bottomRightAction: CalculatorAction? = null,
        override val role: KeyRole
    ) : CalculatorKeyButtonSpec()
}