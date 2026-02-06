package com.jherkenhoff.qalculate.model

enum class KeyRole {
    OPERATOR, NUMBER, SYSTEM
}

sealed class CalcKey {
    abstract val role: KeyRole

    data class Default (
        val clickAction : CalcAction,
        val longClickAction : CalcAction? = null,
        override val role: KeyRole = KeyRole.NUMBER
    ) : CalcKey()
    
    data class Selector (
        val actions: List<CalcAction>,
        val initialSelectedIndex: Int,
        override val role: KeyRole
    ) : CalcKey()

    data class CornerDrag (
        val centerAction: CalcAction,
        val topLeftAction: CalcAction? = null,
        val topRightAction: CalcAction? = null,
        val bottomLeftAction: CalcAction? = null,
        val bottomRightAction: CalcAction? = null,
        override val role: KeyRole
    ) : CalcKey()
}