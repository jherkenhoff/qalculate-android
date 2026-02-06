package com.jherkenhoff.qalculate.model

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString

sealed class CalcActionLabel {
    data class Text(val text: AnnotatedString) : CalcActionLabel() {
        constructor(text: String) : this(androidx.compose.ui.text.AnnotatedString(text))
    }
    data class Icon(val icon: ImageVector, val description: String?) : CalcActionLabel()
}