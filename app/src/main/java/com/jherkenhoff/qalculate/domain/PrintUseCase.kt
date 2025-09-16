package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.DigitGrouping
import com.jherkenhoff.libqalculate.DivisionSign
import com.jherkenhoff.libqalculate.ExpDisplay
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.libqalculate.MultiplicationSign
import com.jherkenhoff.libqalculate.NumberFractionFormat
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.model.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PrintUseCase @Inject constructor(
    private val calc: Calculator
) {
    suspend operator fun invoke(mathStructure: MathStructure, userPreferences: UserPreferences): String {
        return withContext(Dispatchers.Default) {

            var printOptions = PrintOptions()

            printOptions.negative_exponents = false
            printOptions.abbreviate_names   = true
            printOptions.spacious           = true
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

            return@withContext calc.print(mathStructure, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
        }
    }
}