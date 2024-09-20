package com.jherkenhoff.qalculate.domain

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextBeforeSelection
import com.jherkenhoff.libqalculate.Calculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AutocompleteItem(
    val title: String,
    val name: String,
    val abbreviation: String
)

class AutocompleteUseCase @Inject constructor(
    private val calc: Calculator
) {
    suspend operator fun invoke(input: TextFieldValue): List<AutocompleteItem> {

        if (input.selection.length > 0) {
            return listOf()
        }

        return withContext(Dispatchers.Default) {
            var currentString = input.getTextBeforeSelection(input.text.length).toString()

            val pattern = Regex("([a-zA-Z_]+$)")

            val match = pattern.find(currentString) ?: return@withContext listOf()

            currentString = match.value

            val unitList = calc.units.filter {
                it.title().lowercase().startsWith(currentString.lowercase())
                        || it.name().lowercase().startsWith(currentString.lowercase())
                }.map {
                    AutocompleteItem(it.title(), it.name(), it.abbreviation())
                }

            return@withContext unitList
        }

    }
}