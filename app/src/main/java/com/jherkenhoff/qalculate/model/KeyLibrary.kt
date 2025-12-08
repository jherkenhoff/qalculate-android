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
import com.jherkenhoff.qalculate.ui.calculator.KeyLabel
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
        topRightAction = KeyAction.InsertText.operator(KeyLabel.Text(superscriptSymbol("x", "0")), "⁰"),
        role = KeyRole.NUMBER
    )
    val key1 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("1"), "1"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "1")), "¹"),
        role = KeyRole.NUMBER
    )

    val key2 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("2"), "2"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "2")), "²"),
        role = KeyRole.NUMBER
    )

    val key3 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("3"), "3"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "3")), "³"),
        role = KeyRole.NUMBER
    )

    val key4 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("4"), "4"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "4")), "⁴"),
        role = KeyRole.NUMBER
    )

    val key5 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("5"), "5"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "5")), "⁵"),
        role = KeyRole.NUMBER
    )

    val key6 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("6"), "6"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "6")), "⁶"),
        role = KeyRole.NUMBER
    )

    val key7 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("7"), "7"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "7")), "⁷"),
        role = KeyRole.NUMBER
    )
    val key8 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("8"), "8"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "8")), "⁸"),
        role = KeyRole.NUMBER
    )

    val key9 = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("9"), "9"),
        topRightAction = KeyAction.InsertText.operator( KeyLabel.Text(superscriptSymbol("x", "9")), "⁹"),
        role = KeyRole.NUMBER
    )

    val keyDecimal = Key.CornerDragKey(
            centerAction = KeyAction.InsertText(KeyLabel.Text("."), "."),
            topRightAction = KeyAction.InsertText(KeyLabel.Text("␣"), " "),
            bottomRightAction = KeyAction.InsertText(KeyLabel.Text(","), ","),
            role = KeyRole.NUMBER
    )

    val keyLn = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.function(KeyLabel.Text("ln"), "ln"),
        topRightAction = KeyAction.InsertText.function(KeyLabel.Text("log"), "log10"),
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
    val keyPlus = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.operator(KeyLabel.Text("+"), "+"),
        role = KeyRole.OPERATOR
    )
    val keyMinus = Key.CornerDragKey(centerAction = KeyAction.InsertText.operator(KeyLabel.Text("-"), "-"), role = KeyRole.OPERATOR)
    val keyMultiply = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("×"), "×"), role = KeyRole.OPERATOR)
    val keyDivide = Key.CornerDragKey(centerAction = KeyAction.InsertText(KeyLabel.Text("÷"), "÷"), role = KeyRole.OPERATOR)
    val keyPower = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.operator(KeyLabel.Text(superscriptSymbol("x", "y")), "^"),
        role = KeyRole.OPERATOR
    )
    val keySqrt = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.function(KeyLabel.Text("√"), "sqrt"),
        role = KeyRole.OPERATOR
    )

    val keyPlusMinus = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("±"), "±"),
        role = KeyRole.OPERATOR
    )
    val keyUnderscore = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("_"), "_"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text(";"), ";"),
        role = KeyRole.OPERATOR
    )
    val keyEqual = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("="), "="),
        topRightAction = KeyAction.InsertText(KeyLabel.Text(","), ","),
        role = KeyRole.OPERATOR
    )
    val keyPi = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("π"), "π"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("e"), "e"),
        role = KeyRole.OPERATOR
    )
    val keyFactorial = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("∞"), "∞"),
        topRightAction = KeyAction.InsertText.operator(KeyLabel.Text("!"), "!"),
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

    val keyIntegral = Key.CornerDragKey(centerAction = KeyAction.InsertText.function(KeyLabel.Text("∫"), "integral"), role = KeyRole.OPERATOR)
    val keyDifferential = Key.CornerDragKey(centerAction = KeyAction.InsertText.function(KeyLabel.Text("dx"), "diff"), role = KeyRole.OPERATOR)
    val keySum = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.function(KeyLabel.Text("Σ"), "sum"),
        topRightAction = KeyAction.InsertText.function(KeyLabel.Text("Π"), "product"),
        role = KeyRole.OPERATOR
    )
    val keyInfinity = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("∞"), "∞"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("!"), "!"),
        role = KeyRole.OPERATOR
    )
    val keyImaginary = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("i"), "i"),
        topRightAction = KeyAction.InsertText(KeyLabel.Text("∠"), "∠"),
        role = KeyRole.OPERATOR
    )
    val keyComplexOperators = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText.function(KeyLabel.Text("Abs."), "abs"),
            KeyAction.InsertText.function(KeyLabel.Text("Arg."), "arg"),
            KeyAction.InsertText.function(KeyLabel.Text("Real"), "re"),
            KeyAction.InsertText.function(KeyLabel.Text("Imag."), "im"),
            KeyAction.InsertText.function(KeyLabel.Text("Conj."), "conj")
        ),
        2,
        role = KeyRole.OPERATOR
    )
    val keyPercent = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.operator(KeyLabel.Text("%"), "%"),
        topRightAction = KeyAction.InsertText.operator(KeyLabel.Text("±"), "±"),
        role = KeyRole.OPERATOR
    )
    val keyX = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("X"), "x"),
        topRightAction = KeyAction.StoreAsVariable(KeyLabel.Text("→x"), KeyLabel.Text("→x"), "x"),
        role= KeyRole.OPERATOR
    )
    val keyY = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("Y"), "y"),
        topRightAction = KeyAction.StoreAsVariable(KeyLabel.Text("→y"), KeyLabel.Text("→y"), "x"),
        role = KeyRole.OPERATOR
    )
    val keyZ = Key.CornerDragKey(
        centerAction = KeyAction.InsertText(KeyLabel.Text("Z"), "z"),
        topRightAction = KeyAction.StoreAsVariable(KeyLabel.Text("→z"), KeyLabel.Text("→z"), "z"),
        role = KeyRole.OPERATOR
    )
    val keyExp = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.operator(KeyLabel.Text("E"), "E"),
        role = KeyRole.OPERATOR
    )
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
        centerAction = KeyAction.InsertText.function(KeyLabel.Text("sin"), "sin"),
        topRightAction = KeyAction.InsertText.function(label = KeyLabel.Text(superscriptSymbol("sin", "-1")), "asin"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("sinh"), preCursorText = "sinh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("sinh", "-1")), preCursorText = "asinh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keyCos = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.function(label = KeyLabel.Text("cos"), "cos"),
        topRightAction = KeyAction.InsertText.function(label = KeyLabel.Text(superscriptSymbol("cos", "-1")), "acos"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("cosh"), preCursorText = "cosh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("cosh", "-1")), preCursorText = "acosh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keyTan = Key.CornerDragKey(
        centerAction = KeyAction.InsertText.function(label = KeyLabel.Text("tan"), "tan"),
        topRightAction = KeyAction.InsertText.function(label = KeyLabel.Text(superscriptSymbol("tan", "-1")), "atan"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("tanh"), preCursorText = "tanh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("tanh", "-1")), preCursorText = "atanh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )

    val keySiLength = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText.operator(KeyLabel.Text("nm"), "nm "),
            KeyAction.InsertText.operator(KeyLabel.Text("um"), "um "),
            KeyAction.InsertText.operator(KeyLabel.Text("mm"), "mm "),
            KeyAction.InsertText.operator(KeyLabel.Text("cm"), "cm "),
            KeyAction.InsertText.operator(KeyLabel.Text("m"), "m "),
            KeyAction.InsertText.operator(KeyLabel.Text("km"), "km "),
        ),
        4,
        role= KeyRole.OPERATOR
    )

    val keyImperialLength = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText.operator(KeyLabel.Text("thou"), "thou "),
            KeyAction.InsertText.operator(KeyLabel.Text("inch"), "in "),
            KeyAction.InsertText.operator(KeyLabel.Text("foot"), "ft "),
            KeyAction.InsertText.operator(KeyLabel.Text("yard"), "yd "),
            KeyAction.InsertText.operator(KeyLabel.Text("mile"), "mile "),
        ),
        1,
        role= KeyRole.OPERATOR
    )

    val keyImperialWeight = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText.operator(KeyLabel.Text("grain"), "gr "),
            KeyAction.InsertText.operator(KeyLabel.Text("ounce"), "oz "),
            KeyAction.InsertText.operator(KeyLabel.Text("pound"), "lb "),
            KeyAction.InsertText.operator(KeyLabel.Text("stone"), "stone "),
        ),
        2,
        role= KeyRole.OPERATOR
    )

    val keySiWeight = Key.SelectorKey(
        arrayOf(
            KeyAction.InsertText.operator(KeyLabel.Text("pg"), "pg "),
            KeyAction.InsertText.operator(KeyLabel.Text("ng"), "ng "),
            KeyAction.InsertText.operator(KeyLabel.Text("µg"), "µg "),
            KeyAction.InsertText.operator(KeyLabel.Text("mg"), "mg "),
            KeyAction.InsertText.operator(KeyLabel.Text("g"), "g "),
            KeyAction.InsertText.operator(KeyLabel.Text("kg"), "kg "),
            KeyAction.InsertText.operator(KeyLabel.Text("t"), "t "),
        ),
        5,
        role= KeyRole.OPERATOR
    )
}
