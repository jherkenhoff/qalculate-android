package com.jherkenhoff.qalculate.data.model

import com.jherkenhoff.libqalculate.ApproximationMode
import com.jherkenhoff.libqalculate.DigitGrouping
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.ExpDisplay
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MultiplicationSign
import com.jherkenhoff.libqalculate.NumberFractionFormat
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions

fun UserPreferences.getQalculateParseOptions() : ParseOptions {
    var po = ParseOptions()
    po.preserve_format = true
    po.comma_as_separator = true
    po.dot_as_separator = true
    return po
}

fun UserPreferences.getQalculatePrintOptions() : PrintOptions {
    var po = PrintOptions()
    po.negative_exponents = negativeExponents
    po.abbreviate_names   = abbreviateNames
    po.spacious           = spacious
    po.interval_display   = IntervalDisplay.INTERVAL_DISPLAY_CONCISE
    po.decimalpoint_sign  = "."
    po.number_fraction_format = NumberFractionFormat.FRACTION_DECIMAL
    po.digit_grouping = DigitGrouping.DIGIT_GROUPING_NONE
    po.min_exp = 4
    po.exp_display = ExpDisplay.EXP_POWER_OF_10
    po.multiplication_sign = MultiplicationSign.MULTIPLICATION_SIGN_DOT
    po.use_unicode_signs = 1
    return po
}

fun UserPreferences.getQalculateEvaluationOptions() : EvaluationOptions {
    var eo = EvaluationOptions()
    eo.sync_units = syncUnits
    eo.approximation = ApproximationMode.APPROXIMATION_TRY_EXACT
    return eo
}