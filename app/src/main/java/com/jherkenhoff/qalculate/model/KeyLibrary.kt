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
    val keySpec0 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("0"), "0"),
        topRightAction = Action.InsertText.operator(ActionLabel.Text(superscriptSymbol("x", "0")), "⁰"),
        role = KeyRole.NUMBER
    )
    val keySpec1 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("1"), "1"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "1")), "¹"),
        role = KeyRole.NUMBER
    )

    val keySpec2 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("2"), "2"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "2")), "²"),
        role = KeyRole.NUMBER
    )

    val keySpec3 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("3"), "3"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "3")), "³"),
        role = KeyRole.NUMBER
    )

    val keySpec4 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("4"), "4"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "4")), "⁴"),
        role = KeyRole.NUMBER
    )

    val keySpec5 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("5"), "5"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "5")), "⁵"),
        role = KeyRole.NUMBER
    )

    val keySpec6 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("6"), "6"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "6")), "⁶"),
        role = KeyRole.NUMBER
    )

    val keySpec7 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("7"), "7"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "7")), "⁷"),
        role = KeyRole.NUMBER
    )
    val keySpec8 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("8"), "8"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "8")), "⁸"),
        role = KeyRole.NUMBER
    )

    val keySpec9 = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("9"), "9"),
        topRightAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "9")), "⁹"),
        role = KeyRole.NUMBER
    )

    val keySpecDecimal = KeySpec.CornerDragKeySpec(
            centerAction = Action.InsertText(ActionLabel.Text("."), "."),
            topRightAction = Action.InsertText(ActionLabel.Text("␣"), " "),
            bottomRightAction = Action.InsertText(ActionLabel.Text(","), ","),
            role = KeyRole.NUMBER
    )

    val keySpecLn = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.function(ActionLabel.Text("ln"), "ln"),
        topRightAction = Action.InsertText.function(ActionLabel.Text("log"), "log10"),
        role = KeyRole.OPERATOR
    )

    val keySpecAns = KeySpec.CornerDragKeySpec(centerAction = Action.InsertText(ActionLabel.Text("ans"), "ans"), role = KeyRole.OPERATOR)
    val keySpecBracketOpen = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("("), "("),
        topRightAction = Action.InsertText(ActionLabel.Text("["), "["),
        role = KeyRole.OPERATOR
    )
    val keySpecBracketClose = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text(")"), ")"),
        topRightAction = Action.InsertText(ActionLabel.Text("]"), "]"),
        role = KeyRole.OPERATOR
    )
    val keySpecPlus = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.operator(ActionLabel.Text("+"), "+"),
        role = KeyRole.OPERATOR
    )
    val keySpecMinus = KeySpec.CornerDragKeySpec(centerAction = Action.InsertText.operator(ActionLabel.Text("-"), "-"), role = KeyRole.OPERATOR)
    val keySpecMultiply = KeySpec.CornerDragKeySpec(centerAction = Action.InsertText(ActionLabel.Text("×"), "×"), role = KeyRole.OPERATOR)
    val keySpecDivide = KeySpec.CornerDragKeySpec(centerAction = Action.InsertText(ActionLabel.Text("÷"), "÷"), role = KeyRole.OPERATOR)
    val keySpecPower = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.operator(ActionLabel.Text(superscriptSymbol("x", "y")), "^"),
        role = KeyRole.OPERATOR
    )
    val keySpecSqrt = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.function(ActionLabel.Text("√"), "sqrt"),
        role = KeyRole.OPERATOR
    )

    val keySpecPlusMinus = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("±"), "±"),
        role = KeyRole.OPERATOR
    )
    val keySpecUnderscore = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("_"), "_"),
        topRightAction = Action.InsertText(ActionLabel.Text(";"), ";"),
        role = KeyRole.OPERATOR
    )
    val keySpecEqual = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("="), "="),
        topRightAction = Action.InsertText(ActionLabel.Text(","), ","),
        role = KeyRole.OPERATOR
    )
    val keySpecPi = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("π"), "π"),
        topRightAction = Action.InsertText(ActionLabel.Text("e"), "e"),
        role = KeyRole.OPERATOR
    )
    val keySpecFactorial = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("∞"), "∞"),
        topRightAction = Action.InsertText.operator(ActionLabel.Text("!"), "!"),
        role = KeyRole.OPERATOR
    )
    val keySpecEuler = KeySpec.CornerDragKeySpec(centerAction = Action.InsertText(ActionLabel.Text("e"), "e"), role = KeyRole.OPERATOR)
    val keySpecReturn = KeySpec.CornerDragKeySpec(
        centerAction = Action.Return(ActionLabel.Icon(Icons.AutoMirrored.Filled.KeyboardReturn, "Return")),
        topRightAction = Action.InsertText(ActionLabel.Text("ans"), "ans"),
        role = KeyRole.SYSTEM
    )
    val keySpecBackspace = KeySpec.CornerDragKeySpec(centerAction = Action.Backspace(ActionLabel.Icon(Icons.AutoMirrored.Filled.KeyboardBackspace, "Backspace")), role = KeyRole.SYSTEM)
    val keySpecClearAll = KeySpec.CornerDragKeySpec(centerAction = Action.ClearAll(ActionLabel.Text("AC")), role = KeyRole.SYSTEM)

    val keySpecIntegral = KeySpec.CornerDragKeySpec(centerAction = Action.InsertText.function(ActionLabel.Text("∫"), "integral"), role = KeyRole.OPERATOR)
    val keySpecDifferential = KeySpec.CornerDragKeySpec(centerAction = Action.InsertText.function(ActionLabel.Text("dx"), "diff"), role = KeyRole.OPERATOR)
    val keySpecSum = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.function(ActionLabel.Text("Σ"), "sum"),
        topRightAction = Action.InsertText.function(ActionLabel.Text("Π"), "product"),
        role = KeyRole.OPERATOR
    )
    val keySpecInfinity = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("∞"), "∞"),
        topRightAction = Action.InsertText(ActionLabel.Text("!"), "!"),
        role = KeyRole.OPERATOR
    )
    val keySpecImaginary = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("i"), "i"),
        topRightAction = Action.InsertText(ActionLabel.Text("∠"), "∠"),
        role = KeyRole.OPERATOR
    )
    val keySpecComplexOperators = KeySpec.SelectorKeySpec(
        listOf(
            Action.InsertText.function(ActionLabel.Text("Abs."), "abs"),
            Action.InsertText.function(ActionLabel.Text("Arg."), "arg"),
            Action.InsertText.function(ActionLabel.Text("Real"), "re"),
            Action.InsertText.function(ActionLabel.Text("Imag."), "im"),
            Action.InsertText.function(ActionLabel.Text("Conj."), "conj")
        ),
        2,
        role = KeyRole.OPERATOR
    )
    val keySpecPercent = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.operator(ActionLabel.Text("%"), "%"),
        topRightAction = Action.InsertText.operator(ActionLabel.Text("±"), "±"),
        role = KeyRole.OPERATOR
    )
    val keySpecX = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("X"), "x"),
        topRightAction = Action.StoreAsVariable(ActionLabel.Text("→x"), ActionLabel.Text("→x"), "x"),
        role= KeyRole.OPERATOR
    )
    val keySpecY = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("Y"), "y"),
        topRightAction = Action.StoreAsVariable(ActionLabel.Text("→y"), ActionLabel.Text("→y"), "x"),
        role = KeyRole.OPERATOR
    )
    val keySpecZ = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText(ActionLabel.Text("Z"), "z"),
        topRightAction = Action.StoreAsVariable(ActionLabel.Text("→z"), ActionLabel.Text("→z"), "z"),
        role = KeyRole.OPERATOR
    )
    val keySpecExp = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.operator(ActionLabel.Text("E"), "E"),
        role = KeyRole.OPERATOR
    )
    val keySpecConversion = KeySpec.CornerDragKeySpec(centerAction = Action.InsertText(ActionLabel.Icon(Icons.AutoMirrored.Filled.ArrowRightAlt, null), "→"), role = KeyRole.OPERATOR)

    val keySpecLeft = KeySpec.CornerDragKeySpec(
        centerAction = Action.MoveCursor(label = ActionLabel.Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, "Move cursor left"), chars = -1),
        topRightAction = Action.MoveCursor(label = null, popupLabel = ActionLabel.Icon(Icons.AutoMirrored.Filled.ArrowLeft, "Move cursor to start"), chars = MIN_VALUE),
        role = KeyRole.OPERATOR
    )
    val keySpecRight = KeySpec.CornerDragKeySpec(
        centerAction = Action.MoveCursor(label = ActionLabel.Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Move cursor right"), chars = 1),
        topRightAction = Action.MoveCursor(label = null, popupLabel = ActionLabel.Icon(Icons.AutoMirrored.Filled.ArrowRight, "Move cursor to end"), chars = MAX_VALUE),
        role = KeyRole.SYSTEM
    )
    val keySpecUndo = KeySpec.CornerDragKeySpec(
        centerAction = Action.Undo(label = ActionLabel.Icon(Icons.AutoMirrored.Default.Undo, "Undo")),
        role = KeyRole.SYSTEM
    )
    val keySpecRedo = KeySpec.CornerDragKeySpec(
        centerAction = Action.Redo(label = ActionLabel.Icon(Icons.AutoMirrored.Default.Redo, "Redo")),
        role = KeyRole.SYSTEM
    )

    //    val keySin = Key.CornerDragKey(
//        centerAction = KeyAction.InsertText(label = KeyLabel.Text("sin"), preCursorText = "sin(", postCursorText = ")"),
//        topRightAction = KeyAction.InsertText(label = KeyLabel.Text("sin⁻¹"), preCursorText = "arcsin(", postCursorText = ")"),
//        role = KeyRole.OPERATOR
//    )
    val keySpecSin = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.function(ActionLabel.Text("sin"), "sin"),
        topRightAction = Action.InsertText.function(label = ActionLabel.Text(superscriptSymbol("sin", "-1")), "asin"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("sinh"), preCursorText = "sinh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("sinh", "-1")), preCursorText = "asinh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keySpecCos = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.function(label = ActionLabel.Text("cos"), "cos"),
        topRightAction = Action.InsertText.function(label = ActionLabel.Text(superscriptSymbol("cos", "-1")), "acos"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("cosh"), preCursorText = "cosh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("cosh", "-1")), preCursorText = "acosh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keySpecTan = KeySpec.CornerDragKeySpec(
        centerAction = Action.InsertText.function(label = ActionLabel.Text("tan"), "tan"),
        topRightAction = Action.InsertText.function(label = ActionLabel.Text(superscriptSymbol("tan", "-1")), "atan"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("tanh"), preCursorText = "tanh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("tanh", "-1")), preCursorText = "atanh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )

    val keySpecSiLength = KeySpec.SelectorKeySpec(
        listOf(
            Action.InsertText.operator(ActionLabel.Text("nm"), "nm "),
            Action.InsertText.operator(ActionLabel.Text("um"), "um "),
            Action.InsertText.operator(ActionLabel.Text("mm"), "mm "),
            Action.InsertText.operator(ActionLabel.Text("cm"), "cm "),
            Action.InsertText.operator(ActionLabel.Text("m"), "m "),
            Action.InsertText.operator(ActionLabel.Text("km"), "km "),
        ),
        4,
        role= KeyRole.OPERATOR
    )

    val keySpecImperialLength = KeySpec.SelectorKeySpec(
        listOf(
            Action.InsertText.operator(ActionLabel.Text("thou"), "thou "),
            Action.InsertText.operator(ActionLabel.Text("inch"), "in "),
            Action.InsertText.operator(ActionLabel.Text("foot"), "ft "),
            Action.InsertText.operator(ActionLabel.Text("yard"), "yd "),
            Action.InsertText.operator(ActionLabel.Text("mile"), "mile "),
        ),
        1,
        role= KeyRole.OPERATOR
    )

    val keySpecImperialWeight = KeySpec.SelectorKeySpec(
        listOf(
            Action.InsertText.operator(ActionLabel.Text("grain"), "gr "),
            Action.InsertText.operator(ActionLabel.Text("ounce"), "oz "),
            Action.InsertText.operator(ActionLabel.Text("pound"), "lb "),
            Action.InsertText.operator(ActionLabel.Text("stone"), "stone "),
        ),
        2,
        role= KeyRole.OPERATOR
    )

    val keySpecSiWeight = KeySpec.SelectorKeySpec(
        listOf(
            Action.InsertText.operator(ActionLabel.Text("pg"), "pg "),
            Action.InsertText.operator(ActionLabel.Text("ng"), "ng "),
            Action.InsertText.operator(ActionLabel.Text("µg"), "µg "),
            Action.InsertText.operator(ActionLabel.Text("mg"), "mg "),
            Action.InsertText.operator(ActionLabel.Text("g"), "g "),
            Action.InsertText.operator(ActionLabel.Text("kg"), "kg "),
            Action.InsertText.operator(ActionLabel.Text("t"), "t "),
        ),
        5,
        role= KeyRole.OPERATOR
    )
}
