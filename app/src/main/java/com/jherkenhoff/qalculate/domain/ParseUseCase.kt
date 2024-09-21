package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.qalculate.data.ParseOptionsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ParseUseCase @Inject constructor(
    private val parseOptionRepository: ParseOptionsRepository,
    private val calc: Calculator
) {
    suspend operator fun invoke(input: String): MathStructure {
        return withContext(Dispatchers.Default) {
            val parseOptions = parseOptionRepository.getParseOptions()

            val parsedResult = calc.parse(input, parseOptions)

            return@withContext parsedResult
        }
    }
}