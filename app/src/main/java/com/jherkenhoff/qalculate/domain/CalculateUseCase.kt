package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.AngleUnit
import com.jherkenhoff.libqalculate.ApproximationMode
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.DigitGrouping
import com.jherkenhoff.libqalculate.DivisionSign
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.ExpDisplay
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MultiplicationSign
import com.jherkenhoff.libqalculate.NumberFractionFormat
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.model.UserPreferences
import javax.inject.Inject

class CalculateUseCase @Inject constructor(
    private val calc: Calculator
) {
    suspend operator fun invoke(input: String, userPreferences: UserPreferences): String {


        val parseOptions = ParseOptions()
        parseOptions.preserve_format = true
        parseOptions.angle_unit = when (userPreferences.angleUnit) {
            UserPreferences.AngleUnit.RADIANS -> AngleUnit.ANGLE_UNIT_RADIANS
            UserPreferences.AngleUnit.DEGREES -> AngleUnit.ANGLE_UNIT_DEGREES
            UserPreferences.AngleUnit.GRADIANS -> AngleUnit.ANGLE_UNIT_GRADIANS
        }


        var eo = EvaluationOptions()
        eo.sync_units = true
        eo.approximation = ApproximationMode.APPROXIMATION_TRY_EXACT
        eo.parse_options = parseOptions




        var printOptions = PrintOptions()

        printOptions.negative_exponents = userPreferences.negativeExponents
        printOptions.abbreviate_names   = userPreferences.abbreviateNames
        printOptions.spacious           = userPreferences.spaciousOutput
        printOptions.interval_display   = IntervalDisplay.INTERVAL_DISPLAY_CONCISE
        printOptions.decimalpoint_sign  = when (userPreferences.decimalSeparator) {
            UserPreferences.DecimalSeparator.DOT -> "."
            UserPreferences.DecimalSeparator.COMMA -> ","
        }
        printOptions.number_fraction_format = NumberFractionFormat.FRACTION_DECIMAL
        printOptions.digit_grouping = DigitGrouping.DIGIT_GROUPING_NONE
        printOptions.min_exp = 4
        printOptions.exp_display = ExpDisplay.EXP_POWER_OF_10
        printOptions.multiplication_sign = when (userPreferences.multiplicationSign) {
            UserPreferences.MultiplicationSign.DOT -> MultiplicationSign.MULTIPLICATION_SIGN_DOT
            UserPreferences.MultiplicationSign.X -> MultiplicationSign.MULTIPLICATION_SIGN_X
            UserPreferences.MultiplicationSign.ASTERISK -> MultiplicationSign.MULTIPLICATION_SIGN_ASTERISK
            UserPreferences.MultiplicationSign.ALTDOT -> MultiplicationSign.MULTIPLICATION_SIGN_ALTDOT
        }
        printOptions.division_sign = when (userPreferences.divisionSign) {
            UserPreferences.DivisionSign.DIVISION_SLASH -> DivisionSign.DIVISION_SIGN_DIVISION_SLASH
            UserPreferences.DivisionSign.DIVISION -> DivisionSign.DIVISION_SIGN_DIVISION
            UserPreferences.DivisionSign.SLASH -> DivisionSign.DIVISION_SIGN_SLASH
        }
        printOptions.use_unicode_signs = 1
        printOptions.place_units_separately = true

        when (userPreferences.decimalSeparator) {
            UserPreferences.DecimalSeparator.DOT -> {
                calc.useDecimalPoint()
            }

            UserPreferences.DecimalSeparator.COMMA -> {
                calc.useDecimalComma()
            }
        }

        val unlocalizedInput = calc.unlocalizeExpression(input, parseOptions)
        val ms = calc.parse(unlocalizedInput, parseOptions)
        val mathStructure = calc.calculate(ms, eo)

        return calc.print(mathStructure, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)

    }
}