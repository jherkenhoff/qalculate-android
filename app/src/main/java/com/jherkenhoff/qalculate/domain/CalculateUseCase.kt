package com.jherkenhoff.qalculate.domain

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

        val unlocalizedInput = calc.unlocalizeExpression(input, parseOptions)

        val result = calc.calculate(unlocalizedInput, eo)

        return result
    }
}