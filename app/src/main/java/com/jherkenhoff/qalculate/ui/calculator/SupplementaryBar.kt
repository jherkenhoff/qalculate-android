package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        AnimatedContent(targetState = autocompleteItems().isNotEmpty()) {autocompleteVisible ->
            if (autocompleteVisible) {
                AutocompleteBar(
                    entries = autocompleteItems,
                    onEntryClick = onAutocompleteClick,
                    onDismiss = onAutocompleteDismiss
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