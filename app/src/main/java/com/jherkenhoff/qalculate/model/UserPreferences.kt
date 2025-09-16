package com.jherkenhoff.qalculate.model

data class UserPreferences(
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val angleUnit: AngleUnit = AngleUnit.RADIANS,
    val multiplicationSign: MultiplicationSign = MultiplicationSign.DOT,
    val divisionSign: DivisionSign = DivisionSign.SLASH,
    val abbreviateNames: Boolean = true,
    val negativeExponents: Boolean = true,
    val spaciousOutput: Boolean = true
) {
    companion object {
        val Default = UserPreferences()
    }

    enum class DecimalSeparator {DOT, COMMA}
    enum class AngleUnit {DEGREES, RADIANS, GRADIANS}
    enum class MultiplicationSign {DOT, X, ASTERISK, ALTDOT}
    enum class DivisionSign {DIVISION, SLASH, DIVISION_SLASH}

    fun getDivisionSignString(): String {
        return when (this.divisionSign) {
            UserPreferences.DivisionSign.DIVISION -> "÷"
            UserPreferences.DivisionSign.DIVISION_SLASH -> "∕"
            UserPreferences.DivisionSign.SLASH -> "/"
        }
    }

    fun getMultiplicationSignString(): String {
        return when (this.multiplicationSign) {
            UserPreferences.MultiplicationSign.DOT -> "·"
            UserPreferences.MultiplicationSign.X -> "×"
            UserPreferences.MultiplicationSign.ASTERISK -> "*"
            UserPreferences.MultiplicationSign.ALTDOT -> "."
        }
    }
}