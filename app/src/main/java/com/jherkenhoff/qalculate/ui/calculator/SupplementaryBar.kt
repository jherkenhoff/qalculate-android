package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.AutocompleteItem

@Composable
fun SupplementaryBar(
    onKey: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    autocompleteItems: () -> List<AutocompleteItem>,
    onAutocompleteClick: (String, String) -> Unit = {_, _ ->},
    onAutocompleteDismiss: () -> Unit = {  }
) {
    val haptic = LocalHapticFeedback.current

    fun onClick(preCursorText: String, postCursorText: String) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onKey(preCursorText, postCursorText)
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 8.dp,
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp) // Increased to fit autocomplete with two lines
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        AnimatedContent(targetState = autocompleteItems().isNotEmpty()) { autocompleteVisible ->
            if (autocompleteVisible) {
                AutocompleteBar(
                    entries = autocompleteItems,
                    onEntryClick = onAutocompleteClick,
                    onDismiss = onAutocompleteDismiss,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            } else {
                QuickKeys(
                    onKey = onKey
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    val list = emptyList<AutocompleteItem>()

    SupplementaryBar(
        onKey = {_, _ ->},
        autocompleteItems = { list }
    )
}