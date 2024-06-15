package com.jherkenhoff.qalculate.data.model

import java.time.LocalDateTime

data class CalculationHistoryItem(
    val time: LocalDateTime,
    val input: String,
    val parsed: String,
    val result: String
)
