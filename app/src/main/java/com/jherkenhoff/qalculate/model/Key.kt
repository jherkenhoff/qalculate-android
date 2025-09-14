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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import kotlin.Int.Companion.MAX_VALUE
import kotlin.Int.Companion.MIN_VALUE


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

object Keys {
    val key0 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("0"), "0"),
        longClickAction = KeyAction.InsertText(KeyLabel.Text(superscriptSymbol("x", "0")), "⁰"),
        role = KeyRole.NUMBER
    )
    val key1 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("1"), "1"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "1")), "¹"),
        role = KeyRole.NUMBER
    )

    val key2 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("2"), "2"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "2")), "²"),
        role = KeyRole.NUMBER
    )

    val key3 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("3"), "3"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "3")), "³"),
        role = KeyRole.NUMBER
    )

    val key4 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("4"), "4"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "4")), "⁴"),
        role = KeyRole.NUMBER
    )

    val key5 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("5"), "5"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "5")), "⁵"),
        role = KeyRole.NUMBER
    )

    val key6 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("6"), "6"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "6")), "⁶"),
        role = KeyRole.NUMBER
    )

    val key7 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("7"), "7"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "7")), "⁷"),
        role = KeyRole.NUMBER
    )
    val key8 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("8"), "8"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "8")), "⁸"),
        role = KeyRole.NUMBER
    )

    val key9 = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("9"), "9"),
        longClickAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "9")), "⁹"),
        role = KeyRole.NUMBER
    )

    val keyDecimal = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("."), "."),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("␣"), " "),
        bottomRightAction = KeyAction.InsertText(KeyLabel.Text(","), ","),
        role = KeyRole.NUMBER
    )



    val keyLn = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("ln"), "ln(", ")"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("log"), "log10(", ")"),
        role = KeyRole.OPERATOR
    )

    val keyAns = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("ans"), "ans"), role = KeyRole.OPERATOR)
    val keyBracketOpen = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("("), "("),
        longClickAction = KeyAction.InsertText(KeyLabel.Text("["), "["),
        role = KeyRole.OPERATOR
    )
    val keyBracketClose = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text(")"), ")"),
        longClickAction = KeyAction.InsertText(KeyLabel.Text("]"), "]"),
        role = KeyRole.OPERATOR
    )
    val keyPlus = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("+"), "+"), role = KeyRole.OPERATOR)
    val keyMinus = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("-"), "-"), role = KeyRole.OPERATOR)
    val keyMultiply = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("×"), "×"), role = KeyRole.OPERATOR)
    val keyDivide = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("÷"), "÷"), role = KeyRole.OPERATOR)
    val keyPower = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text(superscriptSymbol("x", "y")), "^"),
        longClickAction = KeyAction.InsertText(KeyLabel.Text("E"), "E"),
        role = KeyRole.OPERATOR
    )
    val keySqrt = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("√"), "sqrt(", ")"),
        longClickAction = KeyAction.InsertText(KeyLabel.Text("∛"), "cbrt(", ")"),
        role = KeyRole.OPERATOR
    )
    val keyUnderscore = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("_"), "_"), role = KeyRole.OPERATOR)
    val keyEqual = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("="), "="), role = KeyRole.OPERATOR)
    val keyPi = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("π"), "π"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("e"), "e"),
        bottomRightAction = KeyAction.InsertText(KeyLabel.Text(","), ","),
        role = KeyRole.OPERATOR
    )
    val keyFactorial = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("!"), "!"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("e"), "e"),
        bottomRightAction = KeyAction.InsertText(KeyLabel.Text(","), ","),
        role = KeyRole.OPERATOR
    )
    val keyEuler = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("e"), "e"), role = KeyRole.OPERATOR)
    val keyReturn = Key.DefaultKey(
        clickAction = KeyAction.Return(KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardReturn, "Return")),
        longClickAction = KeyAction.InsertText(KeyLabel.Text("ans"), "ans"),
        role = KeyRole.SYSTEM,
        width = 2
    )
    val keyBackspace = Key.DefaultKey(clickAction = KeyAction.Backspace(KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardBackspace, "Backspace")), role = KeyRole.SYSTEM)
    val keyClearAll = Key.DefaultKey(clickAction = KeyAction.ClearAll(KeyLabel.Text("AC")), role = KeyRole.SYSTEM)

    val keyIntegral = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("∫"), "integral(", ")"), role = KeyRole.OPERATOR)
    val keyDifferential = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("dx"), "diff(", ")"), role = KeyRole.OPERATOR)
    val keySum = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("Σ"), "sum(", ")"),
        longClickAction = KeyAction.InsertText(KeyLabel.Text("Π"), "product(", ")"),
        role = KeyRole.OPERATOR
    )
    val keyInfinity = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("∞"), "∞"), role = KeyRole.OPERATOR)
    val keyImaginary = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("i"), "i"),
        topLeftAction = KeyAction.InsertText(KeyLabel.Text("Re"), popupLabel = KeyLabel.Text("Real"), "re(", ")"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("Im"), popupLabel = KeyLabel.Text("Imag."), "im(", ")"),
        bottomRightAction = KeyAction.InsertText(KeyLabel.Text("∠"), "∠"),
        role = KeyRole.OPERATOR
    )

    val keyPercent = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("%"), "%"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("±"), "±"),
        bottomRightAction = KeyAction.InsertText(KeyLabel.Text("!"), "!"),
        role = KeyRole.OPERATOR
    )

    val keyX = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("X"), "x "),
        topLeftAction = KeyAction.InsertText(KeyLabel.Text("Y"), "y "),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("Z"), "z "),
        role= KeyRole.OPERATOR
    )

    val keyY = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("Y"), "y"),
        longClickAction = KeyAction.StoreAsVariable(null, KeyLabel.Text("→Y"), "x"),
        role = KeyRole.OPERATOR
    )
    val keyZ = Key.DefaultKey(
        clickAction = KeyAction.InsertText(KeyLabel.Text("Z"), "z"),
        longClickAction = KeyAction.StoreAsVariable(null, KeyLabel.Text("→Z"), "z"),
        role = KeyRole.OPERATOR
    )
    val keyExp = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Text("E"), "E"), role = KeyRole.OPERATOR)
    val keyConversion = Key.DefaultKey(clickAction = KeyAction.InsertText(KeyLabel.Icon(Icons.AutoMirrored.Filled.ArrowRightAlt, null), "→"), role = KeyRole.OPERATOR)


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

//    val keySin = Key.DefaultKey(
//        clickAction = KeyAction.InsertText(label = KeyLabel.Text("sin"), preCursorText = "sin(", postCursorText = ")"),
//        longClickAction = KeyAction.InsertText(label = KeyLabel.Text("sin⁻¹"), preCursorText = "arcsin(", postCursorText = ")"),
//        role = KeyRole.OPERATOR
//    )
    val keySin = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(label = KeyLabel.Text("sin"), preCursorText = "sin(", postCursorText = ")"),
        topRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("sin", "-1")), preCursorText = "asin(", postCursorText = ")"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("sinh"), preCursorText = "sinh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("sinh", "-1")), preCursorText = "asinh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keyCos = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(label = KeyLabel.Text("cos"), preCursorText = "cos(", postCursorText = ")"),
        topRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("cos", "-1")), preCursorText = "acos(", postCursorText = ")"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("cosh"), preCursorText = "cosh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("cosh", "-1")), preCursorText = "acosh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keyTan = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(label = KeyLabel.Text("tan"), preCursorText = "tan(", postCursorText = ")"),
        topRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("tan", "-1")), preCursorText = "atan(", postCursorText = ")"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("tanh"), preCursorText = "tanh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("tanh", "-1")), preCursorText = "atanh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )

    val keySiLength = Key.SelectorKey(
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

    val keyImperialLength = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText(KeyLabel.Text("thou"), "thou "),
            KeyAction.InsertText(KeyLabel.Text("inch"), "in "),
            KeyAction.InsertText(KeyLabel.Text("foot"), "ft "),
            KeyAction.InsertText(KeyLabel.Text("yard"), "yd "),
            KeyAction.InsertText(KeyLabel.Text("mile"), "mile "),
        ),
        1,
        role= KeyRole.OPERATOR
    )

    val keyImperialWeight = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText(KeyLabel.Text("grain"), "gr "),
            KeyAction.InsertText(KeyLabel.Text("ounce"), "oz "),
            KeyAction.InsertText(KeyLabel.Text("pound"), "lb "),
            KeyAction.InsertText(KeyLabel.Text("stone"), "stone "),
        ),
        2,
        role= KeyRole.OPERATOR
    )

    val keySiWeight = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText(KeyLabel.Text("pg"), "pg "),
            KeyAction.InsertText(KeyLabel.Text("ng"), "ng "),
            KeyAction.InsertText(KeyLabel.Text("µg"), "µg "),
            KeyAction.InsertText(KeyLabel.Text("mg"), "mg "),
            KeyAction.InsertText(KeyLabel.Text("g"), "g "),
            KeyAction.InsertText(KeyLabel.Text("kg"), "kg "),
            KeyAction.InsertText(KeyLabel.Text("t"), "t "),
        ),
        5,
        role= KeyRole.OPERATOR
    )
}

private fun superscriptSymbol(base: String, superscript: String) : AnnotatedString {
    return buildAnnotatedString {
        append(base)
        withStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 0.6.em)) {
            append(superscript)
        }
    }
}