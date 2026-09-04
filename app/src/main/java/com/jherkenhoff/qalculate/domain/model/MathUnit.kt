package com.jherkenhoff.qalculate.domain.model

sealed class MathUnit : ExpressionItem() {
    abstract val system: String
}

data class BaseUnit(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val system: String
) : MathUnit()

data class AliasUnit(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val system: String,
    val baseUnit: MathUnit
) : MathUnit()

data class CompositeUnit(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val system: String,
) : MathUnit()