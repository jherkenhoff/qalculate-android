package com.jherkenhoff.qalculate.model

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em

private fun superscriptSymbol(base: String, superscript: String) : AnnotatedString {
    return buildAnnotatedString {
        append(base)
        withStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 0.6.em)) {
            append(superscript)
        }
    }
}

object KeyLibrary {
    val NUMBER_0 = CalcKey.Default(
        clickAction = CalcAction.InsertText("0"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "0"), "⁰"),
        role = KeyRole.NUMBER
    )
    val NUMBER_1 = CalcKey.Default(
        clickAction = CalcAction.InsertText("1"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "1"), "¹"),
        role = KeyRole.NUMBER
    )
    val NUMBER_2 = CalcKey.Default(
        clickAction = CalcAction.InsertText("2"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "2"), "²"),
        role = KeyRole.NUMBER
    )
    val NUMBER_3 = CalcKey.Default(
        clickAction = CalcAction.InsertText("3"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "3"), "³"),
        role = KeyRole.NUMBER
    )
    val NUMBER_4 = CalcKey.Default(
        clickAction = CalcAction.InsertText("4"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "4"), "⁴"),
        role = KeyRole.NUMBER
    )
    val NUMBER_5 = CalcKey.Default(
        clickAction = CalcAction.InsertText("5"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "5"), "⁵"),
        role = KeyRole.NUMBER
    )
    val NUMBER_6 = CalcKey.Default(
        clickAction = CalcAction.InsertText("6"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "6"), "⁶"),
        role = KeyRole.NUMBER
    )
    val NUMBER_7 = CalcKey.Default(
        clickAction = CalcAction.InsertText("7"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "7"), "⁷"),
        role = KeyRole.NUMBER
    )
    val NUMBER_8 = CalcKey.Default(
        clickAction = CalcAction.InsertText("8"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "8"), "⁸"),
        role = KeyRole.NUMBER
    )
    val NUMBER_9 = CalcKey.Default(
        clickAction = CalcAction.InsertText("9"),
        longClickAction = CalcAction.InsertText(superscriptSymbol("x", "9"), "⁹"),
        role = KeyRole.NUMBER
    )

    val FUNCTION_LN = CalcKey.Default(
        clickAction = CalcAction.InsertText.function("ln"),
        longClickAction = CalcAction.InsertText.function("ln"),
        role = KeyRole.OPERATOR
    )

    val BRACKET_OPEN = CalcKey.Default(
        clickAction = CalcAction.InsertText("("),
        longClickAction = CalcAction.InsertText("["),
        role = KeyRole.OPERATOR
    )
    val BRACKET_CLOSE = CalcKey.Default(
        clickAction = CalcAction.InsertText(")"),
        longClickAction = CalcAction.InsertText("]"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_PLUS = CalcKey.Default(
        clickAction = CalcAction.InsertText.operator("+"),
        role = KeyRole.OPERATOR
    )

    val OPERATOR_MINUS = CalcKey.Default(
        clickAction = CalcAction.InsertText.operator("-"),
        role = KeyRole.OPERATOR
    )

    val OPERATOR_MULTIPLY = CalcKey.Default(
        clickAction = CalcAction.InsertMultiplicationSymbol
    )

    val OPERATOR_DIVISION = CalcKey.Default(
        clickAction = CalcAction.InsertDivisionSymbol
    )

    val NUMBER_DECIMAL = CalcKey.Default(
        clickAction = CalcAction.InsertDecimalSymbol,
        longClickAction = CalcAction.InsertText(label = "␣", " "),
    )

    val OPERATOR_POWER = CalcKey.Default(
        clickAction = CalcAction.InsertText.operator(superscriptSymbol("x", "y"), "^"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_SQRT = CalcKey.Default(
        clickAction = CalcAction.InsertText.function("√", "sqrt"),
        role = KeyRole.OPERATOR
    )

    val OPERATOR_PLUS_MINUS = CalcKey.Default(
        clickAction = CalcAction.InsertText.operator("±"),
        role = KeyRole.OPERATOR
    )
    val calcKeyUnderscore = CalcKey.Default(
        clickAction = CalcAction.InsertText("_"),
        longClickAction = CalcAction.InsertText(";"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_EQUAL = CalcKey.Default(
        clickAction = CalcAction.InsertText("="),
        longClickAction = CalcAction.InsertText(","),
        role = KeyRole.OPERATOR
    )
    val NUMBER_PI = CalcKey.Default(
        clickAction = CalcAction.InsertText("π"),
        longClickAction = CalcAction.InsertText("e"),
        role = KeyRole.OPERATOR
    )
    val RETURN = CalcKey.Default(
        clickAction = CalcAction.SubmitCalculation,
        longClickAction = CalcAction.InsertText("ans"),
        role = KeyRole.SYSTEM
    )
    val BACKSPACE = CalcKey.Default(clickAction = CalcAction.DeleteChars(-1), role = KeyRole.SYSTEM)
    val CLEAR_ALL = CalcKey.Default(clickAction = CalcAction.ClearAll, role = KeyRole.SYSTEM)

    val FUNCTION_INTEGRAL = CalcKey.Default(clickAction = CalcAction.InsertText.function("∫", "integral"), role = KeyRole.OPERATOR)
    val FUNCTION_DIFFERENTIAL = CalcKey.Default(clickAction = CalcAction.InsertText.function("dx", "diff"), role = KeyRole.OPERATOR)
    val FUNCTION_SUM = CalcKey.Default(
        clickAction = CalcAction.InsertText.function("Σ", "sum"),
        longClickAction = CalcAction.InsertText.function("Π", "product"),
        role = KeyRole.OPERATOR
    )
    val NUMBER_INFINITY = CalcKey.Default(
        clickAction = CalcAction.InsertText("∞"),
        longClickAction = CalcAction.InsertText("!"),
        role = KeyRole.OPERATOR
    )
    val NUMBER_IMAGINARY = CalcKey.Default(
        clickAction = CalcAction.InsertText("i"),
        longClickAction = CalcAction.InsertText("∠"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_COMPLEX = CalcKey.Selector(
        listOf(
            CalcAction.InsertText.function("Abs.", "abs"),
            CalcAction.InsertText.function("Arg.", "arg"),
            CalcAction.InsertText.function("Real", "re"),
            CalcAction.InsertText.function("Imag.", "im"),
            CalcAction.InsertText.function("Conj.", "conj")
        ),
        2,
        role = KeyRole.OPERATOR
    )
    val NUMBER_PERCENT = CalcKey.Default(
        clickAction = CalcAction.InsertText.operator("%"),
        longClickAction = CalcAction.InsertText.operator("±"),
        role = KeyRole.OPERATOR
    )
    val VARIABLE_X = CalcKey.Default(
        clickAction = CalcAction.InsertText("X", "x"),
        longClickAction = CalcAction.StoreAsVariable("x"),
        role= KeyRole.OPERATOR
    )
    val VARIABLE_Y = CalcKey.Default(
        clickAction = CalcAction.InsertText("Y", "y"),
        longClickAction = CalcAction.StoreAsVariable("y"),
        role = KeyRole.OPERATOR
    )
    val VARIABLE_Z = CalcKey.Default(
        clickAction = CalcAction.InsertText("Z", "z"),
        longClickAction = CalcAction.StoreAsVariable("z"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_E = CalcKey.Default(
        clickAction = CalcAction.InsertText.operator("E"),
        role = KeyRole.OPERATOR
    )

    val FUNCTION_SIN = CalcKey.Default(
        clickAction = CalcAction.InsertText.function("sin"),
        longClickAction = CalcAction.InsertText.function(superscriptSymbol("sin", "-1"), "asin"),
        role = KeyRole.OPERATOR
    )
    val FUNCTION_COS = CalcKey.Default(
        clickAction = CalcAction.InsertText.function("cos"),
        longClickAction = CalcAction.InsertText.function(superscriptSymbol("cos", "-1"), "acos"),
        role = KeyRole.OPERATOR
    )
    val FUNCTION_TAN = CalcKey.Default(
        clickAction = CalcAction.InsertText.function("tan"),
        longClickAction = CalcAction.InsertText.function(superscriptSymbol("tan", "-1"), "atan"),
        role = KeyRole.OPERATOR
    )

    val calcKeySiLength = CalcKey.Selector(
        listOf(
            CalcAction.InsertText.operator("nm "),
            CalcAction.InsertText.operator("um "),
            CalcAction.InsertText.operator("mm "),
            CalcAction.InsertText.operator("cm "),
            CalcAction.InsertText.operator("m "),
            CalcAction.InsertText.operator("km "),
        ),
        4,
        role= KeyRole.OPERATOR
    )

    val calcKeyImperialLength = CalcKey.Selector(
        listOf(
            CalcAction.InsertText.operator("thou "),
            CalcAction.InsertText.operator("in "),
            CalcAction.InsertText.operator("ft "),
            CalcAction.InsertText.operator("yd "),
            CalcAction.InsertText.operator("mile "),
        ),
        1,
        role= KeyRole.OPERATOR
    )

    val calcKeyImperialWeight = CalcKey.Selector(
        listOf(
            CalcAction.InsertText.operator("gr "),
            CalcAction.InsertText.operator("oz "),
            CalcAction.InsertText.operator("lb "),
            CalcAction.InsertText.operator("stone "),
        ),
        2,
        role= KeyRole.OPERATOR
    )

    val SI_PREFIX = CalcKey.Selector(
        listOf(
            CalcAction.InsertText.operator("G"),
            CalcAction.InsertText.operator("M"),
            CalcAction.InsertText.operator("k"),
            CalcAction.InsertText.operator("m"),
            CalcAction.InsertText.operator("µ"),
            CalcAction.InsertText.operator("n"),
            CalcAction.InsertText.operator("p"),
        ),
        2,
        role= KeyRole.OPERATOR
    )

    val SI_UNITS = CalcKey.Selector(
        listOf(
            CalcAction.InsertText.operator("A"),
            CalcAction.InsertText.operator("g"),
            CalcAction.InsertText.operator("J"),
            CalcAction.InsertText.operator("K"),
            CalcAction.InsertText.operator("L"),
            CalcAction.InsertText.operator("m"),
            CalcAction.InsertText.operator("N"),
            CalcAction.InsertText.operator("Ω"),
            CalcAction.InsertText.operator("Pa"),
            CalcAction.InsertText.operator("s"),
            CalcAction.InsertText.operator("V"),
            CalcAction.InsertText.operator("W"),
        ),
        5,
        role= KeyRole.OPERATOR
    )
}
