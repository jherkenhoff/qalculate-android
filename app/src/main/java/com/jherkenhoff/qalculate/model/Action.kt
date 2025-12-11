package com.jherkenhoff.qalculate.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString

sealed class ActionLabel {
    data class Text(val text: AnnotatedString) : ActionLabel() {
        constructor(text: String) : this(AnnotatedString(text))
    }
    data class Icon(val icon: ImageVector, val description: String?) : ActionLabel()
}


sealed class Action {
    abstract val label: ActionLabel?
    abstract val popupLabel: ActionLabel?
    abstract val enabled: Boolean

    data class InsertText(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        val preCursorText: String,
        val postCursorText: String = "",
        val selectionPolicy: SelectionPolicy = SelectionPolicy.REPLACE,
        override val enabled: Boolean = true
    ) : Action() {
        enum class SelectionPolicy{REPLACE, SURROUND, PARENTHESES}
        constructor(label: ActionLabel, preCursorText: String, postCursorText: String = "", selectionPolicy: SelectionPolicy = SelectionPolicy.REPLACE, enabled: Boolean = true) : this(label, label, preCursorText, postCursorText, selectionPolicy, enabled)

        companion object {
            fun function(label: ActionLabel, function: String, enabled: Boolean = true): InsertText = InsertText(label, label, preCursorText = "$function(", postCursorText = ")", selectionPolicy = SelectionPolicy.SURROUND, enabled)
            fun operator(label: ActionLabel, operator: String, enabled: Boolean = true): InsertText = InsertText(label, label, preCursorText = operator, selectionPolicy = SelectionPolicy.PARENTHESES, enabled = enabled)
        }
    }

    data class Backspace(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        val nChars: Int = 1,
        override val enabled: Boolean = true
    ): Action() {
        constructor(label: ActionLabel, enabled: Boolean = true) : this(label, label, enabled = enabled)
    }
    class ClearAll(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        override val enabled: Boolean = true
    ): Action() {
        constructor(label: ActionLabel, enabled: Boolean = true) : this(label, label, enabled = enabled)
    }
    class Return(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        override val enabled: Boolean = true
    ): Action() {
        constructor(label: ActionLabel, enabled: Boolean = true) : this(label, label, enabled = enabled)
    }
    class MoveCursor(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        val chars: Int,
        override val enabled: Boolean = true
    ): Action() {
        constructor(label: ActionLabel, chars: Int, enabled: Boolean = true) : this(label, label, chars, enabled = enabled)
    }
    class Undo(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        override val enabled: Boolean = true
    ): Action() {
        constructor(label: ActionLabel, enabled: Boolean = true) : this(label, label, enabled = enabled)
    }
    class Redo(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        override val enabled: Boolean = true
    ): Action() {
        constructor(label: ActionLabel, enabled: Boolean = true) : this(label, label, enabled = enabled)
    }

    class StoreAsVariable(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        val variableName: String,
        override val enabled: Boolean = true
    ): Action() {
        constructor(label: ActionLabel, variableName: String, enabled: Boolean = true) : this(label, label, variableName, enabled = enabled)
    }
}