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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.ui.common.DelayedAnimatedVisibility
import com.jherkenhoff.qalculate.ui.common.toFloatPx
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
            modifier = modifier.fillMaxSize()
        ) {
            Icon(Icons.Default.History, null, tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Text(stringResource(R.string.history_empty_title), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f), style = MaterialTheme.typography.titleLarge)
            Text(stringResource(R.string.history_empty_subtitle), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
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
                val grouped = calculations.groupBy { it.created.toLocalDate() }
                
                item {
                    Spacer(Modifier.height(fadeWidth))
                }

                grouped.forEach { (date, items) ->
                    items(items, key = { it.id }) { item ->
                        CalculationHistoryItem(
                            input = item.input,
                            parsed = item.parsed,
                            result = item.result,
                            onDeleteClick = { onDeleteClick(item) },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }

                    stickyHeader {
                        val label = when (date) {
                            LocalDate.now() -> stringResource(R.string.history_today)
                            LocalDate.now().minusDays(1) -> stringResource(R.string.history_yesterday)
                            else -> date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                             Surface(
                                shape = MaterialTheme.shapes.small,
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                             ) {
                                 Text(
                                     text = label,
                                     style = MaterialTheme.typography.labelMedium,
                                     modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                 )
                             }
                        }
                    }
                }
            }
        }
    }
}
