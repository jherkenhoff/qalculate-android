package com.jherkenhoff.qalculate.data

import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import javax.inject.Inject

class CalculationHistoryRepository @Inject constructor() {

    private val calculationHisotry = MutableStateFlow(listOf<CalculationHistoryItem>())

    fun observeCalculationHistory(): Flow<List<CalculationHistoryItem>> = calculationHisotry

    fun appendCalculation(calculation: CalculationHistoryItem) {
        calculationHisotry.value += calculation
    }
    fun appendCalculation(input: String, parsed: String, result: String) {
        appendCalculation(
            CalculationHistoryItem(
                LocalDateTime.now(),
                input,
                parsed,
                result
            )
        )
    }
}