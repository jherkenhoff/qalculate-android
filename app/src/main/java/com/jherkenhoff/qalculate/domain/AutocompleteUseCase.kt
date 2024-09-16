package com.jherkenhoff.qalculate.domain

import androidx.compose.ui.text.input.TextFieldValue
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.ExpressionItem
import javax.inject.Inject

class AutocompleteUseCase @Inject constructor(
    private val calc: Calculator
) {
    operator fun invoke(input: TextFieldValue): List<ExpressionItem> {
        return listOf(calc.units[0], calc.units[1])
    }
}