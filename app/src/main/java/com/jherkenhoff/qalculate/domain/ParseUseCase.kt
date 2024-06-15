package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.qalculate.data.ParseOptionsRepository
import javax.inject.Inject

class ParseUseCase @Inject constructor(
    private val parseOptionRepository: ParseOptionsRepository,
    private val calc: Calculator
) {
    operator fun invoke(input: String): MathStructure {
        val parseOptions = parseOptionRepository.getParseOptions()

        val parsedResult = calc.parse(input, parseOptions)

        return parsedResult
    }
}