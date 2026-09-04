package com.jherkenhoff.qalculate.domain.model

sealed class Prefix {
    abstract val names: List<ExpressionItemName>
}

data class BinaryPrefix(
    override val names: List<ExpressionItemName>,
    val exponent: Int
) : Prefix()

data class DecimalPrefix(
    override val names: List<ExpressionItemName>,
    val exponent: Int
) : Prefix()

data class NumberPrefix(
    override val names: List<ExpressionItemName>,
) : Prefix()