package com.jherkenhoff.qalculate.data.model

import com.jherkenhoff.libqalculate.MathStructure
import java.time.LocalDateTime

data class CalculationHistory(
    val calculations: List<Calculation>
)
data class Calculation(
    val time: LocalDateTime,
    val input: String,
    val parsed: MathStructure,
    val result: MathStructure
)
