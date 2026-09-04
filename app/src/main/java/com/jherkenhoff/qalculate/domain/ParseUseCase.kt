package com.jherkenhoff.qalculate.domain

import android.util.Log
import com.jherkenhoff.qalculate.data.repository.CalculatorRepository
import com.jherkenhoff.qalculate.domain.model.UserPreferences
import javax.inject.Inject

class ParseUseCase @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) {
    operator fun invoke(input: String, userPreferences: UserPreferences): String {
        val parseOptions = userPreferences.getParseOptions()
        parseOptions.preserve_format = true

        val printOptions = userPreferences.getPrintOptions()
        printOptions.preserve_format = true
        printOptions.negative_exponents = true
        printOptions.abbreviate_names   = false
        printOptions.spacious           = true
        printOptions.improve_division_multipliers = false
        printOptions.place_units_separately = false

        Log.d("Moin", calculatorRepository.findAllUnknowns(input).toString())

        return calculatorRepository.parseAndPrint(input, parseOptions, printOptions)
    }
}