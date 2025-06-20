package com.jherkenhoff.qalculate.data.calculations

import androidx.compose.ui.text.input.TextFieldValue
import com.jherkenhoff.qalculate.model.Calculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class CalculationsRepository @Inject constructor() {

    private val calculations: MutableStateFlow<Map<UUID, Calculation>> = MutableStateFlow(mapOf(
        UUID.randomUUID() to Calculation(
            LocalDateTime.now(),
            LocalDateTime.now(),
            TextFieldValue(""),
            "",
            ""
        )
    ))

    fun observeCalculations(): Flow<Map<UUID, Calculation>> = calculations

    fun updateCalculation(uuid: UUID, calculation: Calculation) {
        calculations.update { current ->
            current.toMutableMap().apply {
                this[uuid] = calculation
            }.toMap()
        }
    }

    fun deleteCalculation(uuid: UUID) {
        calculations.update { current ->
            current.filterKeys { it != uuid  }
        }
    }

    fun appendCalculation(calculation: Calculation): UUID {
        val newUuid = UUID.randomUUID()
        calculations.update { current ->
            current + (newUuid to calculation)
        }

        return newUuid
    }
}