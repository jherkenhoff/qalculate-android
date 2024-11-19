package com.jherkenhoff.qalculate.domain

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.input.getTextBeforeSelection
import com.jherkenhoff.libqalculate.Calculator
import com.jherkenhoff.libqalculate.Unit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AutocompleteItem(
    val title: String,
    val name: String,
    val abbreviations: List<String>,
    val description: String,
    val typeBeforeCursor: String,
    val typeAfterCursor: String
)

enum class AutocompleteContext{
    INPUT, CONVERSION
}

data class AutocompleteResult(
    val success: Boolean,
    val items: List<AutocompleteItem> = emptyList(),
    val relevantText: String = "",
    val textBefore: String = "",
    val textAfter: String = "",
    val context: AutocompleteContext = AutocompleteContext.INPUT
)

class AutocompleteUseCase @Inject constructor(
    private val calc: Calculator
) {

    private suspend fun formatUnit(unit: Unit): String {

        return unit.title()
    }
    suspend operator fun invoke(input: TextFieldValue): AutocompleteResult {

        if (input.selection.length > 0) {
            return AutocompleteResult(success = false)
        }

        return withContext(Dispatchers.Default) {
            val textBefore = input.getTextBeforeSelection(input.text.length).toString()
            val textAfter = input.getTextAfterSelection(input.text.length).toString()

            val pattern = Regex("([a-zA-Z_]+$)")

            val match = pattern.find(textBefore) ?: return@withContext AutocompleteResult(success = false)

            val textBeforeWithoutRelevant = pattern.split(textBefore).first()

            val relevantText = match.value

            val unitSuggestions = calc.units.filter {
                it.title().lowercase().startsWith(relevantText.lowercase())
                        || it.name().lowercase().startsWith(relevantText.lowercase())
                }.map {
                    AutocompleteItem(it.title(), it.name(), listOf(it.abbreviation()), it.description(), it.abbreviation(), "")
                }

            val funcSuggestions = calc.functions.filter {
                it.title().lowercase().startsWith(relevantText.lowercase())
                        || it.name().lowercase().startsWith(relevantText.lowercase())
            }.map {
                AutocompleteItem(it.title(), it.name(), listOf(it.name()), it.description(), "${it.name()}(", ")")
            }

            return@withContext AutocompleteResult(
                success = true,
                items = unitSuggestions + funcSuggestions,
                relevantText = relevantText,
                textBefore = textBeforeWithoutRelevant,
                textAfter = textAfter,
                context = AutocompleteContext.INPUT
            )
        }

    }
}