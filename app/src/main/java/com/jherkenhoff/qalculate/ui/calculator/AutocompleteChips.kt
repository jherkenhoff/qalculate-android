package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowOverflowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.domain.AutocompleteItem

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AutocompleteChips(
    entries: () -> List<AutocompleteItem>,
    modifier: Modifier = Modifier,
    onEntryClick: (String) -> Unit = {}
) {
    var maxLines by remember { mutableIntStateOf(2) }

    val moreOrCollapseIndicator = @Composable { scope: FlowRowOverflowScope ->
        val remainingItems = entries().size - 0
        AssistChip(
            label = { Text(if (remainingItems == 0) "Less" else "+$remainingItems", style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold))},
            onClick = {
                if (remainingItems == 0) {
                    maxLines = 2
                } else {
                    maxLines += 5
                }
            }
        )
    }

    ContextualFlowRow(
        itemCount = entries().size,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.Center,
        maxLines = maxLines,
        overflow = ContextualFlowRowOverflow.expandOrCollapseIndicator(
            minRowsToShowCollapse = 4,
            expandIndicator = moreOrCollapseIndicator,
            collapseIndicator = moreOrCollapseIndicator
        ),
        modifier = modifier.padding(horizontal = 8.dp)
    ) { index ->
        SuggestionChip(
            onClick = { onEntryClick(entries()[index].abbreviation) },
            label = { Text(entries().getOrNull(index)?.title?:"") }, // Null handling is necessary due to a bug in ContextualFlowRow: https://issuetracker.google.com/issues/358379365
        )
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
    AutocompleteChips(
        { list }
    )
}
@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    AutocompleteChips(
        { emptyList() }
    )
}