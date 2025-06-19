package com.jherkenhoff.qalculate.model

import androidx.compose.ui.text.input.TextFieldValue
import java.time.LocalDateTime

data class Calculation(
    val creationTime: LocalDateTime,
    val modificationTime: LocalDateTime,
    val input: TextFieldValue,
    val parsed: String,
    val result: String
)