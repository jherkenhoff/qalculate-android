package com.jherkenhoff.qalculate.domain

import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextBeforeSelection
import com.jherkenhoff.qalculate.data.AutocompleteRepository
import com.jherkenhoff.qalculate.data.model.UserPreferences
import com.jherkenhoff.qalculate.model.AutocompleteItem
import javax.inject.Inject

data class AutocompleteResult (
    val relevantText: String = "",
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
            return AutocompleteResult(
                "",
                emptyList()
            )
        }

        val relevantText = match.value

        return AutocompleteResult(
            relevantText,
            autocompleteRepository.getAutocompleteSuggestions(relevantText)
        )
    }
}