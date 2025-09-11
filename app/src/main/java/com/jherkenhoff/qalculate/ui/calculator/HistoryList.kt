package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.LocalDateTime


private fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HistroyList(
    calculationHistory: List<CalculationHistoryItem>,
    modifier: Modifier = Modifier,
    onTextToInput: (String) -> Unit = {}
) {

    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(calculationHistory.size) {
        scrollState.animateScrollToItem(calculationHistory.size)
    }

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            state = scrollState,
            modifier = Modifier.fillMaxWidth(),
        ) {
            calculationHistory
                .groupBy {
                    // Parse ISO string to LocalDate
                    try {
                        LocalDateTime.parse(it.time).toLocalDate()
                    } catch (e: Exception) {
                        LocalDate.MIN
                    }
                }
                .toSortedMap(compareByDescending { it })
                .map { (id, list) ->
                    stickyHeader {
                        val dayString = when (id) {
                            LocalDate.now() -> "Today"
                            LocalDate.now().minusDays(1) -> "Yesterday"
                            else -> id.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                        }
                        CalculationDivider(text = dayString)
                    }
                    list.forEach {
                        item {
                            HistoryItem(
                                inputText = it.input,
                                parsedText = it.parsed,
                                resultText = it.result,
                                onTextToInput = onTextToInput,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
        }

        val isScrolledToTheEnd by remember {
            derivedStateOf {
                scrollState.isScrolledToTheEnd()
            }
        }

        AnimatedVisibility(
            visible = !isScrolledToTheEnd && calculationHistory.isNotEmpty(),
            enter = fadeIn() + slideInVertically(initialOffsetY = { it/2 }),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            JumpToBottomButton(
                onClick = {
                    scope.launch {
                        scrollState.animateScrollToItem(calculationHistory.size)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Text("Empty")
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
    val now = LocalDateTime.now()
    val testCalculationHistory = listOf(
        CalculationHistoryItem(
            now.minusDays(10).toString(),
            "1 kilometer + 5 meter",
            "1.005 m",
            "2 m"
        ),
        CalculationHistoryItem(
            now.minusDays(1).toString(),
            "1m + 1m",
            "1 m + 1 m",
            "2 m"
        ),
        CalculationHistoryItem(
            now.minusDays(1).minusHours(2).toString(),
            "1m + 1m",
            "1 m + 1 m",
            "2 m"
        ),
        CalculationHistoryItem(
            now.minusMinutes(20).toString(),
            "1km + 5m",
            "1 kilometer + 5 meter",
            "1.005 m",
        )
    )
    HistroyList(
        testCalculationHistory
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    HistroyList (
        emptyList()
    )
}