package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.domain.AutocompleteItem


@Composable
fun AutocompleteList(
    autocompleteText: () -> String,
    entries: () -> List<AutocompleteItem>,
    modifier: Modifier = Modifier,
    onEntryClick: (String) -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val dismissState = rememberSwipeToDismissBoxState()

    when (dismissState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {

        }

        else -> {  }
    }

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {},
    ) {
        Card(
            modifier = modifier,
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
        ) {
            Text(
                buildAnnotatedString {
                    append("Suggestions for ")

                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(autocompleteText())
                    }
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp).fillMaxWidth()
            )
            HorizontalDivider()
            LazyColumn(
                reverseLayout = true
            ) {
                for (entry in entries()) {
                    item {
                        ListItem(
                            headlineContent = { Text(entry.title) },
                            trailingContent = { Text(entry.name) },
                            modifier = Modifier.clickable { onEntryClick(entry.abbreviation) },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun DefaultPreview() {
    val list = listOf(
        AutocompleteItem("Tesla", "M", "T"),
        AutocompleteItem("Thomson cross section", "M", "T"),
        AutocompleteItem("Terabyte", "M", "T"),
        AutocompleteItem("Planck temperature", "M", "T"),
    )
    AutocompleteList(
        { "t" },
        { list }
    )
}