package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculationList(
    calculationHistory: List<CalculationHistoryItem>,
    currentParsed: () -> String,
    currentResult: () -> String,
    modifier: Modifier = Modifier,
    bottomSpacing: Dp = 0.dp,
) {

    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    Box(
        modifier = modifier
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.Bottom,
            state = scrollState,
            modifier = Modifier.fillMaxSize()
        ) {
            calculationHistory.groupBy { it.time.toLocalDate() }
                .map { (id, list) ->
                    stickyHeader{
                        val dayString = when (id) {
                            LocalDate.now() -> "Today"
                            LocalDate.now().minusDays(1) -> "Yesterday"
                            else -> id.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                        }
                        CalculationDivider(text = dayString)
                    }
                    list.forEach {
                        it.time.toLocalDate()
                        item() {
                            CalculationListItem(
                                it.parsed,
                                it.result,
                                //modifier = Modifier.animateItemPlacement()
                            )
                        }
                    }
                }
            item {
                CalculationDivider(text = "Now")
            }
            item {
                CalculationListItem(
                    currentParsed(),
                    currentResult()
                )
                Spacer(modifier = Modifier.size(bottomSpacing))
            }
        }


        val isScrolledToTheEnd by remember {
            derivedStateOf {
                scrollState.isScrolledToTheEnd()
            }
        }

        AnimatedVisibility(
            visible = !isScrolledToTheEnd,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it/2 }),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            JumpToBottomButton(
                onClick = {
                    scope.launch {
                        scrollState.animateScrollToItem(calculationHistory.size-1)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp)
                    .padding(bottom = bottomSpacing)
            )
        }
    }
}

@Composable
private fun JumpToBottomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = {},
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

    val testCalculationHistory = listOf(
        CalculationHistoryItem(
            LocalDateTime.now().minusDays(10),
            "1m + 1m",
            "1 m + 1 m",
            "2 m"
        ),
        CalculationHistoryItem(
            LocalDateTime.now().minusDays(1),
            "1m + 1m",
            "1 m + 1 m",
            "2 m"
        ),
        CalculationHistoryItem(
            LocalDateTime.now().minusDays(1).minusHours(2),
            "1m + 1m",
            "1 m + 1 m",
            "2 m"
        ),
        CalculationHistoryItem(
            LocalDateTime.now().minusMinutes(20),
            "1m + 1m",
            "1 m + 1 m",
            "2 m"
        )
    )

    CalculationList(
        testCalculationHistory,
        { "1+1" },
        { "2" }
    )
}