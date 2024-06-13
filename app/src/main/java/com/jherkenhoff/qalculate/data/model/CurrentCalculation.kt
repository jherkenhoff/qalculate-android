package com.jherkenhoff.qalculate.data.model

import com.jherkenhoff.libqalculate.MathStructure

data class CurrentCalculation(
    val input: String,
    val parsedExpression: MathStructure,
    val resultExpression: MathStructure
)