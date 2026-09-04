package com.jherkenhoff.qalculate.data.mapper

import android.util.Log
import com.jherkenhoff.libqalculate.ExpressionItem
import com.jherkenhoff.libqalculate.ExpressionName
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

fun ExpressionItem.names() =
    (1L..countNames()).map { i -> getName(i).toModel() }

fun com.jherkenhoff.libqalculate.Unit.toModel(): MathUnit {
    return when (this) {
        is com.jherkenhoff.libqalculate.AliasUnit -> AliasUnit(
            names = names(),
            description = description(),
            category = category(),
            system = system(),
            baseUnit = baseUnit().toModel()
        )

        is com.jherkenhoff.libqalculate.CompositeUnit -> CompositeUnit(
            names = names(),
            description = description(),
            category = category(),
            system = system()
        )

        else -> BaseUnit(
            names = names(),
            description = description(),
            category = category(),
            system = system()
        )
    }
}

fun com.jherkenhoff.libqalculate.MathFunction.toModel(): MathFunction {
    return MathFunction(
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
            names = names(),
            description = description(),
            category = category(),
            expression = expression(),
        )

        is com.jherkenhoff.libqalculate.UnknownVariable -> {
            UnknownVariable(
                names = names(),
                description = description(),
                category = category(),
                assumptions = assumptions().toModel(),
            )
        }

        else -> Variable(
            names = names(),
            description = description(),
            category = category(),
        )
    }
}