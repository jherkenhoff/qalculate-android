package com.jherkenhoff.qalculate.domain.model

data class ExpressionItemName (
    val name: String,
    val abbreviation: Boolean = false,
    val suffix: Boolean = false,
    val unicode: Boolean = false,
    val plural: Boolean = false,
    val reference: Boolean = false,
    val avoidInput: Boolean = false,
    val caseSensitive: Boolean = false,
    val completionOnly: Boolean = false
)

sealed class ExpressionItem {
    abstract val names: List<ExpressionItemName>
    abstract val description: String
    abstract val category: String
}