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
import com.jherkenhoff.qalculate.model.AutocompleteItem

@Composable
fun AutocompleteBar(
    entries: List<AutocompleteItem>,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {  },
    onEntryClick: (String, String) -> Unit = {_, _ ->},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = onDismiss) {
            Icon(Icons.Outlined.Close, contentDescription = "Close suggestions")
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 8.dp)
        ) {
            items(entries) {
                AutocompleteCard(it, modifier = Modifier.clickable(onClick = {onEntryClick(it.typeBeforeCursor, it.typeAfterCursor)}))
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    val list = emptyList<AutocompleteItem>()
    AutocompleteBar(
        list,
        modifier = Modifier.height(100.dp)
    )
}