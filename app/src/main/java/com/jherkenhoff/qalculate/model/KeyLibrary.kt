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
    val keySpec0 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("0"), "0"),
        longClickAction = Action.InsertText.operator(ActionLabel.Text(superscriptSymbol("x", "0")), "⁰"),
        role = KeyRole.NUMBER
    )
    val keySpec1 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("1"), "1"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "1")), "¹"),
        role = KeyRole.NUMBER
    )

    val keySpec2 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("2"), "2"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "2")), "²"),
        role = KeyRole.NUMBER
    )

    val keySpec3 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("3"), "3"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "3")), "³"),
        role = KeyRole.NUMBER
    )

    val keySpec4 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("4"), "4"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "4")), "⁴"),
        role = KeyRole.NUMBER
    )

    val keySpec5 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("5"), "5"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "5")), "⁵"),
        role = KeyRole.NUMBER
    )

    val keySpec6 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("6"), "6"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "6")), "⁶"),
        role = KeyRole.NUMBER
    )

    val keySpec7 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("7"), "7"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "7")), "⁷"),
        role = KeyRole.NUMBER
    )
    val keySpec8 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("8"), "8"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "8")), "⁸"),
        role = KeyRole.NUMBER
    )

    val keySpec9 = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("9"), "9"),
        longClickAction = Action.InsertText.operator( ActionLabel.Text(superscriptSymbol("x", "9")), "⁹"),
        role = KeyRole.NUMBER
    )

    val keySpecLn = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.function(ActionLabel.Text("ln"), "ln"),
        longClickAction = Action.InsertText.function(ActionLabel.Text("log"), "log10"),
        role = KeyRole.OPERATOR
    )

    val keySpecBracketOpen = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("("), "("),
        longClickAction = Action.InsertText(ActionLabel.Text("["), "["),
        role = KeyRole.OPERATOR
    )
    val keySpecBracketClose = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text(")"), ")"),
        longClickAction = Action.InsertText(ActionLabel.Text("]"), "]"),
        role = KeyRole.OPERATOR
    )
    val keySpecPlus = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.operator(ActionLabel.Text("+"), "+"),
        role = KeyRole.OPERATOR
    )
    val keySpecMinus = KeySpec.DefaultKeySpec(clickAction = Action.InsertText.operator(ActionLabel.Text("-"), "-"), role = KeyRole.OPERATOR)

    val keySpecPower = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.operator(ActionLabel.Text(superscriptSymbol("x", "y")), "^"),
        role = KeyRole.OPERATOR
    )
    val keySpecSqrt = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.function(ActionLabel.Text("√"), "sqrt"),
        role = KeyRole.OPERATOR
    )

    val keySpecPlusMinus = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("±"), "±"),
        role = KeyRole.OPERATOR
    )
    val keySpecUnderscore = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("_"), "_"),
        longClickAction = Action.InsertText(ActionLabel.Text(";"), ";"),
        role = KeyRole.OPERATOR
    )
    val keySpecEqual = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("="), "="),
        longClickAction = Action.InsertText(ActionLabel.Text(","), ","),
        role = KeyRole.OPERATOR
    )
    val keySpecPi = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("π"), "π"),
        longClickAction = Action.InsertText(ActionLabel.Text("e"), "e"),
        role = KeyRole.OPERATOR
    )
    val keySpecFactorial = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("∞"), "∞"),
        longClickAction = Action.InsertText.operator(ActionLabel.Text("!"), "!"),
        role = KeyRole.OPERATOR
    )
    val keySpecEuler = KeySpec.DefaultKeySpec(clickAction = Action.InsertText(ActionLabel.Text("e"), "e"), role = KeyRole.OPERATOR)
    val keySpecReturn = KeySpec.DefaultKeySpec(
        clickAction = Action.Return(ActionLabel.Icon(Icons.AutoMirrored.Filled.KeyboardReturn, "Return")),
        longClickAction = Action.InsertText(ActionLabel.Text("ans"), "ans"),
        role = KeyRole.SYSTEM
    )
    val keySpecBackspace = KeySpec.DefaultKeySpec(clickAction = Action.Backspace(ActionLabel.Icon(Icons.AutoMirrored.Filled.KeyboardBackspace, "Backspace")), role = KeyRole.SYSTEM)
    val keySpecClearAll = KeySpec.DefaultKeySpec(clickAction = Action.ClearAll(ActionLabel.Text("AC")), role = KeyRole.SYSTEM)

    val keySpecIntegral = KeySpec.DefaultKeySpec(clickAction = Action.InsertText.function(ActionLabel.Text("∫"), "integral"), role = KeyRole.OPERATOR)
    val keySpecDifferential = KeySpec.DefaultKeySpec(clickAction = Action.InsertText.function(ActionLabel.Text("dx"), "diff"), role = KeyRole.OPERATOR)
    val keySpecSum = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.function(ActionLabel.Text("Σ"), "sum"),
        longClickAction = Action.InsertText.function(ActionLabel.Text("Π"), "product"),
        role = KeyRole.OPERATOR
    )
    val keySpecInfinity = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("∞"), "∞"),
        longClickAction = Action.InsertText(ActionLabel.Text("!"), "!"),
        role = KeyRole.OPERATOR
    )
    val keySpecImaginary = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("i"), "i"),
        longClickAction = Action.InsertText(ActionLabel.Text("∠"), "∠"),
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
    val keySpecPercent = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.operator(ActionLabel.Text("%"), "%"),
        longClickAction = Action.InsertText.operator(ActionLabel.Text("±"), "±"),
        role = KeyRole.OPERATOR
    )
    val keySpecX = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("X"), "x"),
        longClickAction = Action.StoreAsVariable(ActionLabel.Text("→x"), ActionLabel.Text("→x"), "x"),
        role= KeyRole.OPERATOR
    )
    val keySpecY = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("Y"), "y"),
        longClickAction = Action.StoreAsVariable(ActionLabel.Text("→y"), ActionLabel.Text("→y"), "x"),
        role = KeyRole.OPERATOR
    )
    val keySpecZ = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText(ActionLabel.Text("Z"), "z"),
        longClickAction = Action.StoreAsVariable(ActionLabel.Text("→z"), ActionLabel.Text("→z"), "z"),
        role = KeyRole.OPERATOR
    )
    val keySpecExp = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.operator(ActionLabel.Text("E"), "E"),
        role = KeyRole.OPERATOR
    )

    //    val keySin = Key.CornerDragKey(
//        clickAction = KeyAction.InsertText(label = KeyLabel.Text("sin"), preCursorText = "sin(", postCursorText = ")"),
//        longClickAction = KeyAction.InsertText(label = KeyLabel.Text("sin⁻¹"), preCursorText = "arcsin(", postCursorText = ")"),
//        role = KeyRole.OPERATOR
//    )
    val keySpecSin = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.function(ActionLabel.Text("sin"), "sin"),
        longClickAction = Action.InsertText.function(label = ActionLabel.Text(superscriptSymbol("sin", "-1")), "asin"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("sinh"), preCursorText = "sinh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("sinh", "-1")), preCursorText = "asinh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keySpecCos = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.function(label = ActionLabel.Text("cos"), "cos"),
        longClickAction = Action.InsertText.function(label = ActionLabel.Text(superscriptSymbol("cos", "-1")), "acos"),
//        bottomLeftAction = KeyAction.InsertText(label = KeyLabel.Text("cosh"), preCursorText = "cosh(", postCursorText = ")"),
//        bottomRightAction = KeyAction.InsertText(label = KeyLabel.Text(superscriptSymbol("cosh", "-1")), preCursorText = "acosh(", postCursorText = ")"),
        role = KeyRole.OPERATOR
    )
    val keySpecTan = KeySpec.DefaultKeySpec(
        clickAction = Action.InsertText.function(label = ActionLabel.Text("tan"), "tan"),
        longClickAction = Action.InsertText.function(label = ActionLabel.Text(superscriptSymbol("tan", "-1")), "atan"),
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

    val keySpecSiPrefix = KeySpec.SelectorKeySpec(
        listOf(
            Action.InsertText.operator(ActionLabel.Text("giga"), "G"),
            Action.InsertText.operator(ActionLabel.Text("mega"), "M"),
            Action.InsertText.operator(ActionLabel.Text("kilo"), "k"),
            Action.InsertText.operator(ActionLabel.Text("milli"), "m"),
            Action.InsertText.operator(ActionLabel.Text("micro"), "µ"),
            Action.InsertText.operator(ActionLabel.Text("nano"), "n"),
            Action.InsertText.operator(ActionLabel.Text("pico"), "p"),
        ),
        2,
        role= KeyRole.OPERATOR
    )

    val keySpecBasicUnits = KeySpec.SelectorKeySpec(
        listOf(
            Action.InsertText.operator(ActionLabel.Text("Ampere"), "A"),
            Action.InsertText.operator(ActionLabel.Text("Gram"), "g"),
            Action.InsertText.operator(ActionLabel.Text("Joule"), "J"),
            Action.InsertText.operator(ActionLabel.Text("Kelvin"), "K"),
            Action.InsertText.operator(ActionLabel.Text("Liter"), "L"),
            Action.InsertText.operator(ActionLabel.Text("Meter"), "m"),
            Action.InsertText.operator(ActionLabel.Text("Newton"), "N"),
            Action.InsertText.operator(ActionLabel.Text("Ohm"), "Ω"),
            Action.InsertText.operator(ActionLabel.Text("Pascal"), "Pa"),
            Action.InsertText.operator(ActionLabel.Text("Second"), "s"),
            Action.InsertText.operator(ActionLabel.Text("Volt"), "V"),
            Action.InsertText.operator(ActionLabel.Text("Watt"), "W"),
        ),
        5,
        role= KeyRole.OPERATOR
    )
}
