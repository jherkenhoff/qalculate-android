package com.jherkenhoff.qalculate.domain

import android.util.Log
import com.jherkenhoff.libqalculate.AngleUnit
import com.jherkenhoff.libqalculate.DigitGrouping
import com.jherkenhoff.libqalculate.DivisionSign
import com.jherkenhoff.libqalculate.ExpDisplay
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MultiplicationSign
import com.jherkenhoff.libqalculate.NumberFractionFormat
import com.jherkenhoff.libqalculate.ParseOptions
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.qalculate.data.CalculatorRepository
import com.jherkenhoff.qalculate.model.UserPreferences
import javax.inject.Inject

class ParseUseCase @Inject constructor(
    private val calculatorRepository: CalculatorRepository
) {
    operator fun invoke(input: String, userPreferences: UserPreferences): String {
        val parseOptions = userPreferences.getParseOptions()
        parseOptions.preserve_format = true

        val printOptions = userPreferences.getPrintOptions()
        printOptions.negative_exponents = true
        printOptions.abbreviate_names   = false
        printOptions.spacious           = true
        printOptions.improve_division_multipliers = false
        printOptions.place_units_separately = false

        return calculatorRepository.parseAndPrint(input, parseOptions, printOptions)
    }
}