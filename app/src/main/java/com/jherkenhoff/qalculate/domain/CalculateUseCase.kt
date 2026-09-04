package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.qalculate.data.repository.CalculatorRepository
import com.jherkenhoff.qalculate.domain.model.UserPreferences
import javax.inject.Inject

class CalculateUseCase @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) {
    operator fun invoke(input: String, userPreferences: UserPreferences, format: Boolean = true): String {
        val evaluationOptions = userPreferences.getEvaluationOptions()
        val printOptions = userPreferences.getPrintOptions()

        return calculatorRepository.calculateAndPrint(
            input,
            evaluationOptions,
            printOptions,
            format = format
        )
    }
}