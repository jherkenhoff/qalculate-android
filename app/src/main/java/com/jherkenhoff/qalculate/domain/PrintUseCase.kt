package com.jherkenhoff.qalculate.domain

import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.MathStructure
import com.jherkenhoff.qalculate.data.model.UserPreferences
import com.jherkenhoff.qalculate.data.model.getQalculatePrintOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PrintUseCase @Inject constructor(
    private val calc: Calculator
) {
    suspend operator fun invoke(mathStructure: MathStructure, userPreferences: UserPreferences): String {
        return withContext(Dispatchers.Default) {

            val po = userPreferences.getQalculatePrintOptions()

            return@withContext calc.print(mathStructure, 10000, po)
        }
    }
}