package com.jherkenhoff.qalculate.model

data class PositionedKeySpec (
    val row: Int,
    val col: Int,
    val rowSpan: Int,
    val colSpan: Int,
    val keySpec: KeySpec
) {
    constructor(row: Int, col: Int, keySpec: KeySpec) : this(row, col, 1, 1, keySpec)
}