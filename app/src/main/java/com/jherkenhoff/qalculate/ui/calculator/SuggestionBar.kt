package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.domain.AutocompleteItem

@Composable
fun SuggestionBar(
    entries: () -> List<AutocompleteItem>,
    modifier: Modifier = Modifier,
    onEntryClick: (String, String) -> Unit = {_, _ ->},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Outlined.Close, contentDescription = "Close suggestions")
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 8.dp)
        ) {
            items(entries()) {
                SuggestionCard(
                    title = it.title,
                    abbreviations = it.abbreviations,
                    description = it.description,
                    modifier = Modifier.clickable { onEntryClick(it.typeBeforeCursor, it.typeAfterCursor) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    val list = listOf(
        AutocompleteItem("Tesla", "M", emptyList(), "T", "", ""),
        AutocompleteItem("Thomson cross section", "M", emptyList(), "T", "", ""),
        AutocompleteItem("Terabyte", "M", emptyList(), "T", "", ""),
        AutocompleteItem("Planck temperature", "M", emptyList(), "T", "", ""),
    )
    SuggestionBar(
        { list },
        modifier = Modifier.height(100.dp)
    )
}