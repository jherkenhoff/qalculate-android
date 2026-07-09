package com.jherkenhoff.qalculate.ui.calculator

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.PreviewData
import com.jherkenhoff.qalculate.ui.common.DelayedAnimatedVisibility
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableListState
import sh.calvin.reorderable.rememberReorderableLazyListState
import kotlin.math.abs

private fun LazyListState.isScrolledToTheEnd() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1

class CalculationListState(
    internal val lazyListState: LazyListState
) {
    internal var activeCalculationOffsetPx: Int = 0

    internal val activeCalculationLazyListIdx = mutableIntStateOf(0)
    internal val _isActiveCalculationSnapped = mutableStateOf(false)

    val isActiveCalculationSnapped by _isActiveCalculationSnapped

    internal val _animationInProgress = mutableStateOf(false)
    val animationInProgress by _animationInProgress

    val isFreeScrolling by derivedStateOf {
        !animationInProgress && !isActiveCalculationSnapped
    }

    suspend fun animateScrollToActiveCalculation() {
        _animationInProgress.value = true
        lazyListState.animateScrollToItem(activeCalculationLazyListIdx.intValue, -activeCalculationOffsetPx)
        _animationInProgress.value = false
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
    calculations: List<CalculationHistoryItemData>,
    activeCalculationIdx: Int,
    calculationListState: CalculationListState,
    activeCalculationInput: TextFieldValue,
    activeCalculationParsed: String,
    activeCalculationResult: String,
    interceptKeyboard: Boolean,
    userPreferences: UserPreferences,
    modifier: Modifier = Modifier,
    padding: Dp = 0.dp,
    onActiveCalculationChanged: (Int) -> Unit = {},
    onDeleteClick: (CalculationHistoryItemData) -> Unit = {},
    onActiveCalculationInputChange: (TextFieldValue) -> Unit = {},
    onUserpreferencesChanged: (UserPreferences) -> Unit = {},
) {
    calculationListState.activeCalculationOffsetPx = (padding + 20.dp).toIntPx()

    val reorderableListState = rememberReorderableLazyListState(calculationListState.lazyListState) { from, to -> }

    calculationListState.activeCalculationLazyListIdx.intValue = calculations.lastIndex - activeCalculationIdx + 1

    val isActiveCalculationSnapped by remember(activeCalculationIdx) {
        derivedStateOf {
            calculationListState.lazyListState.layoutInfo.visibleItemsInfo.any {
                (it.index == calculationListState.activeCalculationLazyListIdx.intValue) && (it.offset == calculationListState.activeCalculationOffsetPx)
            }
        }
    }

    LaunchedEffect(calculationListState.lazyListState, activeCalculationIdx) {
        snapshotFlow {
            calculationListState.lazyListState.layoutInfo.visibleItemsInfo.any {
                (it.index == calculationListState.activeCalculationLazyListIdx.intValue) && (it.offset == calculationListState.activeCalculationOffsetPx)
            }
        }.distinctUntilChanged()
            .collect {
                Log.d("Moin", it.toString())
                calculationListState._isActiveCalculationSnapped.value = it
            }
    }

    val coroutineScope = rememberCoroutineScope()

    val passiveCalculationItemsAlpha by animateFloatAsState(if (calculationListState.isFreeScrolling) 1f else 0.5f)

    val snapThreshold = 200

    LaunchedEffect(calculationListState.lazyListState.isScrollInProgress) {
        if (!calculationListState.lazyListState.isScrollInProgress) {
            if (!calculationListState.lazyListState.canScrollBackward || !calculationListState.lazyListState.canScrollForward) {
                // Don't snap if scrolled all the way to the start or end
                return@LaunchedEffect
            }

            val target = calculationListState.lazyListState.layoutInfo.visibleItemsInfo
                .find { it.index == calculationListState.activeCalculationLazyListIdx.intValue }

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

    LaunchedEffect(activeCalculationIdx) {
        calculationListState.animateScrollToActiveCalculation()
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
            Column {
                IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Default.Add, null)
                }
                Spacer(Modifier.height(padding))
            }
        }
        calculations.sortedBy { it.sortIndex }.withIndex().reversed().forEach { (i, calculation) ->
            val isFirstItem = i == 0
            val isLastItem = i == calculations.lastIndex

            item(key = calculation.id) {
                ReorderableItem(
                    reorderableListState,
                    key = calculation.id,
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
                                        onClick = { coroutineScope.launch { calculationListState.animateScrollToActiveCalculation() }},
                                        onUserpreferencesChanged = onUserpreferencesChanged,
                                        modifier = Modifier.padding(vertical = 2.dp)//.longPressDraggableHandle()
                                    )
                                } else {
                                    PassiveCalculationListItem(
                                        i+1,
                                        calculation.input,
                                        calculation.result,
                                        topRounded = isFirstItem,
                                        bottomRounded = isLastItem,
                                        this@SharedTransitionLayout,
                                        this@AnimatedContent,
                                        onClick = { onActiveCalculationChanged(i) },
                                        onDeleteClick = { onDeleteClick(calculation) },
                                        modifier = Modifier.padding(vertical = 2.dp).alpha(passiveCalculationItemsAlpha)//.longPressDraggableHandle()
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
    var activeIdx by remember { mutableIntStateOf(1) }
    val calculationListState = rememberCalculationListState()

    CalculationList(
        calculations = PreviewData.calculationList,
        calculationListState = calculationListState,
        activeCalculationIdx = activeIdx,
        activeCalculationInput = TextFieldValue("1+1"),
        activeCalculationParsed = "1+1",
        activeCalculationResult = "2",
        interceptKeyboard = false,
        userPreferences = UserPreferences(),
        onActiveCalculationChanged = { activeIdx = it }
    )
}