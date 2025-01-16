package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.data.model.UserPreferences
import com.jherkenhoff.qalculate.data.model.getQalculateParseOptions
import com.jherkenhoff.qalculate.data.model.getQalculatePrintOptions
import javax.inject.Inject

class ParseUseCase @Inject constructor(
    private val calc: Calculator
) {
    suspend operator fun invoke(input: String, userPreferences: UserPreferences): String {

        val parseOptions = userPreferences.getQalculateParseOptions()
        val printOptions = userPreferences.getQalculatePrintOptions()

        printOptions.place_units_separately = false
        printOptions.preserve_format = true
        printOptions.use_unicode_signs = 1
        printOptions.abbreviate_names = false
        printOptions.short_multiplication = false

        // TODO: Implement proper conversion handling
        val toExpressions = input.split(" to ")
        if (toExpressions.size == 2) {
            val beforeToExpression = calc.parse(toExpressions.first(), parseOptions)
            val afterToExpression = calc.parse(toExpressions.last(), parseOptions)

            val beforeToString = calc.print(beforeToExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
            val afterToString = calc.print(afterToExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)

            return "$beforeToString to $afterToString"
        } else {
            val parsedExpression = calc.parse(input, parseOptions)
            return calc.print(parsedExpression, 2000, printOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
        }
    }
}