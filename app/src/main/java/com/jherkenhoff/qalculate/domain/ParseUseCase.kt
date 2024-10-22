package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.data.ParseOptionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ParseUseCase @Inject constructor(
    private val parseOptionRepository: ParseOptionsRepository,
    private val calc: Calculator
) {
    suspend operator fun invoke(input: String): String {
        return withContext(Dispatchers.Default) {
            val parseOptions = parseOptionRepository.getParseOptions()


            val parsedPrintOptions = PrintOptions()
            parsedPrintOptions.place_units_separately = false
            parsedPrintOptions.preserve_format = true
            parsedPrintOptions.use_unicode_signs = 1
            parsedPrintOptions.abbreviate_names = false
            parsedPrintOptions.short_multiplication = false


            // TODO: Implement proper conversion handling
            val toExpressions = input.split(" to ")
            if (toExpressions.size == 2) {
                val beforeToExpression = calc.parse(toExpressions.first(), parseOptions)
                val afterToExpression = calc.parse(toExpressions.last(), parseOptions)

                val beforeToString = calc.print(beforeToExpression, 2000, parsedPrintOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
                val afterToString = calc.print(afterToExpression, 2000, parsedPrintOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)

                return@withContext "$beforeToString to $afterToString"
            } else {
                val parsedExpression = calc.parse(input, parseOptions)
                return@withContext calc.print(parsedExpression, 2000, parsedPrintOptions, true, 1, libqalculateConstants.TAG_TYPE_HTML)
            }
        }
    }
}