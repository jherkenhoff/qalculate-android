package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.AngleUnit
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.DigitGrouping
import com.jherkenhoff.libqalculate.DivisionSign
import com.jherkenhoff.libqalculate.ExpDisplay
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MultiplicationSign
import com.jherkenhoff.libqalculate.NumberFractionFormat
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.data.CalculatorRepository
import com.jherkenhoff.qalculate.model.UserPreferences
import javax.inject.Inject

class ParseUseCase @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) {
    operator fun invoke(input: String, userPreferences: UserPreferences): String {
        val printOptions = PrintOptions()

        printOptions.negative_exponents = true
        printOptions.abbreviate_names   = false
        printOptions.spacious           = true
        printOptions.interval_display   = IntervalDisplay.INTERVAL_DISPLAY_CONCISE
        printOptions.number_fraction_format = NumberFractionFormat.FRACTION_DECIMAL
        printOptions.digit_grouping = DigitGrouping.DIGIT_GROUPING_NONE
        printOptions.min_exp = 4
        printOptions.exp_display = when (userPreferences.expDisplay) {
            UserPreferences.ExpDisplay.POWER_OF_10 -> ExpDisplay.EXP_POWER_OF_10
            UserPreferences.ExpDisplay.LOWERCASE_E -> ExpDisplay.EXP_LOWERCASE_E
            UserPreferences.ExpDisplay.UPPERCASE_E -> ExpDisplay.EXP_UPPERCASE_E
        }
        printOptions.use_unicode_signs = 1
        printOptions.place_units_separately = false
        printOptions.decimalpoint_sign  = when (userPreferences.decimalSeparator) {
            UserPreferences.DecimalSeparator.DOT -> "."
            UserPreferences.DecimalSeparator.COMMA -> ","
        }
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

        val parseOptions = ParseOptions()
        parseOptions.preserve_format = true
        parseOptions.angle_unit = when (userPreferences.angleUnit) {
            UserPreferences.AngleUnit.RADIANS -> AngleUnit.ANGLE_UNIT_RADIANS
            UserPreferences.AngleUnit.DEGREES -> AngleUnit.ANGLE_UNIT_DEGREES
            UserPreferences.AngleUnit.GRADIANS -> AngleUnit.ANGLE_UNIT_GRADIANS
        }
        //parseOptions.comma_as_separator = true
        //parseOptions.dot_as_separator = true

        printOptions.decimalpoint_sign = when (userPreferences.decimalSeparator) {
            UserPreferences.DecimalSeparator.DOT -> "."
            UserPreferences.DecimalSeparator.COMMA -> ","
        }

        return calculatorRepository.parse(input, parseOptions, printOptions)
    }
}