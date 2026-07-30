package com.jherkenhoff.qalculate.model

import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions

data class UserPreferences(
    val activeKeypadIndex: Int = 0,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val angleUnit: AngleUnit = AngleUnit.RADIANS,
    val multiplicationSign: MultiplicationSign = MultiplicationSign.X,
    val divisionSign: DivisionSign = DivisionSign.DIVISION,
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
    val intervalDisplay: IntervalDisplay = IntervalDisplay.CONCISE,
    val digitGrouping: Boolean = true
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
            DivisionSign.DIVISION -> "÷"
            DivisionSign.DIVISION_SLASH -> "∕"
            DivisionSign.SLASH -> "/"
        }
    }

    fun getMultiplicationSignString(): String {
        return when (this.multiplicationSign) {
            MultiplicationSign.DOT -> "·"
            MultiplicationSign.X -> "×"
            MultiplicationSign.ASTERISK -> "*"
            MultiplicationSign.ALTDOT -> "."
        }
    }

    fun getDecimalSeparatorString(): String {
        return when (this.decimalSeparator) {
            DecimalSeparator.DOT -> "."
            DecimalSeparator.COMMA -> ","
        }
    }

    fun getParseOptions(): ParseOptions {
        val parseOptions = ParseOptions()
        parseOptions.preserve_format = preserveFormat
        parseOptions.angle_unit = when (angleUnit) {
            AngleUnit.RADIANS -> com.jherkenhoff.libqalculate.AngleUnit.ANGLE_UNIT_RADIANS
            AngleUnit.DEGREES -> com.jherkenhoff.libqalculate.AngleUnit.ANGLE_UNIT_DEGREES
            AngleUnit.GRADIANS -> com.jherkenhoff.libqalculate.AngleUnit.ANGLE_UNIT_GRADIANS
        }
        //parseOptions.comma_as_separator = true
        //parseOptions.dot_as_separator = true
        return parseOptions
    }

    fun getEvaluationOptions(parseOptions: ParseOptions = getParseOptions()): EvaluationOptions {
        val evaluationOptions = EvaluationOptions()
        evaluationOptions.sync_units = true
        evaluationOptions.approximation = when (approximationMode) {
            ApproximationMode.TRY_EXACT -> com.jherkenhoff.libqalculate.ApproximationMode.APPROXIMATION_TRY_EXACT
            ApproximationMode.EXACT -> com.jherkenhoff.libqalculate.ApproximationMode.APPROXIMATION_EXACT
            ApproximationMode.APPROXIMATE -> com.jherkenhoff.libqalculate.ApproximationMode.APPROXIMATION_APPROXIMATE
        }
        evaluationOptions.parse_options = parseOptions
        evaluationOptions.allow_complex = true
        return evaluationOptions
    }
    
    fun getPrintOptions(): PrintOptions {
        val printOptions = PrintOptions()

        printOptions.use_unicode_signs = 1
        printOptions.spell_out_logical_operators = true
        printOptions.exp_display = when (expDisplay) {
            ExpDisplay.POWER_OF_10 -> com.jherkenhoff.libqalculate.ExpDisplay.EXP_POWER_OF_10
            ExpDisplay.LOWERCASE_E -> com.jherkenhoff.libqalculate.ExpDisplay.EXP_LOWERCASE_E
            ExpDisplay.UPPERCASE_E -> com.jherkenhoff.libqalculate.ExpDisplay.EXP_UPPERCASE_E
        }
        printOptions.interval_display = when (intervalDisplay) {
            IntervalDisplay.CONCISE -> com.jherkenhoff.libqalculate.IntervalDisplay.INTERVAL_DISPLAY_CONCISE
            IntervalDisplay.INTERVAL -> com.jherkenhoff.libqalculate.IntervalDisplay.INTERVAL_DISPLAY_INTERVAL
            IntervalDisplay.PLUSMINUS -> com.jherkenhoff.libqalculate.IntervalDisplay.INTERVAL_DISPLAY_PLUSMINUS
            IntervalDisplay.MIDPOINT -> com.jherkenhoff.libqalculate.IntervalDisplay.INTERVAL_DISPLAY_MIDPOINT
            IntervalDisplay.RELATIVE -> com.jherkenhoff.libqalculate.IntervalDisplay.INTERVAL_DISPLAY_RELATIVE
            IntervalDisplay.SIGNIFICANT_DIGITS -> com.jherkenhoff.libqalculate.IntervalDisplay.INTERVAL_DISPLAY_SIGNIFICANT_DIGITS
        }
        //printOptions.indicate_infinite_series = true // TODO: Why is this a char? Check with upstream libqalculate
        printOptions.negative_exponents = negativeExponents
        printOptions.abbreviate_names   = abbreviateNames
        printOptions.spacious           = spaciousOutput
        printOptions.decimalpoint_sign  = when (decimalSeparator) {
            DecimalSeparator.DOT -> "."
            DecimalSeparator.COMMA -> ","
        }
        printOptions.digit_grouping = when (digitGrouping) {
            true -> com.jherkenhoff.libqalculate.DigitGrouping.DIGIT_GROUPING_STANDARD
            false -> com.jherkenhoff.libqalculate.DigitGrouping.DIGIT_GROUPING_NONE
        }
        printOptions.min_exp = when (numericalDisplayMode) {
            NumericalDisplayMode.NORMAL -> -1
            NumericalDisplayMode.SCIENTIFIC -> 3
            NumericalDisplayMode.ENGINEERING -> -3
        }
        printOptions.multiplication_sign = when (multiplicationSign) {
            MultiplicationSign.DOT -> com.jherkenhoff.libqalculate.MultiplicationSign.MULTIPLICATION_SIGN_DOT
            MultiplicationSign.X -> com.jherkenhoff.libqalculate.MultiplicationSign.MULTIPLICATION_SIGN_X
            MultiplicationSign.ASTERISK -> com.jherkenhoff.libqalculate.MultiplicationSign.MULTIPLICATION_SIGN_ASTERISK
            MultiplicationSign.ALTDOT -> com.jherkenhoff.libqalculate.MultiplicationSign.MULTIPLICATION_SIGN_ALTDOT
        }
        printOptions.division_sign = when (divisionSign) {
            DivisionSign.DIVISION_SLASH -> com.jherkenhoff.libqalculate.DivisionSign.DIVISION_SIGN_DIVISION_SLASH
            DivisionSign.DIVISION -> com.jherkenhoff.libqalculate.DivisionSign.DIVISION_SIGN_DIVISION
            DivisionSign.SLASH -> com.jherkenhoff.libqalculate.DivisionSign.DIVISION_SIGN_SLASH
        }
        printOptions.place_units_separately = placeUnitsSeparately
        printOptions.use_denominator_prefix = useDenominatorPrefix
        printOptions.number_fraction_format = when (numberFractionFormat) {
            NumberFractionFormat.FRACTION_DECIMAL -> com.jherkenhoff.libqalculate.NumberFractionFormat.FRACTION_DECIMAL
            NumberFractionFormat.FRACTION_DECIMAL_EXACT -> com.jherkenhoff.libqalculate.NumberFractionFormat.FRACTION_DECIMAL_EXACT
            NumberFractionFormat.FRACTION_FRACTIONAL -> com.jherkenhoff.libqalculate.NumberFractionFormat.FRACTION_FRACTIONAL
            NumberFractionFormat.FRACTION_COMBINED -> com.jherkenhoff.libqalculate.NumberFractionFormat.FRACTION_COMBINED
            NumberFractionFormat.FRACTION_PERCENT -> com.jherkenhoff.libqalculate.NumberFractionFormat.FRACTION_PERCENT
            NumberFractionFormat.FRACTION_PERMILLE -> com.jherkenhoff.libqalculate.NumberFractionFormat.FRACTION_PERMILLE
            NumberFractionFormat.FRACTION_PERMYRIAD -> com.jherkenhoff.libqalculate.NumberFractionFormat.FRACTION_PERMYRIAD
        }

        return printOptions
    }
}