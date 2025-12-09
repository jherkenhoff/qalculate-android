package com.jherkenhoff.qalculate.model

enum class KeyRole {
    OPERATOR, NUMBER, SYSTEM
}

sealed class KeySpec {
    abstract val role: KeyRole

    data class DefaultKeySpec (
        val clickAction : Action,
        val longClickAction : Action? = null,
        override val role: KeyRole
    ) : KeySpec()
    
    data class SelectorKeySpec (
        val actions: List<Action>,
        val initialSelectedIndex: Int,
        override val role: KeyRole
    ) : KeySpec()

    data class CornerDragKeySpec (
        val centerAction: Action,
        val topLeftAction: Action? = null,
        val topRightAction: Action? = null,
        val bottomLeftAction: Action? = null,
        val bottomRightAction: Action? = null,
        override val role: KeyRole
    ) : KeySpec()
}