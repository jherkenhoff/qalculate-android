package com.jherkenhoff.qalculate.model

data class UserPreferences(
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val angleUnit: AngleUnit = AngleUnit.RADIANS,
    val multiplicationSign: MultiplicationSign = MultiplicationSign.DOT,
    val divisionSign: DivisionSign = DivisionSign.SLASH,
    val abbreviateNames: Boolean = true,
    val negativeExponents: Boolean = true,
    val spaciousOutput: Boolean = true,
    val approximationMode: ApproximationMode = ApproximationMode.TRY_EXACT,
    val numericalDisplayMode: NumericalDisplayMode = NumericalDisplayMode.ENGINEERING,
    val numberFractionFormat: NumberFractionFormat = NumberFractionFormat.FRACTION_DECIMAL,
    val useDenominatorPrefix: Boolean = false,
    val placeUnitsSeparately: Boolean = true,
    val preserveFormat: Boolean = true,
    val expDisplay: ExpDisplay = ExpDisplay.POWER_OF_10,
    val intervalDisplay: IntervalDisplay = IntervalDisplay.CONCISE
) {
    companion object {
        val Default = UserPreferences()
    }

    enum class DecimalSeparator {DOT, COMMA}
    enum class AngleUnit {DEGREES, RADIANS, GRADIANS}
    enum class MultiplicationSign {DOT, X, ASTERISK, ALTDOT}
    enum class DivisionSign {DIVISION, SLASH, DIVISION_SLASH}
    enum class ApproximationMode {EXACT, TRY_EXACT, APPROXIMATE}
    enum class NumericalDisplayMode {NORMAL, SCIENTIFIC, ENGINEERING}
    enum class ExpDisplay {POWER_OF_10, LOWERCASE_E, UPPERCASE_E}

    enum class NumberFractionFormat {
        FRACTION_DECIMAL, FRACTION_DECIMAL_EXACT, FRACTION_FRACTIONAL, FRACTION_COMBINED,
        FRACTION_PERCENT, FRACTION_PERMILLE, FRACTION_PERMYRIAD
    }
    enum class IntervalDisplay {CONCISE, INTERVAL, PLUSMINUS, MIDPOINT, RELATIVE, SIGNIFICANT_DIGITS}

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