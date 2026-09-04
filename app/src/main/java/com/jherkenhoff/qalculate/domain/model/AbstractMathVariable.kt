package com.jherkenhoff.qalculate.domain.model

sealed class AbstractMathVariable : ExpressionItem() {
}

data class Variable(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
) : AbstractMathVariable()

data class KnownVariable(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    val expression: String
) : AbstractMathVariable()

data class UnknownVariable(
    override val title: String,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    val assumptions: Assumptions
) : AbstractMathVariable()