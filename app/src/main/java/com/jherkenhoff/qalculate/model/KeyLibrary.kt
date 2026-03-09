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
        clickAction = CalculatorAction.InsertText("0"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "0"), "⁰"),
        role = KeyRole.NUMBER
    )
    val NUMBER_1 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("1"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "1"), "¹"),
        role = KeyRole.NUMBER
    )
    val NUMBER_2 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("2"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "2"), "²"),
        role = KeyRole.NUMBER
    )
    val NUMBER_3 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("3"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "3"), "³"),
        role = KeyRole.NUMBER
    )
    val NUMBER_4 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("4"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "4"), "⁴"),
        role = KeyRole.NUMBER
    )
    val NUMBER_5 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("5"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "5"), "⁵"),
        role = KeyRole.NUMBER
    )
    val NUMBER_6 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("6"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "6"), "⁶"),
        role = KeyRole.NUMBER
    )
    val NUMBER_7 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("7"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "7"), "⁷"),
        role = KeyRole.NUMBER
    )
    val NUMBER_8 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("8"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "8"), "⁸"),
        role = KeyRole.NUMBER
    )
    val NUMBER_9 = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("9"),
        longClickAction = CalculatorAction.InsertText(superscriptSymbol("x", "9"), "⁹"),
        role = KeyRole.NUMBER
    )

    val FUNCTION_LN = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.function("ln"),
        longClickAction = CalculatorAction.InsertText.function("log"),
        role = KeyRole.OPERATOR
    )

    val BRACKET_OPEN = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("("),
        longClickAction = CalculatorAction.InsertText("["),
        role = KeyRole.OPERATOR
    )
    val BRACKET_CLOSE = CalcKey.Default(
        clickAction = CalculatorAction.InsertText(")"),
        longClickAction = CalculatorAction.InsertText("]"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_PLUS = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.operator("+"),
        role = KeyRole.OPERATOR
    )

    val OPERATOR_MINUS = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.operator("-"),
        role = KeyRole.OPERATOR
    )

    val OPERATOR_MULTIPLY = CalcKey.Default(
        clickAction = CalculatorAction.InsertMultiplicationSymbol,
        role = KeyRole.OPERATOR
    )

    val OPERATOR_DIVISION = CalcKey.Default(
        clickAction = CalculatorAction.InsertDivisionSymbol,
        role = KeyRole.OPERATOR
    )

    val NUMBER_DECIMAL = CalcKey.Default(
        clickAction = CalculatorAction.InsertDecimalSymbol,
        longClickAction = CalculatorAction.InsertText(label = "␣", " "),
    )

    val OPERATOR_POWER = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.operator(superscriptSymbol("x", "y"), "^"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_SQRT = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.function("√", "sqrt"),
        role = KeyRole.OPERATOR
    )

    val OPERATOR_PLUS_MINUS = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.operator("±"),
        role = KeyRole.OPERATOR
    )
    val calcKeyUnderscore = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("_"),
        longClickAction = CalculatorAction.InsertText(";"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_EQUAL = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("="),
        longClickAction = CalculatorAction.InsertText(","),
        role = KeyRole.OPERATOR
    )
    val NUMBER_PI = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("π"),
        longClickAction = CalculatorAction.InsertText("e"),
        role = KeyRole.OPERATOR
    )
    val RETURN = CalcKey.Default(
        clickAction = CalculatorAction.SubmitCalculation,
        longClickAction = CalculatorAction.InsertText("ans"),
        role = KeyRole.SYSTEM
    )
    val BACKSPACE = CalcKey.Default(clickAction = CalculatorAction.DeleteChars(-1), role = KeyRole.SYSTEM)
    val CLEAR_ALL = CalcKey.Default(clickAction = CalculatorAction.ClearAll, role = KeyRole.SYSTEM)

    val FUNCTION_INTEGRAL = CalcKey.Default(clickAction = CalculatorAction.InsertText.function("∫", "integral"), role = KeyRole.OPERATOR)
    val FUNCTION_DIFFERENTIAL = CalcKey.Default(clickAction = CalculatorAction.InsertText.function("dx", "diff"), role = KeyRole.OPERATOR)
    val FUNCTION_SUM = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.function("Σ", "sum"),
        longClickAction = CalculatorAction.InsertText.function("Π", "product"),
        role = KeyRole.OPERATOR
    )
    val NUMBER_INFINITY = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("∞"),
        longClickAction = CalculatorAction.InsertText("!"),
        role = KeyRole.OPERATOR
    )
    val NUMBER_IMAGINARY = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("i"),
        longClickAction = CalculatorAction.InsertText("∠"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_COMPLEX = CalcKey.Selector(
        listOf(
            CalculatorAction.InsertText.function("Abs.", "abs"),
            CalculatorAction.InsertText.function("Arg.", "arg"),
            CalculatorAction.InsertText.function("Real", "re"),
            CalculatorAction.InsertText.function("Imag.", "im"),
            CalculatorAction.InsertText.function("Conj.", "conj")
        ),
        2,
        role = KeyRole.OPERATOR
    )
    val NUMBER_PERCENT = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.operator("%"),
        longClickAction = CalculatorAction.InsertText.operator("±"),
        role = KeyRole.OPERATOR
    )
    val VARIABLE_X = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("X", "x"),
        longClickAction = CalculatorAction.StoreAsVariable("x"),
        role= KeyRole.OPERATOR
    )
    val VARIABLE_Y = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("Y", "y"),
        longClickAction = CalculatorAction.StoreAsVariable("y"),
        role = KeyRole.OPERATOR
    )
    val VARIABLE_Z = CalcKey.Default(
        clickAction = CalculatorAction.InsertText("Z", "z"),
        longClickAction = CalculatorAction.StoreAsVariable("z"),
        role = KeyRole.OPERATOR
    )
    val OPERATOR_E = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.operator("E"),
        role = KeyRole.OPERATOR
    )

    val FUNCTION_SIN = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.function("sin"),
        longClickAction = CalculatorAction.InsertText.function(superscriptSymbol("sin", "-1"), "asin"),
        role = KeyRole.OPERATOR
    )
    val FUNCTION_COS = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.function("cos"),
        longClickAction = CalculatorAction.InsertText.function(superscriptSymbol("cos", "-1"), "acos"),
        role = KeyRole.OPERATOR
    )
    val FUNCTION_TAN = CalcKey.Default(
        clickAction = CalculatorAction.InsertText.function("tan"),
        longClickAction = CalculatorAction.InsertText.function(superscriptSymbol("tan", "-1"), "atan"),
        role = KeyRole.OPERATOR
    )

    val calcKeySiLength = CalcKey.Selector(
        listOf(
            CalculatorAction.InsertText.operator("nm "),
            CalculatorAction.InsertText.operator("um "),
            CalculatorAction.InsertText.operator("mm "),
            CalculatorAction.InsertText.operator("cm "),
            CalculatorAction.InsertText.operator("m "),
            CalculatorAction.InsertText.operator("km "),
        ),
        4,
        role= KeyRole.OPERATOR
    )

    val calcKeyImperialLength = CalcKey.Selector(
        listOf(
            CalculatorAction.InsertText.operator("thou "),
            CalculatorAction.InsertText.operator("in "),
            CalculatorAction.InsertText.operator("ft "),
            CalculatorAction.InsertText.operator("yd "),
            CalculatorAction.InsertText.operator("mile "),
        ),
        1,
        role= KeyRole.OPERATOR
    )

    val calcKeyImperialWeight = CalcKey.Selector(
        listOf(
            CalculatorAction.InsertText.operator("gr "),
            CalculatorAction.InsertText.operator("oz "),
            CalculatorAction.InsertText.operator("lb "),
            CalculatorAction.InsertText.operator("stone "),
        ),
        2,
        role= KeyRole.OPERATOR
    )

    val SI_PREFIX = CalcKey.Selector(
        listOf(
            CalculatorAction.InsertText.operator("G"),
            CalculatorAction.InsertText.operator("M"),
            CalculatorAction.InsertText.operator("k"),
            CalculatorAction.InsertText.operator("m"),
            CalculatorAction.InsertText.operator("µ"),
            CalculatorAction.InsertText.operator("n"),
            CalculatorAction.InsertText.operator("p"),
        ),
        2,
        role= KeyRole.OPERATOR
    )

    val SI_UNITS = CalcKey.Selector(
        listOf(
            CalculatorAction.InsertText.operator("A"),
            CalculatorAction.InsertText.operator("g"),
            CalculatorAction.InsertText.operator("J"),
            CalculatorAction.InsertText.operator("K"),
            CalculatorAction.InsertText.operator("L"),
            CalculatorAction.InsertText.operator("m"),
            CalculatorAction.InsertText.operator("N"),
            CalculatorAction.InsertText.operator("Ω"),
            CalculatorAction.InsertText.operator("Pa"),
            CalculatorAction.InsertText.operator("s"),
            CalculatorAction.InsertText.operator("V"),
            CalculatorAction.InsertText.operator("W"),
        ),
        5,
        role= KeyRole.OPERATOR
    )
}
