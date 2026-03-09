package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString

sealed class CalculatorKeyButtonActionLabel {
    data class Text(val text: AnnotatedString) : CalculatorKeyButtonActionLabel() {
        constructor(text: String) : this(androidx.compose.ui.text.AnnotatedString(text))
    }
    data class Icon(val icon: ImageVector, val description: String?) : CalculatorKeyButtonActionLabel()
}