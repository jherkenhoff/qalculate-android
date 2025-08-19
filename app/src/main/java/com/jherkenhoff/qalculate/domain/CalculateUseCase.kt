package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.ApproximationMode
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.qalculate.data.EvaluationOptionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculateUseCase @Inject constructor(
    private val evaluationOptionsRepository: EvaluationOptionsRepository,
    private val calc: Calculator
) {
    suspend operator fun invoke(expression: String, approximationMode: ApproximationMode): MathStructure {
        calc.setCustomAngleUnit(calc.getDegUnit())

        return withContext(Dispatchers.Default) {
            val evaluationOptions = evaluationOptionsRepository.getEvaluationOptions()

            evaluationOptions.approximation = approximationMode

            val result = calc.calculate(expression, evaluationOptions)

            return@withContext result
        }
    }
}