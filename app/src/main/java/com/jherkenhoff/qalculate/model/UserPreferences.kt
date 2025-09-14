package com.jherkenhoff.qalculate.model

data class UserPreferences(
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT
) {
    enum class DecimalSeparator {DOT, COMMA}
}