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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.Calculation
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.UUID


private fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculationList(
    calculations: Map<UUID, Calculation>,
    focusedCalculationUuid: UUID?,
    modifier: Modifier = Modifier,
    onInputFieldValueChange: (UUID, TextFieldValue) -> Unit = {_, _ -> },
    onDeleteClick: (UUID) -> Unit = {},
    onSubmit: (UUID) -> Unit = {},
    onCalculationFocusChange: (UUID) -> Unit = {}
) {

    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(calculations.size) {
        scrollState.animateScrollToItem(calculations.size)
    }

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = scrollState,
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
        ) {
            calculations.entries.groupBy { it.value.creationTime.toLocalDate() }
                .map { (id, list) ->
                    stickyHeader{
                        val dayString = when (id) {
                            LocalDate.now() -> "Today"
                            LocalDate.now().minusDays(1) -> "Yesterday"
                            else -> id.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                        }
                        CalculationDivider(text = dayString)
                    }
                    list.forEach { entry ->
                        val isExpandedItem = entry.key == focusedCalculationUuid
                        item(key = entry.key) {
                            CalculationItem(
                                entry.value.input,
                                entry.value.parsed,
                                entry.value.result,
                                expanded = isExpandedItem,
                                onFocusChange = { if (it.isFocused) onCalculationFocusChange(entry.key)},
                                onInputFieldValueChange = { onInputFieldValueChange(entry.key, it) },
                                onDeleteClick = { onDeleteClick(entry.key) },
                                onSubmit = { onSubmit(entry.key) },
                                modifier = Modifier.animateItem()
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
            visible = !isScrolledToTheEnd,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it/2 }),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            JumpToBottomButton(
                onClick = {
                    scope.launch {
                        scrollState.animateScrollToItem(calculations.size)
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

    val testCalculationHistory = mapOf(
        UUID.randomUUID() to Calculation(
            LocalDateTime.now().minusDays(10),
            LocalDateTime.now().minusDays(10),
            TextFieldValue("1m + 1m"),
            "1 m + 1 m",
            "2 m"
        ),
        UUID.randomUUID() to Calculation(
            LocalDateTime.now().minusDays(10),
            LocalDateTime.now().minusDays(10),
            TextFieldValue("1m + 1m"),
            "1 m + 1 m",
            "2 m"
        )
    )

    CalculationList(
        testCalculationHistory,
        null
    )
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    CalculationList(
        emptyMap(),
        null
    )
}