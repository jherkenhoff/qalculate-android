package com.jherkenhoff.qalculate.domain.model

data class ExpressionItemName (
    val name: String,
    val abbreviation: Boolean,
    val suffix: Boolean,
    val unicode: Boolean,
    val plural: Boolean,
    val reference: Boolean,
    val avoidInput: Boolean,
    val caseSensitive: Boolean,
    val completionOnly: Boolean
)

sealed class ExpressionItem {
    abstract val id: Int
    abstract val names: List<ExpressionItemName>
    abstract val description: String
    abstract val category: String
}