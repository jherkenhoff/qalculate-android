package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.AngleUnit
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.DigitGrouping
import com.jherkenhoff.libqalculate.ExpDisplay
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MultiplicationSign
import com.jherkenhoff.libqalculate.NumberFractionFormat
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.model.UserPreferences
import javax.inject.Inject

class ParseUseCase @Inject constructor(
   private val calc: Calculator
) {
    suspend operator fun invoke(input: String, userPreferences: UserPreferences): String {

        var printOptions = PrintOptions()

        printOptions.negative_exponents = false
        printOptions.abbreviate_names   = false
        printOptions.spacious           = true
        printOptions.interval_display   = IntervalDisplay.INTERVAL_DISPLAY_CONCISE
        printOptions.number_fraction_format = NumberFractionFormat.FRACTION_DECIMAL
        printOptions.digit_grouping = DigitGrouping.DIGIT_GROUPING_NONE
        printOptions.min_exp = 4
        printOptions.exp_display = ExpDisplay.EXP_POWER_OF_10
        printOptions.multiplication_sign = MultiplicationSign.MULTIPLICATION_SIGN_DOT
        printOptions.use_unicode_signs = 1
        printOptions.place_units_separately = false


        val parseOptions = ParseOptions()
        parseOptions.preserve_format = true
        parseOptions.angle_unit = when (userPreferences.angleUnit) {
            UserPreferences.AngleUnit.RADIANS -> AngleUnit.ANGLE_UNIT_RADIANS
            UserPreferences.AngleUnit.DEGREES -> AngleUnit.ANGLE_UNIT_DEGREES
            UserPreferences.AngleUnit.GRADIANS -> AngleUnit.ANGLE_UNIT_GRADIANS
        }
        //parseOptions.comma_as_separator = true
        //parseOptions.dot_as_separator = true

        when (userPreferences.decimalSeparator) {
            UserPreferences.DecimalSeparator.DOT -> {
                printOptions.decimalpoint_sign = "."
                calc.useDecimalPoint()
            }

            UserPreferences.DecimalSeparator.COMMA -> {
                printOptions.decimalpoint_sign = ","
                calc.useDecimalComma()
            }
        }

        val unlocalizedInput = calc.unlocalizeExpression(input, parseOptions)

        // TODO: Implement proper conversion handling
        val toExpressions = unlocalizedInput.split(" to ")
        if (toExpressions.size == 2) {
            val beforeToExpression = calc.parse(toExpressions.first(), parseOptions)
            val afterToExpression = calc.parse(toExpressions.last(), parseOptions)

            val beforeToString = calc.print(beforeToExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
            val afterToString = calc.print(afterToExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)

            return "$beforeToString to $afterToString"
        } else {
            val parsedExpression = calc.parse(unlocalizedInput, parseOptions)
            return calc.print(parsedExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
        }
    }
}