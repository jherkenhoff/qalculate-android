package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.AngleUnit
import com.jherkenhoff.libqalculate.ApproximationMode
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.qalculate.model.UserPreferences
import javax.inject.Inject

class CalculateUseCase @Inject constructor(
    private val calc: Calculator
) {
    operator fun invoke(input: String, userPreferences: UserPreferences): MathStructure {

        var eo = EvaluationOptions()
        eo.sync_units = true
        eo.approximation = ApproximationMode.APPROXIMATION_TRY_EXACT

        when (userPreferences.decimalSeparator) {
            UserPreferences.DecimalSeparator.DOT -> {
                calc.useDecimalPoint()
            }

            UserPreferences.DecimalSeparator.COMMA -> {
                calc.useDecimalComma()
            }
        }

        val parseOptions = ParseOptions()
        parseOptions.preserve_format = true
        parseOptions.angle_unit = when (userPreferences.angleUnit) {
            UserPreferences.AngleUnit.RADIANS -> AngleUnit.ANGLE_UNIT_RADIANS
            UserPreferences.AngleUnit.DEGREES -> AngleUnit.ANGLE_UNIT_DEGREES
            UserPreferences.AngleUnit.GRADIANS -> AngleUnit.ANGLE_UNIT_GRADIANS
        }

        val unlocalizedInput = calc.unlocalizeExpression(input, parseOptions)

        val ms = calc.parse(unlocalizedInput, parseOptions)

        val result = calc.calculate(ms, eo)

        return result
    }
}