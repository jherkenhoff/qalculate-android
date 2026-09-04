package com.jherkenhoff.qalculate.domain.model

sealed class AbstractMathFunction : ExpressionItem() {
    abstract val example: String
}

data class MathFunction(
    override val id: Int,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val example: String,
) : AbstractMathFunction()

data class UserFunction(
    override val id: Int,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val example: String,
    val equation: String,
    val internalEquation: String,
) : AbstractMathFunction()

data class DataSet(
    override val id: Int,
    override val names: List<ExpressionItemName>,
    override val description: String,
    override val category: String,
    override val example: String,
    val copyright: String,
) : AbstractMathFunction()