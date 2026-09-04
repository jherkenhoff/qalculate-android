package com.jherkenhoff.qalculate.data.mapper

import com.jherkenhoff.libqalculate.AliasUnit_Composite
import com.jherkenhoff.libqalculate.ExpressionItem
import com.jherkenhoff.libqalculate.ExpressionName
import com.jherkenhoff.libqalculate.SWIGTYPE_p_int
import com.jherkenhoff.libqalculate.Unit
import com.jherkenhoff.qalculate.domain.model.AliasUnit
import com.jherkenhoff.qalculate.domain.model.Assumptions
import com.jherkenhoff.qalculate.domain.model.BaseUnit
import com.jherkenhoff.qalculate.domain.model.CompositeUnit
import com.jherkenhoff.qalculate.domain.model.ExpressionItemName
import com.jherkenhoff.qalculate.domain.model.KnownVariable
import com.jherkenhoff.qalculate.domain.model.MathFunction
import com.jherkenhoff.qalculate.domain.model.MathUnit
import com.jherkenhoff.qalculate.domain.model.UnknownVariable
import com.jherkenhoff.qalculate.domain.model.AbstractMathVariable
import com.jherkenhoff.qalculate.domain.model.BinaryPrefix
import com.jherkenhoff.qalculate.domain.model.DecimalPrefix
import com.jherkenhoff.qalculate.domain.model.Prefix
import com.jherkenhoff.qalculate.domain.model.PrefixedUnit
import com.jherkenhoff.qalculate.domain.model.Variable

fun ExpressionName.toModel() = ExpressionItemName(
    name = name,
    abbreviation = abbreviation,
    suffix = suffix,
    unicode = unicode,
    plural = plural,
    reference = reference,
    avoidInput = avoid_input,
    caseSensitive = case_sensitive,
    completionOnly = completion_only,
)

fun com.jherkenhoff.libqalculate.Prefix.toModel() : Prefix {
    return when (this) {
        is com.jherkenhoff.libqalculate.BinaryPrefix -> BinaryPrefix(
            names = names(),
            exponent = exponent()
        )
        is com.jherkenhoff.libqalculate.DecimalPrefix -> DecimalPrefix(
            names = names(),
            exponent = exponent()
        )
        else -> error("Unexpected Prefix child class: ${this::class.qualifiedName}")
    }
}

fun com.jherkenhoff.libqalculate.Prefix.names() =
    (1L..countNames()).map { i -> getName(i).toModel() }

fun ExpressionItem.names() =
    (1L..countNames()).map { i -> getName(i).toModel() }

fun Unit.toModel(): MathUnit {
    return when (this) {
        is com.jherkenhoff.libqalculate.AliasUnit -> AliasUnit(
            title = title(),
            names = names(),
            description = description(),
            category = category(),
            system = system(),
            exponent = baseExponent(),
            parentUnit = firstBaseUnit().toModel()
        )

        is com.jherkenhoff.libqalculate.CompositeUnit -> {
            CompositeUnit(
                title = title(),
                names = names(),
                description = description(),
                category = category(),
                system = system(),
                childUnits = (1..countUnits()).map { i ->
                    val nativeChildUnit = get(i)
                    PrefixedUnit(
                        prefix = (nativeChildUnit as AliasUnit_Composite).prefix().toModel(),
                        unit = nativeChildUnit.toModel(),
                    )
                }
            )
        }

        else -> BaseUnit(
            title = title(),
            names = names(),
            description = description(),
            category = category(),
            system = system()
        )
    }
}

fun com.jherkenhoff.libqalculate.MathFunction.toModel(): MathFunction {
    return MathFunction(
        title = title(),
        names = names(),
        description = description(),
        category = category(),
        example = example(),
    )
}

fun com.jherkenhoff.libqalculate.Assumptions.toModel() = Assumptions(
    positive = isPositive,
    negative = isNegative,
    nonNegative = isNonNegative,
    nonPositive = isNonPositive,
    integer = isInteger,
    boolean = isBoolean,
    number = isNumber,
    rational = isRational,
    real = isReal,
    complex = isComplex,
    nonZero = isNonZero,
    nonMatrix = isNonMatrix,
    scalar = isScalar
)

fun com.jherkenhoff.libqalculate.Variable.toModel(): AbstractMathVariable {
    return when (this) {

        is com.jherkenhoff.libqalculate.KnownVariable -> KnownVariable(
            title = title(),
            names = names(),
            description = description(),
            category = category(),
            expression = expression(),
        )

        is com.jherkenhoff.libqalculate.UnknownVariable -> {
            UnknownVariable(
                title = title(),
                names = names(),
                description = description(),
                category = category(),
                assumptions = assumptions().toModel(),
            )
        }

        else -> Variable(
            title = title(),
            names = names(),
            description = description(),
            category = category(),
        )
    }
}