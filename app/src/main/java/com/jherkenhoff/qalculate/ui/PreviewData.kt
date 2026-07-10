package com.jherkenhoff.qalculate.ui

import com.jherkenhoff.qalculate.ui.calculator.CalculationItem

object PreviewData {
    val calculationList = listOf(
        CalculationItem(
            0,
            "1+1",
            "1+1",
            "2",
        ),
        CalculationItem(
            1,
            "boltzmann * planck",
            "boltzmann*planck",
            "9.1482771E-57 second*joule^2/kelvin",
        ),
        CalculationItem(
            2,
            "1+1",
            "1+1",
            "2",
        ),
        CalculationItem(
            3,
            "boltzmann + planck",
            "boltzmann + planck",
            "6.626 070 15 × 10^(−34) Joule·seconds + 13.806 49 peta joule / terra kelvin",
        ),
    )
}