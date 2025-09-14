package com.jherkenhoff.qalculate.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardBackspace
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import kotlin.Int.Companion.MAX_VALUE
import kotlin.Int.Companion.MIN_VALUE


sealed class KeyLabel {
    data class Text(val text: AnnotatedString) : KeyLabel() {
        constructor(text: String) : this(AnnotatedString(text))
    }
    data class Icon(val icon: ImageVector, val description: String) : KeyLabel()
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
}

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
}

object Keys {
    val key0 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("0"), "0"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x⁰"), "⁰"),
        role = KeyRole.NUMBER
    )
    val key1 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("1"), "1"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x¹"), "¹"),
        role = KeyRole.NUMBER
    )

    val key2 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("2"), "2"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x²"), "²"),
        role = KeyRole.NUMBER
    )

    val key3 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("3"), "3"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x³"), "³"),
        role = KeyRole.NUMBER
    )

    val key4 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("4"), "4"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x⁴"), "⁴"),
        role = KeyRole.NUMBER
    )

    val key5 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("5"), "5"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x⁵"), "⁵"),
        role = KeyRole.NUMBER
    )

    val key6 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("6"), "6"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x⁶"), "⁶"),
        role = KeyRole.NUMBER
    )

    val key7 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("7"), "7"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x⁷"), "⁷"),
        role = KeyRole.NUMBER
    )
    val key8 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("8"), "8"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x⁸"), "⁸"),
        role = KeyRole.NUMBER
    )

    val key9 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("9"), "9"),
        longClickAction = KeyAction.InsertText(null, KeyLabel.Text("x⁹"), "⁹"),
        role = KeyRole.NUMBER
    )


    val keyDecimal = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("."), "."), role = KeyRole.NUMBER)
    val keyAns = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("ans"), "ans"), role = KeyRole.OPERATOR)
    val keyBracketOpen = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("("), "("), role = KeyRole.OPERATOR)
    val keyBracketClose = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text(")"), ")"), role = KeyRole.OPERATOR)
    val keyPlus = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("+"), "+"), role = KeyRole.OPERATOR)
    val keyMinus = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("-"), "-"), role = KeyRole.OPERATOR)
    val keyMultiply = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("×"), "×"), role = KeyRole.OPERATOR)
    val keyDivide = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("÷"), "÷"), role = KeyRole.OPERATOR)
    val keyPower = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("^"), "^"), role = KeyRole.OPERATOR)
    val keySqrt = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("√"), "√"), role = KeyRole.OPERATOR)
    val keyUnderscore = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("_"), "_"), role = KeyRole.OPERATOR)
    val keyEqual = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("="), "="), role = KeyRole.OPERATOR)
    val keyPi = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("π"), "π"), role = KeyRole.OPERATOR)
    val keyEuler = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("e"), "e"), role = KeyRole.OPERATOR)
    val keyReturn = Key.DefaultKey(clickAction = KeyAction.Return(KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardReturn, "Return")), role = KeyRole.SYSTEM, width = 2)
    val keyBackspace = Key.DefaultKey(clickAction = KeyAction.Return(KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardBackspace, "Backspace")), role = KeyRole.SYSTEM)
    val keyClearAll = Key.DefaultKey(clickAction = KeyAction.ClearAll(KeyLabel.Text("AC")), role = KeyRole.SYSTEM)


    val keyIntegral = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("∫"), "integral(", ")"), role = KeyRole.OPERATOR)
    val keyDifferential = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("dx"), "diff(", ")"), role = KeyRole.OPERATOR)
    val keySum = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("Σ"), "sum(", ")"),
        longClickAction = KeyAction.InsertText(KeyLabel.Text("Π"), "product(", ")"),
        role = KeyRole.OPERATOR
    )
    val keyPercent = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("%"), "%"), role = KeyRole.OPERATOR)
    val keyInfinity = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("∞"), "∞"), role = KeyRole.OPERATOR)
    val keyImaginary = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("i"), "i"), role = KeyRole.OPERATOR)
    val keyPlusMinus = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("±"), "±"), role = KeyRole.OPERATOR)
    val keyX = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("x"), "x"), role = KeyRole.OPERATOR)
    val keyExp = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("EXP"), "E"), role = KeyRole.OPERATOR)
    val keyConversion = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("x→"), "→"), role = KeyRole.OPERATOR)



    val keyLeft = Key.DefaultKey(
        clickAction = KeyAction.MoveCursor(label = KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Move cursor left"), chars = -1),
        longClickAction = KeyAction.MoveCursor(label = null, popupLabel = KeyLabel.Icon(Icons.AutoMirrored.Filled.ArrowLeft, "Move cursor to start"), chars = MIN_VALUE),
        role = KeyRole.OPERATOR
    )
    val keyRight = Key.DefaultKey(
        clickAction = KeyAction.MoveCursor(label = KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Move cursor right"), chars = 1),
        longClickAction = KeyAction.MoveCursor(label = null, popupLabel = KeyLabel.Icon(Icons.AutoMirrored.Filled.ArrowRight, "Move cursor to end"), chars = MAX_VALUE),
        role = KeyRole.SYSTEM
    )
    val keyUndo = Key.DefaultKey(
        clickAction = KeyAction.Undo(label = KeyLabel.Icon(Icons.AutoMirrored.Default.Undo, "Undo")),
        role = KeyRole.SYSTEM
    )
    val keyRedo = Key.DefaultKey(
        clickAction = KeyAction.Redo(label = KeyLabel.Icon(Icons.AutoMirrored.Default.Redo, "Redo")),
        role = KeyRole.SYSTEM
    )

    val keySin = Key.DefaultKey(
        clickAction = KeyAction.InsertText(label = KeyLabel.Text("sin"), preCursorText = "sin(", postCursorText = ")"),
        longClickAction = KeyAction.InsertText(label = KeyLabel.Text("sin⁻¹"), preCursorText = "arcsin(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keyCos = Key.DefaultKey(
        clickAction = KeyAction.InsertText(label = KeyLabel.Text("cos"), preCursorText = "cos(", postCursorText = ")"),
        longClickAction = KeyAction.InsertText(label = KeyLabel.Text("cos⁻¹"), preCursorText = "arccos(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keyTan = Key.DefaultKey(
        clickAction = KeyAction.InsertText(label = KeyLabel.Text("tan"), preCursorText = "tan(", postCursorText = ")"),
        longClickAction = KeyAction.InsertText(label = KeyLabel.Text("tan⁻¹"), preCursorText = "arctan(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )

    val keyMeter = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText(KeyLabel.Text("nm"), "nm "),
            KeyAction.InsertText(KeyLabel.Text("um"), "um "),
            KeyAction.InsertText(KeyLabel.Text("mm"), "mm "),
            KeyAction.InsertText(KeyLabel.Text("cm"), "cm "),
            KeyAction.InsertText(KeyLabel.Text("m"), "m "),
            KeyAction.InsertText(KeyLabel.Text("km"), "km "),
        ),
        4,
        role= KeyRole.OPERATOR
    )
}