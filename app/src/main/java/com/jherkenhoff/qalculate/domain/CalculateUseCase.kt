package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.AngleUnit
import com.jherkenhoff.libqalculate.ApproximationMode
import com.jherkenhoff.libqalculate.DigitGrouping
import com.jherkenhoff.libqalculate.DivisionSign
import com.jherkenhoff.libqalculate.EvaluationOptions
import com.jherkenhoff.libqalculate.ExpDisplay
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MultiplicationSign
import com.jherkenhoff.libqalculate.NumberFractionFormat
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.qalculate.data.CalculatorRepository
import com.jherkenhoff.qalculate.model.UserPreferences
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