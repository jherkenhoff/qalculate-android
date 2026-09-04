package com.jherkenhoff.qalculate.domain.model

data class Assumptions(
    val positive: Boolean,
    val negative: Boolean,
    val nonNegative: Boolean,
    val nonPositive: Boolean,
    val integer: Boolean,
    val boolean: Boolean,
    val number: Boolean,
    val rational: Boolean,
    val real: Boolean,
    val complex: Boolean,
    val nonZero: Boolean,
    val nonMatrix: Boolean,
    val scalar: Boolean,
)