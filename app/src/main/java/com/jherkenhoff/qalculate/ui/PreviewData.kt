package com.jherkenhoff.qalculate.ui

import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import java.time.LocalDateTime

object PreviewData {
    val calculationList = (0..9).map {
        CalculationHistoryItemData(
            it,
            it,
            "1+1",
            "1+1",
            "2",
            LocalDateTime.of(2026, 1, 1, 12, 0),
            LocalDateTime.of(2026, 1, 1, 12, 0)
        )
    }
}