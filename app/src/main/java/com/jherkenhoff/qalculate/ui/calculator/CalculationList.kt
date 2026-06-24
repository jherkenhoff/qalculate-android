package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.PreviewData
import com.jherkenhoff.qalculate.ui.common.DelayedAnimatedVisibility
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.abs

private fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculationList(
    calculations: List<CalculationHistoryItemData>,
    activeCalculationIdx: Int,
    activeCalculationInput: TextFieldValue,
    activeCalculationParsed: String,
    activeCalculationResult: String,
    interceptKeyboard: Boolean,
    userPreferences: UserPreferences,
    modifier: Modifier = Modifier,
    onActiveCalculationChanged: (Int) -> Unit = {},
    onDeleteClick: (CalculationHistoryItemData) -> Unit = {},
    onActiveCalculationInputChange: (TextFieldValue) -> Unit = {},
    onUserpreferencesChanged: (UserPreferences) -> Unit = {},
) {
    val lazyListState = rememberLazyListState()

    val reorderableListState = rememberReorderableLazyListState(lazyListState) { from, to ->
    }

    fun getListIdx(i: Int): Int {
        return calculations.lastIndex - i + 1 // Plus one, because of the "add calculation" button on the bottom of the list
    }

    val fabRowHeight = 60.dp
    val fabRowHeightPx = fabRowHeight.toIntPx()

    val fadeWidth = fabRowHeight
    val fadeWidthPx = fadeWidth.toFloatPx()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(calculations.size) {
        if (calculations.isNotEmpty()) {
            lazyListState.animateScrollToItem(calculations.lastIndex)
        }
    }

    val isActiveCalculationVisible = remember(lazyListState.layoutInfo.visibleItemsInfo) {
        return@remember lazyListState.layoutInfo.visibleItemsInfo.any {
            if (it.index == getListIdx(activeCalculationIdx)) {
                if (it.offset < 0)
                    return@any false // Active item is clipped on the bottom
                else if (it.offset + it.size > lazyListState.layoutInfo.viewportSize.height)
                    return@any false // Active item is clipped on the top
                else
                    return@any true // Active item is fully visible
            } else
                return@any false // Active item is not visible at all
        }
    }

    val snapThreshold = 200

    suspend fun scrollToActive() {
        lazyListState.animateScrollToItem(
            index =getListIdx(activeCalculationIdx),
            scrollOffset = -fabRowHeightPx
        )
    }

    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (!lazyListState.isScrollInProgress) {
            if (!lazyListState.canScrollBackward) {
                return@LaunchedEffect
            }

            val target = lazyListState.layoutInfo.visibleItemsInfo
                .find { it.index == getListIdx(activeCalculationIdx) }

            if (target != null) {
                val distance = target.offset - fabRowHeightPx

                if (distance == 0) {
                    return@LaunchedEffect
                } else if (abs(distance) < snapThreshold) {
                    scrollToActive()
                }
            }
        }
    }

    LaunchedEffect(activeCalculationIdx) {
        scrollToActive()
    }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = lazyListState,
            verticalArrangement = Arrangement.Bottom,
            reverseLayout = true,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(brush = Brush.verticalGradient(0f to Color.Transparent, 1f to Color.White, startY = size.height, endY = size.height-fadeWidthPx), blendMode = BlendMode.DstIn)
                },
        ) {
            item {
                IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Default.Add, null)
                }
            }
            calculations.sortedBy { it.sortIndex }.withIndex().reversed().forEach { (i, calculation) ->
                    item(key = calculation.id) {
                        ReorderableItem(
                            reorderableListState,
                            key = calculation.id,
                            animateItemModifier = Modifier
                        ) { isDragging ->
                            SharedTransitionLayout() {
                                AnimatedContent(i == activeCalculationIdx) { isExpanded ->
                                    if (isExpanded) {
                                        ActiveCalculationListItem(
                                            i+1,
                                            activeCalculationInput,
                                            activeCalculationParsed,
                                            activeCalculationResult,
                                            interceptKeyboard,
                                            this@SharedTransitionLayout,
                                            this@AnimatedContent,
                                            userPreferences = userPreferences,
                                            onInputChange = onActiveCalculationInputChange,
                                            onDeleteClick = { onDeleteClick(calculation) },
                                            onUserpreferencesChanged = onUserpreferencesChanged,
                                            modifier = Modifier.padding(vertical = 2.dp)//.longPressDraggableHandle()
                                        )
                                    } else {
                                        PassiveCalculationListItem(
                                            i+1,
                                            calculation.input,
                                            calculation.result,
                                            topRounded = i == 0,
                                            bottomRounded = i == calculations.size-1,
                                            this@SharedTransitionLayout,
                                            this@AnimatedContent,
                                            onClick = { onActiveCalculationChanged(i) },
                                            onDeleteClick = { onDeleteClick(calculation) },
                                            modifier = Modifier.padding(vertical = 2.dp)//.longPressDraggableHandle()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth().height(fabRowHeight)
        ) {
            AnimatedVisibility(lazyListState.canScrollBackward && lazyListState.lastScrolledBackward) {
                SmallFloatingActionButton(
                    onClick = { coroutineScope.launch { lazyListState.animateScrollToItem(0) } },
                ) {
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = null)
                }
            }

            AnimatedVisibility(!isActiveCalculationVisible) {
                SmallFloatingActionButton(
                    onClick = { coroutineScope.launch { scrollToActive() }},
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Input, contentDescription = null)
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
    SmallFloatingActionButton(
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
    var activeIdx by remember { mutableIntStateOf(1) }

    CalculationList(
        calculations = PreviewData.calculationList,
        activeCalculationIdx = activeIdx,
        activeCalculationInput = TextFieldValue("1+1"),
        activeCalculationParsed = "1+1",
        activeCalculationResult = "2",
        interceptKeyboard = false,
        userPreferences = UserPreferences(),
        onActiveCalculationChanged = { activeIdx = it }
    )
}