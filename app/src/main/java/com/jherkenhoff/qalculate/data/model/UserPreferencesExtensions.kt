package com.jherkenhoff.qalculate.data.model

import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions

fun UserPreferences.getQalculateParseOptions() : ParseOptions {
    var po = ParseOptions()
    return po
}

fun UserPreferences.getQalculatePrintOptions() : PrintOptions {
    var po = PrintOptions()
    return po
}

fun UserPreferences.getQalculateEvaluationOptions() : EvaluationOptions {
    var eo = EvaluationOptions()
    return eo
}