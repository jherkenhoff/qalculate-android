package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.qalculate.data.EvaluationOptionsRepository
import javax.inject.Inject

class CalculateUseCase @Inject constructor(
    private val evaluationOptionsRepository: EvaluationOptionsRepository,
    private val calc: Calculator
) {
    operator fun invoke(expression: MathStructure): MathStructure {
        val evaluationOptions = evaluationOptionsRepository.getEvaluationOptions()

        val result = calc.calculate(expression, evaluationOptions)

        return result
    }
}