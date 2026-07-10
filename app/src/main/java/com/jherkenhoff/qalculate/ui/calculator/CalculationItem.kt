package com.jherkenhoff.qalculate.ui.calculator

data class CalculationItem (
    val id: Long,
    val input: String,
    val parsed: String,
    val result: String,
    val executionOrderNumber: Int?
)

data class CalculationListData (
    val items: List<CalculationItem>,
    val activeCalculationId: Long?
)