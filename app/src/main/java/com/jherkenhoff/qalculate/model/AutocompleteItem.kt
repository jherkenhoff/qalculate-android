package com.jherkenhoff.qalculate.model

enum class AutocompleteType {
    FUNCTION, UNIT, VARIABLE, CURRENCY
}

data class AutocompleteItem (
    val type: AutocompleteType,
    val name: String,
    val title: String,
    val abbreviations: List<String>,
    val description: String,
    val matchDepth: Int,
    val typeBeforeCursor: String,
    val typeAfterCursor: String
)