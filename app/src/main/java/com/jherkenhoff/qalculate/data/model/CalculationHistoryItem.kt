package com.jherkenhoff.qalculate.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CalculationHistoryItem(
    val time: String, // ISO string for persistence
    val input: String,
    val parsed: String,
    val result: String
)
