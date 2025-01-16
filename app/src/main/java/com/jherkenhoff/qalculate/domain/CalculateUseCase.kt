package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.qalculate.data.model.UserPreferences
import com.jherkenhoff.qalculate.data.model.getQalculateEvaluationOptions
import javax.inject.Inject

class CalculateUseCase @Inject constructor(
    private val calc: Calculator
) {
    operator fun invoke(expression: String, userPreferences: UserPreferences): MathStructure {
        val eo = userPreferences.getQalculateEvaluationOptions()

        val result = calc.calculate(expression)

        return result
    }
}