package com.jherkenhoff.qalculate.ui.calculator

data class CalculationItem (
    val id: Long,
    val input: String,
    val parsed: String,
    val result: String
)

data class CalculationListData (
    val items: List<CalculationItem>,
    val activeCalculationIdx: Int?
)