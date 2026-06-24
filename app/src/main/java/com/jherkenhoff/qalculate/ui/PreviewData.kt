package com.jherkenhoff.qalculate.ui

import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import java.time.LocalDateTime

object PreviewData {
    val calculationList = listOf(
        CalculationHistoryItemData(
            0,
            0,
            "1+1",
            "1+1",
            "2",
            LocalDateTime.of(2026, 1, 1, 12, 0),
            LocalDateTime.of(2026, 1, 1, 12, 0)
        ),
        CalculationHistoryItemData(
            1,
            1,
            "boltzmann * planck",
            "boltzmann*planck",
            "9.1482771E-57 second*joule^2/kelvin",
            LocalDateTime.of(2026, 1, 1, 12, 1),
            LocalDateTime.of(2026, 1, 1, 12, 1)
        ),
        CalculationHistoryItemData(
            2,
            2,
            "1+1",
            "1+1",
            "2",
            LocalDateTime.of(2026, 1, 1, 12, 2),
            LocalDateTime.of(2026, 1, 1, 12, 2)
        ),
        CalculationHistoryItemData(
            3,
            3,
            "boltzmann + planck",
            "boltzmann + planck",
            "6.626 070 15 × 10^(−34) Joule·seconds + 13.806 49 peta joule / terra kelvin",
            LocalDateTime.of(2026, 1, 1, 12, 3),
            LocalDateTime.of(2026, 1, 1, 12, 3)
        ),
    )
}