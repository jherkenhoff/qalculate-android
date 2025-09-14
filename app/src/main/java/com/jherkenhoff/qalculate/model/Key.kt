package com.jherkenhoff.qalculate.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import kotlin.Int.Companion.MAX_VALUE
import kotlin.Int.Companion.MIN_VALUE

enum class KeyRole {
    NUMBER, OPERATOR, SYSTEM
}


sealed class Key {
    abstract val role: KeyRole
    abstract val width: Int

    data class DefaultKey (
        val clickAction : KeyAction,
        val longClickAction : KeyAction? = null,
        override val role: KeyRole,
        override val width: Int = 1,
    ) : Key()
    
    data class SelectorKey (
        val actions: Array<KeyAction>,
        val initialSelectedIndex: Int,
        override val role: KeyRole,
        override val width: Int = 1,
    ) : Key()

    data class CornerDragKey (
        val centerAction: KeyAction,
        val topLeftAction: KeyAction? = null,
        val topRightAction: KeyAction? = null,
        val bottomLeftAction: KeyAction? = null,
        val bottomRightAction: KeyAction? = null,
        override val role: KeyRole,
        override val width: Int = 1,
    ) : Key()
}
