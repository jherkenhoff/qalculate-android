package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.libqalculate.ExpressionItem
import com.jherkenhoff.libqalculate.Unit

@Composable
fun AutocompleteList(
    entries: List<ExpressionItem>,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier
            .fillMaxWidth()
    ) {
        Column {
            AutocompleteListItem()
            HorizontalDivider()
            AutocompleteListItem()
        }
    }
}

@Composable
fun AutocompleteListItem() {
    ListItem(
        headlineContent = { Text(text = "Liter") },
        trailingContent = { Text(text = "Liter L l litre") }
    )
}

@Preview
@Composable
private fun DefaultPreview() {
    AutocompleteList(
        entries = listOf(Unit())
    )
}