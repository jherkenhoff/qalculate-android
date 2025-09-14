package com.jherkenhoff.qalculate.domain

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextBeforeSelection
import com.jherkenhoff.qalculate.data.AutocompleteRepository
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.UserPreferences
import javax.inject.Inject

data class AutocompleteResult (
    val relevantText: String = "",
    val contextRange: TextRange = TextRange(0),
    val items: List<AutocompleteItem> = emptyList()
)

class AutocompleteUseCase @Inject constructor(
    private val autocompleteRepository: AutocompleteRepository
) {

    operator fun invoke(inputTextFieldValue: TextFieldValue, userPreferences: UserPreferences): AutocompleteResult {

        val textBefore = inputTextFieldValue.getTextBeforeSelection(inputTextFieldValue.text.length).toString()

        val pattern = Regex("([a-zA-Z_]+$)")

        val match = pattern.find(textBefore)

        if (match == null) {
            return AutocompleteResult()
        }

        val relevantText = match.value

        return AutocompleteResult(
            relevantText,
            TextRange(match.range.start, match.range.endInclusive+1),
            autocompleteRepository.getAutocompleteSuggestions(relevantText)
        )
    }
}