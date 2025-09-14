package com.jherkenhoff.qalculate.data.model

import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.IntervalDisplay
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
    return po
}

fun UserPreferences.getQalculateEvaluationOptions() : EvaluationOptions {
    var eo = EvaluationOptions()
    eo.sync_units = syncUnits
    return eo
}