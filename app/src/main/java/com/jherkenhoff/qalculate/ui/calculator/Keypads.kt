package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Science
import com.jherkenhoff.qalculate.model.KeyLibrary
import com.jherkenhoff.qalculate.model.KeyPositionSpec
import com.jherkenhoff.qalculate.model.KeypadSection
import com.jherkenhoff.qalculate.model.KeypadDefinition

val basicKeypadSectionExtended = KeypadSection(
    rows = 2,
    cols = 6,
    keys = listOf(
        Pair(KeyPositionSpec(0, 0), KeyLibrary.FUNCTION_SIN),
        Pair(KeyPositionSpec(0, 1), KeyLibrary.FUNCTION_COS),
        Pair(KeyPositionSpec(0, 2), KeyLibrary.FUNCTION_TAN),
        Pair(KeyPositionSpec(0, 3), KeyLibrary.FUNCTION_LN),
        Pair(KeyPositionSpec(0, 4), KeyLibrary.NUMBER_PI),
        Pair(KeyPositionSpec(0, 5), KeyLibrary.NUMBER_PERCENT),

        Pair(KeyPositionSpec(1, 0), KeyLibrary.VARIABLE_SELECTOR),
        Pair(KeyPositionSpec(1, 1), KeyLibrary.OPERATOR_EQUAL),
        Pair(KeyPositionSpec(1, 2), KeyLibrary.BRACKET_OPEN),
        Pair(KeyPositionSpec(1, 3), KeyLibrary.BRACKET_CLOSE),
        Pair(KeyPositionSpec(1, 4), KeyLibrary.OPERATOR_POWER),
        Pair(KeyPositionSpec(1, 5), KeyLibrary.OPERATOR_SQRT),
    ),
    aspectRatio = 0.6f
)


val basicKeypadSectionNumpad = KeypadSection(
    rows = 4,
    cols = 5,
    keys = listOf(
        Pair(KeyPositionSpec(0, 0), KeyLibrary.NUMBER_7),
        Pair(KeyPositionSpec(0, 1), KeyLibrary.NUMBER_8),
        Pair(KeyPositionSpec(0, 2), KeyLibrary.NUMBER_9),
        Pair(KeyPositionSpec(0, 3), KeyLibrary.BACKSPACE),
        Pair(KeyPositionSpec(0, 4), KeyLibrary.CLEAR_ALL),

        Pair(KeyPositionSpec(1, 0), KeyLibrary.NUMBER_4),
        Pair(KeyPositionSpec(1, 1), KeyLibrary.NUMBER_5),
        Pair(KeyPositionSpec(1, 2), KeyLibrary.NUMBER_6),
        Pair(KeyPositionSpec(1, 3), KeyLibrary.OPERATOR_MULTIPLY),
        Pair(KeyPositionSpec(1, 4), KeyLibrary.OPERATOR_DIVISION),

        Pair(KeyPositionSpec(2, 0), KeyLibrary.NUMBER_1),
        Pair(KeyPositionSpec(2, 1), KeyLibrary.NUMBER_2),
        Pair(KeyPositionSpec(2, 2), KeyLibrary.NUMBER_3),
        Pair(KeyPositionSpec(2, 3), KeyLibrary.OPERATOR_PLUS),
        Pair(KeyPositionSpec(2, 4), KeyLibrary.OPERATOR_MINUS),

        Pair(KeyPositionSpec(3, 0), KeyLibrary.NUMBER_0),
        Pair(KeyPositionSpec(3, 1), KeyLibrary.NUMBER_DECIMAL),
        Pair(KeyPositionSpec(3, 2), KeyLibrary.OPERATOR_E),
        Pair(KeyPositionSpec(3, 3, 1, 2), KeyLibrary.RETURN)
    ),
    aspectRatio = 0.8f
)

val primaryKeypadSection = KeypadSection(
    rows = 4,
    cols = 7,
    keys = listOf(
        Pair(KeyPositionSpec(0, 0), KeyLibrary.NUMBER_PERCENT),
        Pair(KeyPositionSpec(0, 1), KeyLibrary.NUMBER_PI),
        Pair(KeyPositionSpec(0, 2), KeyLibrary.NUMBER_7),
        Pair(KeyPositionSpec(0, 3), KeyLibrary.NUMBER_8),
        Pair(KeyPositionSpec(0, 4), KeyLibrary.NUMBER_9),
        Pair(KeyPositionSpec(0, 5), KeyLibrary.BACKSPACE),
        Pair(KeyPositionSpec(0, 6), KeyLibrary.CLEAR_ALL),

        Pair(KeyPositionSpec(1, 0), KeyLibrary.OPERATOR_SQRT),
        Pair(KeyPositionSpec(1, 1), KeyLibrary.OPERATOR_POWER),
        Pair(KeyPositionSpec(1, 2), KeyLibrary.NUMBER_4),
        Pair(KeyPositionSpec(1, 3), KeyLibrary.NUMBER_5),
        Pair(KeyPositionSpec(1, 4), KeyLibrary.NUMBER_6),
        Pair(KeyPositionSpec(1, 5), KeyLibrary.OPERATOR_MULTIPLY),
        Pair(KeyPositionSpec(1, 6), KeyLibrary.OPERATOR_DIVISION),

        Pair(KeyPositionSpec(2, 0), KeyLibrary.BRACKET_OPEN),
        Pair(KeyPositionSpec(2, 1), KeyLibrary.BRACKET_CLOSE),
        Pair(KeyPositionSpec(2, 2), KeyLibrary.NUMBER_1),
        Pair(KeyPositionSpec(2, 3), KeyLibrary.NUMBER_2),
        Pair(KeyPositionSpec(2, 4), KeyLibrary.NUMBER_3),
        Pair(KeyPositionSpec(2, 5), KeyLibrary.OPERATOR_PLUS),
        Pair(KeyPositionSpec(2, 6), KeyLibrary.OPERATOR_MINUS),

        Pair(KeyPositionSpec(3, 0), KeyLibrary.calcKeyUnderscore),
        Pair(KeyPositionSpec(3, 1), KeyLibrary.OPERATOR_EQUAL),
        Pair(KeyPositionSpec(3, 2), KeyLibrary.NUMBER_0),
        Pair(KeyPositionSpec(3, 3), KeyLibrary.NUMBER_DECIMAL),
        Pair(KeyPositionSpec(3, 4), KeyLibrary.OPERATOR_E),
        Pair(KeyPositionSpec(3, 5, 1, 2), KeyLibrary.RETURN)
    ),
    aspectRatio = 0.6f
)

val secondaryKeypadSection =
    KeypadSection(
        rows = 3,
        cols = 5,
        keys = listOf(
            Pair(KeyPositionSpec(0, 0), KeyLibrary.VARIABLE_X),
            Pair(KeyPositionSpec(0, 1), KeyLibrary.VARIABLE_Y),
            Pair(KeyPositionSpec(0, 2), KeyLibrary.VARIABLE_Z),
            Pair(KeyPositionSpec(0, 3), KeyLibrary.SI_PREFIX),
            Pair(KeyPositionSpec(0, 4), KeyLibrary.SI_UNITS),

            Pair(KeyPositionSpec(1, 0), KeyLibrary.FUNCTION_INTEGRAL),
            Pair(KeyPositionSpec(1, 1), KeyLibrary.FUNCTION_DIFFERENTIAL),
            Pair(KeyPositionSpec(1, 2), KeyLibrary.FUNCTION_SUM),
            Pair(KeyPositionSpec(1, 3), KeyLibrary.NUMBER_IMAGINARY),
            Pair(KeyPositionSpec(1, 4), KeyLibrary.OPERATOR_COMPLEX),

            Pair(KeyPositionSpec(2, 0), KeyLibrary.FUNCTION_SIN),
            Pair(KeyPositionSpec(2, 1), KeyLibrary.FUNCTION_COS),
            Pair(KeyPositionSpec(2, 2), KeyLibrary.FUNCTION_TAN),
            Pair(KeyPositionSpec(2, 3), KeyLibrary.FUNCTION_LN),
            Pair(KeyPositionSpec(2, 4), KeyLibrary.NUMBER_INFINITY)
        ),
        aspectRatio = 0.5f
    )

val keypads = listOf(
    KeypadDefinition(
        name = "Basic",
        icon = Icons.Default.Calculate,
        sections = listOf(basicKeypadSectionExtended, basicKeypadSectionNumpad),
        imeEnabled = false
    ),
    KeypadDefinition(
        name = "Advanced",
        icon = Icons.Default.Science,
        sections = listOf(
            secondaryKeypadSection.copy(aspectRatio = 0.5f),
            primaryKeypadSection.copy(aspectRatio = 0.9f)
        ),
        imeEnabled = false
    ),
    KeypadDefinition(
        name = "Keyboard",
        icon = Icons.Default.Keyboard,
        sections = listOf(
            primaryKeypadSection.copy(aspectRatio = 0.6f)
        ),
        imeEnabled = true
    ),
)