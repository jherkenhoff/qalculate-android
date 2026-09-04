package com.jherkenhoff.qalculate.domain.model

sealed class MathUnit : ExpressionItem() {
    abstract val system: String
    open val exponent: Int = 1
}

data class BaseUnit(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val system: String,
) : MathUnit()

data class AliasUnit(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val system: String,
    override val exponent: Int,
    val parentUnit: MathUnit
) : MathUnit()

data class PrefixedUnit(
    val prefix: Prefix,
    val unit: MathUnit
)

data class CompositeUnit(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val system: String,
    val childUnits: List<PrefixedUnit>
) : MathUnit()