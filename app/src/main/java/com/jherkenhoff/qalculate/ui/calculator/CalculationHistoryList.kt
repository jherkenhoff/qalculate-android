package com.jherkenhoff.qalculate.ui.calculator


import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.ui.common.DelayedAnimatedVisibility
import kotlinx.coroutines.launch
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
    val fadeWidth = 60.dp
    val fadeWidthPx = fadeWidth.toFloatPx()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(calculations.size) {
        if (calculations.isNotEmpty()) {
            scrollState.animateScrollToItem(0)
        }
    }

    if (calculations.isEmpty()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(Icons.Default.History, null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text("No calculations yet", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), style = MaterialTheme.typography.titleLarge)
            Text("Your calculation history will appear here", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
        }
    } else {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = modifier
                .fillMaxSize()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(brush = Brush.verticalGradient(0f to Color.Transparent, 1f to Color.White, startY = 0f, endY = fadeWidthPx), blendMode = BlendMode.DstIn)
                },
        ) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                state = scrollState,
                verticalArrangement = Arrangement.Bottom,
                reverseLayout = true,
                modifier = Modifier.fillMaxSize()
            ) {
                item{
                    Spacer(Modifier.height(8.dp))
                }
                calculations.sortedBy { it.created }.reversed().groupBy { it.created.toLocalDate() }
                    .map { (id, list) ->
                        list.forEach { entry ->
                            item(key = entry.id) {
                                CalculationHistoryItem(
                                    entry.input,
                                    entry.parsed,
                                    entry.result,
                                    onDeleteClick = { onDeleteClick(entry) },
                                )
                            }
                        }
                        item {
                            val dayString = when (id) {
                                LocalDate.now() -> "Today"
                                LocalDate.now().minusDays(1) -> "Yesterday"
                                else -> id.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                            }
                            CalculationDivider(text = dayString)
                        }
                    }

                item{
                    Spacer(Modifier.height(fadeWidth))
                }
            }
            DelayedAnimatedVisibility(
                scrollState.canScrollBackward,
                500L,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                JumpToBottomButton(
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollToItem(0)
                        }
                    },
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun CalculationDivider(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 6.dp)
            .height(16.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
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
            1, "2+2", "2+2", "4", LocalDateTime.now().minusMinutes(30)
        ),
        CalculationHistoryItemData(
            2, "2+2", "2+2", "4", LocalDateTime.now().minusDays(1)
        ),
        CalculationHistoryItemData(
            3, "2+2", "2+2", "4", LocalDateTime.now().minusDays(1)
        ),
        CalculationHistoryItemData(
            4, "2+2", "2+2", "4", LocalDateTime.now().minusDays(2)
        ),
        CalculationHistoryItemData(
            5, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
        ),
        CalculationHistoryItemData(
            6, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
        ),
        CalculationHistoryItemData(
            7, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
        ),
        CalculationHistoryItemData(
            8, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
        ),
        CalculationHistoryItemData(
            9, "2+2", "2+2", "4", LocalDateTime.now().minusDays(6)
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