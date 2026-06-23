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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.ui.PreviewData
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
    activeCalculationIdx: Int?,
    activeCalculationInput: TextFieldValue,
    activeCalculationParsed: String,
    activeCalculationResult: String,
    interceptKeyboard: Boolean,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    onActiveCalculationChanged: (Int) -> Unit = {},
    onDeleteClick: (CalculationHistoryItemData) -> Unit = {},
) {

    val fadeWidth = 20.dp
    val fadeWidthPx = fadeWidth.toFloatPx()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(calculations.size) {
        if (calculations.isNotEmpty()) {
            scrollState.animateScrollToItem(0)
        }
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = scrollState,
            verticalArrangement = Arrangement.Bottom,
            reverseLayout = true,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(brush = Brush.verticalGradient(0f to Color.Transparent, 1f to Color.White, startY = 0f, endY = fadeWidthPx), blendMode = BlendMode.DstIn)
                    drawRect(brush = Brush.verticalGradient(0f to Color.Transparent, 1f to Color.White, startY = size.height, endY = size.height-fadeWidthPx), blendMode = BlendMode.DstIn)
                },
        ) {
            item {
                Spacer(Modifier.height(fadeWidth))
            }
            calculations.sortedBy { it.sortIndex }.withIndex().reversed().forEach { (i, calculation) ->
                    item(key = calculation.id) {
                        CalculationHistoryItem2(
                            calculationNumber = i+1,
                            input = if (calculation.id==activeCalculationIdx) activeCalculationInput else TextFieldValue(calculation.input),
                            parsed = if (calculation.id==activeCalculationIdx)  activeCalculationParsed else calculation.parsed,
                            result = if (calculation.id==activeCalculationIdx) activeCalculationResult else calculation.result,
                            index = i,
                            count = calculations.size,
                            expanded = calculation.id == activeCalculationIdx,
                            onClick = { onActiveCalculationChanged(calculation.id) },
                            onDeleteClick = { onDeleteClick(calculation) },
                            interceptKeyboard = interceptKeyboard,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            item {
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

@Preview()
@Composable
private fun DefaultPreview() {
    CalculationHistoryList(
        calculations = PreviewData.calculationList,
        activeCalculationIdx = 1,
        activeCalculationInput = TextFieldValue("1+1"),
        activeCalculationParsed = "1+1",
        activeCalculationResult = "2",
        interceptKeyboard = false
    )
}