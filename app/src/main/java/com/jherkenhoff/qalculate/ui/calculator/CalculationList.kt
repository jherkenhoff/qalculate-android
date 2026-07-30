package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.PreviewData
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.abs

private fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

class CalculationListState(
    internal val lazyListState: LazyListState
) {
    internal var activeCalculationOffsetPx: Int = 0

    internal val activeCalculationLazyListIdx = mutableStateOf<Int?>(null)
    internal val _isActiveCalculationSnapped = mutableStateOf(false)
    val isActiveCalculationSnapped by _isActiveCalculationSnapped

    private var scrollingToActiveCalculation by mutableStateOf(false)

    val isFreeScrolling by derivedStateOf {
        !scrollingToActiveCalculation && !isActiveCalculationSnapped
    }

    suspend fun animateScrollToActiveCalculation() {
        val idx = activeCalculationLazyListIdx.value ?: return

        scrollingToActiveCalculation = true
        lazyListState.animateScrollToItem(idx, -activeCalculationOffsetPx)
        scrollingToActiveCalculation = false
    }

    suspend fun animateScrollToLastCalculation() {
        scrollingToActiveCalculation = false
        lazyListState.animateScrollToItem(0, -activeCalculationOffsetPx)
    }
}

@Composable
fun rememberCalculationListState(): CalculationListState {
    val lazyListState = rememberLazyListState()
    return remember { CalculationListState(lazyListState) }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculationList(
    calculationListData: CalculationListData,
    calculationListState: CalculationListState,
    activeCalculationInput: TextFieldValue,
    activeCalculationParsed: String,
    activeCalculationResult: String,
    interceptKeyboard: Boolean,
    userPreferences: UserPreferences,
    modifier: Modifier = Modifier,
    padding: Dp = 0.dp,
    onActiveCalculationChanged: (Long) -> Unit = {},
    onActiveCalculationInputChange: (TextFieldValue) -> Unit = {},
    onUserpreferencesChanged: (UserPreferences) -> Unit = {},
    onCalculationDragged: (Int, Int) -> Unit = { fromIdx, toIdx -> },
    onCalculationDragStopped: () -> Unit = {},
    onAction: (CalculatorAction) -> Unit = {}
) {
    val calculations = calculationListData.items
    val activeCalculationId = calculationListData.activeCalculationId
    val activeCalculationIdx = calculations.indexOfFirst { it.id == activeCalculationId }.takeIf { it != -1 }

    val activeCalculationOffset = padding + 20.dp
    calculationListState.activeCalculationOffsetPx = activeCalculationOffset.toIntPx()

    val hapticFeedback = LocalHapticFeedback.current
    val reorderableListState = rememberReorderableLazyListState(calculationListState.lazyListState) { from, to ->
        onCalculationDragged(calculations.lastIndex - from.index + 1, calculations.lastIndex - to.index + 1)

        hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
    }

    calculationListState.activeCalculationLazyListIdx.value = activeCalculationIdx?.let { idx -> calculations.lastIndex - idx + 1 }

    // Check if the active calculation is in its "snapped position" and update the
    // calculationListState if this snapping state changed
    LaunchedEffect(calculationListState.lazyListState, activeCalculationIdx) {
        snapshotFlow {
            calculationListState.lazyListState.layoutInfo.visibleItemsInfo.any {
                (it.index == calculationListState.activeCalculationLazyListIdx.value) && (it.offset == calculationListState.activeCalculationOffsetPx)
            }
        }
            .distinctUntilChanged()
            .collect { calculationListState._isActiveCalculationSnapped.value = it }
    }

    val coroutineScope = rememberCoroutineScope()

    val snapThreshold = 200

    // Snapping behaviour
    LaunchedEffect(calculationListState.lazyListState.isScrollInProgress) {
        if (reorderableListState.isAnyItemDragging) return@LaunchedEffect // Don't snap during reordering

        if (!calculationListState.lazyListState.isScrollInProgress) {
            if (!calculationListState.lazyListState.canScrollBackward || !calculationListState.lazyListState.canScrollForward) {
                // Don't snap if scrolled all the way to the start or end
                return@LaunchedEffect
            }

            val target = calculationListState.lazyListState.layoutInfo.visibleItemsInfo
                .find { it.index == calculationListState.activeCalculationLazyListIdx.value }

            if (target != null) {
                val distance = target.offset - calculationListState.activeCalculationOffsetPx

                if (distance == 0) {
                    return@LaunchedEffect
                } else if (abs(distance) < snapThreshold) {
                    calculationListState.animateScrollToActiveCalculation()
                }
            }
        }
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        state = calculationListState.lazyListState,
        verticalArrangement = Arrangement.Bottom,
        reverseLayout = true,
        modifier = modifier
            .fillMaxWidth()
//                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
//                .drawWithContent {
//                    drawContent()
//                    drawRect(brush = Brush.verticalGradient(0f to Color.Transparent, 1f to Color.White, startY = size.height, endY = size.height-fadeWidthPx), blendMode = BlendMode.DstIn)
//                },
    ) {
        item {
            Spacer(Modifier.height(activeCalculationOffset))
        }
        calculations.withIndex().reversed().forEach { (i, calculation) ->
            val isFirstItem = i == 0
            val isLastItem = i == calculations.lastIndex

            item(key = if(calculation.id == activeCalculationId) "active" else calculation.id) {
                ReorderableItem(
                    reorderableListState,
                    key = if(calculation.id == activeCalculationId) "active" else calculation.id,
                    animateItemModifier = Modifier
                ) { isDragging ->
                    Box(
                        Modifier.then( if (isFirstItem) Modifier.fillParentMaxSize() else Modifier ),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        SharedTransitionLayout() {
                            AnimatedContent(i == activeCalculationIdx) { isExpanded ->
                                if (isExpanded) {
                                    ActiveCalculationListItem(
                                        calculation.id,
                                        activeCalculationInput,
                                        activeCalculationParsed,
                                        activeCalculationResult,
                                        calculation.executionOrderNumber,
                                        interceptKeyboard,
                                        this@SharedTransitionLayout,
                                        this@AnimatedContent,
                                        userPreferences = userPreferences,
                                        onInputChange = onActiveCalculationInputChange,
                                        onClick = { coroutineScope.launch {
                                            calculationListState.animateScrollToActiveCalculation()
                                        }},
                                        onUserpreferencesChanged = onUserpreferencesChanged,
                                        onDragStopped = onCalculationDragStopped,
                                        onAction = onAction,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                } else {
                                    PassiveCalculationListItem(
                                        calculation.id,
                                        calculation.input,
                                        calculation.result,
                                        calculation.executionOrderNumber,
                                        topRounded = isFirstItem,
                                        bottomRounded = isLastItem,
                                        this@SharedTransitionLayout,
                                        this@AnimatedContent,
                                        onClick = { onActiveCalculationChanged(calculation.id) },
                                        onAction = onAction,
                                        modifier = Modifier.padding(vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview()
@Composable
private fun DefaultPreview() {
    var calculationListData by remember { mutableStateOf(CalculationListData(PreviewData.calculationList, 0)) }
    val calculationListState = rememberCalculationListState()

    CalculationList(
        calculationListData = calculationListData,
        calculationListState = calculationListState,
        activeCalculationInput = TextFieldValue("1+1"),
        activeCalculationParsed = "1+1",
        activeCalculationResult = "2",
        interceptKeyboard = false,
        userPreferences = UserPreferences(),
        onActiveCalculationChanged = { calculationListData = calculationListData.copy(activeCalculationId = it) }
    )
}