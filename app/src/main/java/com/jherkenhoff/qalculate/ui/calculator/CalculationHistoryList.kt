package com.jherkenhoff.qalculate.ui.calculator


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


private fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculationHistoryList(
    calculations: List<CalculationHistoryItemData>,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    onDeleteClick: (CalculationHistoryItemData) -> Unit = {},
) {
    if (calculations.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Outlined.History, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(10.dp).size(40.dp))
            Text("Calculation history empty", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = scrollState,
            modifier = modifier,
            verticalArrangement = Arrangement.Bottom,
            reverseLayout = true
        ) {
            calculations.groupBy { it.created.toLocalDate() }
                .map { (id, list) ->
                    list.reversed().forEach { entry ->
                        item(key = entry.id) {
                            CalculationHistoryItem(
                                entry.input,
                                entry.parsed,
                                entry.result,
                                onDeleteClick = { onDeleteClick(entry) },
                            )
                        }
                    }
                    stickyHeader{
                        val dayString = when (id) {
                            LocalDate.now() -> "Today"
                            LocalDate.now().minusDays(1) -> "Yesterday"
                            else -> id.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                        }
                        CalculationDivider(text = dayString)
                    }
                }
        }
    }
}

@Composable
private fun JumpToBottomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val testCalculationHistoryItemHistory = listOf(
        CalculationHistoryItemData(
            0, "1+1", "1+1", "2", LocalDateTime.now()
        ),
        CalculationHistoryItemData(
            1, "2+2", "2+2", "4", LocalDateTime.now()
        )
    )

    CalculationHistoryList(
        testCalculationHistoryItemHistory
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    CalculationHistoryList(
        emptyList()
    )
}