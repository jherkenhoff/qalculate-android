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

    data class InsertText(
        override val label: ActionLabel?,
        override val popupLabel: ActionLabel?,
        val preCursorText: String,
        val postCursorText: String = "",
        val selectionPolicy: SelectionPolicy = SelectionPolicy.REPLACE
    ) : Action() {
        enum class SelectionPolicy{REPLACE, SURROUND, PARENTHESES}
        constructor(label: ActionLabel, preCursorText: String, postCursorText: String = "", selectionPolicy: SelectionPolicy = SelectionPolicy.REPLACE) : this(label, label, preCursorText, postCursorText, selectionPolicy)

        companion object {
            fun function(label: ActionLabel, function: String): InsertText = InsertText(label, label, preCursorText = "$function(", postCursorText = ")", selectionPolicy = SelectionPolicy.SURROUND)
            fun operator(label: ActionLabel, operator: String): InsertText = InsertText(label, label, preCursorText = operator, selectionPolicy = SelectionPolicy.PARENTHESES)
        }
    }

    data class Backspace(override val label: ActionLabel?, override val popupLabel: ActionLabel?, val nChars: Int = 1): Action() {
        constructor(label: ActionLabel) : this(label, label)
    }
    class ClearAll(override val label: ActionLabel?, override val popupLabel: ActionLabel?): Action() {
        constructor(label: ActionLabel) : this(label, label)
    }
    class Return(override val label: ActionLabel?, override val popupLabel: ActionLabel?): Action() {
        constructor(label: ActionLabel) : this(label, label)
    }
    class MoveCursor(override val label: ActionLabel?, override val popupLabel: ActionLabel?, val chars: Int): Action() {
        constructor(label: ActionLabel, chars: Int) : this(label, label, chars)
    }
    class Undo(override val label: ActionLabel?, override val popupLabel: ActionLabel?): Action() {
        constructor(label: ActionLabel) : this(label, label)
    }
    class Redo(override val label: ActionLabel?, override val popupLabel: ActionLabel?): Action() {
        constructor(label: ActionLabel) : this(label, label)
    }

    class StoreAsVariable(override val label: ActionLabel?, override val popupLabel: ActionLabel?, val variableName: String): Action() {
        constructor(label: ActionLabel, variableName: String) : this(label, label, variableName)
    }
}