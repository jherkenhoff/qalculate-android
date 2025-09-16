package com.jherkenhoff.qalculate.model

data class UserPreferences(
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val angleUnit: AngleUnit = AngleUnit.RADIANS,
    val multiplicationSign: MultiplicationSign = MultiplicationSign.DOT,
    val divisionSign: DivisionSign = DivisionSign.DIVISION
) {
    enum class DecimalSeparator {DOT, COMMA}
    enum class AngleUnit {DEGREES, RADIANS, GRADIANS}
    enum class MultiplicationSign {DOT, X, ASTERISK, ALTDOT}
    enum class DivisionSign {DIVISION, SLASH, DIVISION_SLASH}
}