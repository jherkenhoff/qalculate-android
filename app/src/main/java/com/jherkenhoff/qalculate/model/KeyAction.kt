package com.jherkenhoff.qalculate.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString

sealed class KeyLabel {
    data class Text(val text: AnnotatedString) : KeyLabel() {
        constructor(text: String) : this(AnnotatedString(text))
    }
    data class Icon(val icon: ImageVector, val description: String?) : KeyLabel()
}


sealed class KeyAction {
    abstract val label: KeyLabel?
    abstract val popupLabel: KeyLabel?

    data class InsertText(override val label: KeyLabel?, override val popupLabel: KeyLabel?, val preCursorText: String, val postCursorText: String = "") : KeyAction() {
        constructor(label: KeyLabel, preCursorText: String, postCursorText: String = "") : this(label, label, preCursorText, postCursorText)
    }
    data class Backspace(override val label: KeyLabel?, override val popupLabel: KeyLabel?, val nChars: Int = 1): KeyAction() {
        constructor(label: KeyLabel) : this(label, label)
    }
    class ClearAll(override val label: KeyLabel?, override val popupLabel: KeyLabel?): KeyAction() {
        constructor(label: KeyLabel) : this(label, label)
    }
    class Return(override val label: KeyLabel?, override val popupLabel: KeyLabel?): KeyAction() {
        constructor(label: KeyLabel) : this(label, label)
    }
    class MoveCursor(override val label: KeyLabel?, override val popupLabel: KeyLabel?, val chars: Int): KeyAction() {
        constructor(label: KeyLabel, chars: Int) : this(label, label, chars)
    }
    class Undo(override val label: KeyLabel?, override val popupLabel: KeyLabel?): KeyAction() {
        constructor(label: KeyLabel) : this(label, label)
    }
    class Redo(override val label: KeyLabel?, override val popupLabel: KeyLabel?): KeyAction() {
        constructor(label: KeyLabel) : this(label, label)
    }

    class StoreAsVariable(override val label: KeyLabel?, override val popupLabel: KeyLabel?, val variableName: String): KeyAction() {
        constructor(label: KeyLabel, variableName: String) : this(label, label, variableName)
    }
}