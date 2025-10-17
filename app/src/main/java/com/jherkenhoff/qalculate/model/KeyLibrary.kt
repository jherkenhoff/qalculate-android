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


private fun superscriptSymbol(base: String, superscript: String) : AnnotatedString {
    return buildAnnotatedString {
        append(base)
        withStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 0.6.em)) {
            append(superscript)
        }
    }
}

object Keys {
    val key0 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("0"), "0"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text(superscriptSymbol("x", "0")), "⁰"),
        role = KeyRole.NUMBER
    )
    val key1 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("1"), "1"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "1")), "¹"),
        role = KeyRole.NUMBER
    )

    val key2 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("2"), "2"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "2")), "²"),
        role = KeyRole.NUMBER
    )

    val key3 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("3"), "3"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "3")), "³"),
        role = KeyRole.NUMBER
    )

    val key4 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("4"), "4"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "4")), "⁴"),
        role = KeyRole.NUMBER
    )

    val key5 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("5"), "5"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "5")), "⁵"),
        role = KeyRole.NUMBER
    )

    val key6 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("6"), "6"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "6")), "⁶"),
        role = KeyRole.NUMBER
    )

    val key7 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("7"), "7"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "7")), "⁷"),
        role = KeyRole.NUMBER
    )
    val key8 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("8"), "8"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "8")), "⁸"),
        role = KeyRole.NUMBER
    )

    val key9 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("9"), "9"),
        topRightAction = KeyAction.InsertText( KeyLabel.Text(superscriptSymbol("x", "9")), "⁹"),
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

    val keyAns = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("ans"), "ans"), role = KeyRole.OPERATOR)
    val keyBracketOpen = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("("), "("),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("["), "["),
        role = KeyRole.OPERATOR
    )
    val keyBracketClose = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text(")"), ")"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("]"), "]"),
        role = KeyRole.OPERATOR
    )
    val keyPlus = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("+"), "+"), role = KeyRole.OPERATOR)
    val keyMinus = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("-"), "-"), role = KeyRole.OPERATOR)
    val keyMultiply = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("×"), "×"), role = KeyRole.OPERATOR)
    val keyDivide = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("÷"), "÷"), role = KeyRole.OPERATOR)
    val keyPower = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text(superscriptSymbol("x", "y")), "^"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("E"), "E"),
        role = KeyRole.OPERATOR
    )
    val keySqrt = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("√"), "sqrt(", ")"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("∛"), "cbrt(", ")"),
        role = KeyRole.OPERATOR
    )
    val keyUnderscore = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("_"), "_"), role = KeyRole.OPERATOR)
    val keyEqual = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("="), "="), role = KeyRole.OPERATOR)
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
    val keyEuler = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("e"), "e"), role = KeyRole.OPERATOR)
    val keyReturn = Key.CornerDragKey(
        centerAction = KeyAction.Return(KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardReturn, "Return")),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("ans"), "ans"),
        role = KeyRole.SYSTEM,
        width = 2
    )
    val keyBackspace = Key.CornerDragKey(centerAction = KeyAction.Backspace(KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardBackspace, "Backspace")), role = KeyRole.SYSTEM)
    val keyClearAll = Key.CornerDragKey(centerAction = KeyAction.ClearAll(KeyLabel.Text("AC")), role = KeyRole.SYSTEM)

    val keyIntegral = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("∫"), "integral(", ")"), role = KeyRole.OPERATOR)
    val keyDifferential = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("dx"), "diff(", ")"), role = KeyRole.OPERATOR)
    val keySum = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("Σ"), "sum(", ")"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("Π"), "product(", ")"),
        role = KeyRole.OPERATOR
    )
    val keyInfinity = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("∞"), "∞"), role = KeyRole.OPERATOR)
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

    val keyY = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("Y"), "y"),
        topRightAction = KeyAction.StoreAsVariable(null, KeyLabel.Text("→Y"), "x"),
        role = KeyRole.OPERATOR
    )
    val keyZ = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("Z"), "z"),
        topRightAction = KeyAction.StoreAsVariable(null, KeyLabel.Text("→Z"), "z"),
        role = KeyRole.OPERATOR
    )
    val keyExp = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("E"), "E"), role = KeyRole.OPERATOR)
    val keyConversion = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Icon(Icons.AutoMirrored.Filled.ArrowRightAlt, null), "→"), role = KeyRole.OPERATOR)


    val keyLeft = Key.CornerDragKey(
        centerAction = KeyAction.MoveCursor(label = KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Move cursor left"), chars = -1),
        topRightAction = KeyAction.MoveCursor(label = null, popupLabel = KeyLabel.Icon(Icons.AutoMirrored.Filled.ArrowLeft, "Move cursor to start"), chars = MIN_VALUE),
        role = KeyRole.OPERATOR
    )
    val keyRight = Key.CornerDragKey(
        centerAction = KeyAction.MoveCursor(label = KeyLabel.Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Move cursor right"), chars = 1),
        topRightAction = KeyAction.MoveCursor(label = null, popupLabel = KeyLabel.Icon(Icons.AutoMirrored.Filled.ArrowRight, "Move cursor to end"), chars = MAX_VALUE),
        role = KeyRole.SYSTEM
    )
    val keyUndo = Key.CornerDragKey(
        centerAction = KeyAction.Undo(label = KeyLabel.Icon(Icons.AutoMirrored.Default.Undo, "Undo")),
        role = KeyRole.SYSTEM
    )
    val keyRedo = Key.CornerDragKey(
        centerAction = KeyAction.Redo(label = KeyLabel.Icon(Icons.AutoMirrored.Default.Redo, "Redo")),
        role = KeyRole.SYSTEM
    )

    //    val keySin = Key.CornerDragKey(
//        centerAction = KeyAction.InsertText(label = KeyLabel.Text("sin"), preCursorText = "sin(", postCursorText = ")"),
//        topRightAction = KeyAction.InsertText(label = KeyLabel.Text("sin⁻¹"), preCursorText = "arcsin(", postCursorText = ")"),
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
