package com.jherkenhoff.qalculate.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.domain.AutocompleteItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SupplementaryBar(
    onKey: (String) -> Unit,
    modifier: Modifier = Modifier,
    autocompleteItems: () -> List<AutocompleteItem>,
    onAutocompleteClick: (String) -> Unit = {}
) {
    val haptic = LocalHapticFeedback.current

    fun onClick(text: String) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onKey(text)
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
    ) {
        AnimatedContent(targetState = autocompleteItems().isNotEmpty()) {autocompleteVisible ->
            if (autocompleteVisible) {
                AutocompleteChips(
                    entries = autocompleteItems,
                    onEntryClick = onAutocompleteClick
                )
            } else {
                QuickKeysPanel(
                    onKeyAction = onKey,
                )
            }
            
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val list = listOf(
        AutocompleteItem("Tesla", "M", "T"),
        AutocompleteItem("Thomson cross section", "M", "T"),
        AutocompleteItem("Terabyte", "M", "T"),
        AutocompleteItem("Planck temperature", "M", "T"),
    )

    SupplementaryBar(
        onKey = {},
        autocompleteItems = { list }
    )
}