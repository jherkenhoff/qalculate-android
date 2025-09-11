package com.jherkenhoff.qalculate.data

import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import javax.inject.Inject

open class CalculationHistoryRepository @Inject constructor() {

    private val calculationHisotry = MutableStateFlow(listOf<CalculationHistoryItem>())

    open fun observeCalculationHistory(): Flow<List<CalculationHistoryItem>> = calculationHisotry

    open fun appendCalculation(calculation: CalculationHistoryItem) {
        calculationHisotry.value += calculation
    }
    open fun appendCalculation(input: String, parsed: String, result: String) {
        appendCalculation(
            CalculationHistoryItem(
                java.time.LocalDateTime.now().toString(),
                input,
                parsed,
                result
            )
        )
    }
}