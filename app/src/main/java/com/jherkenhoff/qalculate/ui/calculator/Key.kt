package com.jherkenhoff.qalculate.ui.calculator

import android.view.ViewConfiguration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.jherkenhoff.qalculate.model.KeySpec
import com.jherkenhoff.qalculate.model.Action
import com.jherkenhoff.qalculate.model.ActionLabel
import com.jherkenhoff.qalculate.model.KeyRole
import com.jherkenhoff.qalculate.model.Keys
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.roundToInt


object KeyDefaults {
    val Shape = RoundedCornerShape(6.dp)
}

@Composable
fun Key(
    keySpec: KeySpec,
    modifier: Modifier = Modifier,
    shape: Shape = KeyDefaults.Shape,
    onKeyAction: (Action) -> Unit = {}
) {

    val containerColor = when (keySpec.role) {
        KeyRole.NUMBER -> MaterialTheme.colorScheme.surfaceBright
        KeyRole.OPERATOR -> MaterialTheme.colorScheme.surfaceContainerHigh
        KeyRole.SYSTEM -> MaterialTheme.colorScheme.tertiaryContainer
    }

    val labelColor = when (keySpec.role) {
        KeyRole.NUMBER -> MaterialTheme.colorScheme.onSurface
        KeyRole.OPERATOR -> MaterialTheme.colorScheme.onSurface
        KeyRole.SYSTEM -> MaterialTheme.colorScheme.onTertiaryContainer
    }

    when (keySpec) {
        is KeySpec.DefaultKeySpec -> DefaultKey(
            keySpec,
            onKeyAction = onKeyAction,
            labelColor = labelColor,
            containerColor = containerColor,
            shape = shape,
            modifier = modifier
        )
        is KeySpec.SelectorKeySpec -> SelectorKey(
            keySpec,
            onKeyAction = onKeyAction,
            labelColor = labelColor,
            containerColor = containerColor,
            shape = shape,
            modifier = modifier
        )

        is KeySpec.CornerDragKeySpec -> CornerDragKey(
            keySpec,
            onKeyAction = onKeyAction,
            labelColor = labelColor,
            containerColor = containerColor,
            shape = shape,
            modifier = modifier
        )
    }
}

@Composable
fun DefaultKey(
    keySpec: KeySpec.DefaultKeySpec,
    modifier: Modifier = Modifier,
    shape: Shape = KeyDefaults.Shape,
    onKeyAction: (Action) -> Unit = {},
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()

    var showPopup by remember { mutableStateOf(false) }
    var popupSecondary by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.clickable { }
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown()
                        showPopup = true
                        popupSecondary = false

                        var isLongPress = false

                        val longPressDetectionJob = coroutineScope.launch {
                            if (keySpec.longClickAction != null) {
                                delay(longPressTimeout)
                                popupSecondary = true
                                isLongPress = true
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            }
                        }

                        val up = waitForUpOrCancellation()

                        showPopup = false
                        longPressDetectionJob.cancel()

                        if ( up?.changedToUp() == true) {
                            if (isLongPress) {
                                keySpec.longClickAction?.let {
                                    //haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onKeyAction(it)
                                }
                            } else {
                                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                                onKeyAction(keySpec.clickAction)
                            }
                        }

                    }
                }
            }
    ) {

        if (showPopup) {
            Layout(
                content = {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape,
                        shadowElevation = 3.dp,
                        modifier = Modifier.defaultMinSize(32.dp, 32.dp),
                    ) {
                        Box(
                            modifier = Modifier.wrapContentSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(popupSecondary) {
                                val label = if (it && keySpec.longClickAction != null) keySpec.longClickAction.popupLabel else keySpec.clickAction.popupLabel
                                label?.let {
                                    KeyLabel(
                                        label = label,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                },
            ) { measurables, constraints ->
                val placeable = measurables.first().measure(constraints)
                layout(width = 0, height = 0) {
                    placeable.placeRelative(x = (constraints.maxWidth / 2 - placeable.width / 2), y = -placeable.height - 8.dp.roundToPx())
                }
            }
        }

        Surface(
            shape = shape,
            color = containerColor,
            modifier = modifier.fillMaxSize().align(Alignment.Center)
        ) {
            Box {
                KeyLabel(
                    label = keySpec.clickAction.label,
                    color = labelColor,
                    modifier = Modifier.align(Alignment.Center)
                )
                keySpec.longClickAction?.let {
                    KeyLabel(
                        label = it.label,
                        color = labelColor.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(if (it.label != null) Alignment.TopEnd else Alignment.TopEnd)
                    )
                }
            }
        }
    }
}



@Composable
fun CornerDragKey(
    keySpec: KeySpec.CornerDragKeySpec,
    modifier: Modifier = Modifier,
    shape: Shape = KeyDefaults.Shape,
    onKeyAction: (Action) -> Unit = {},
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val dragThreshold = (10.dp).toFloatPx()

    var dragOffset by remember { mutableStateOf(Offset(0f, 0f)) }

    var selectedAction by remember { mutableStateOf<Action?>(null) }

    Box(
        modifier = modifier.pointerInput(Unit) {
            awaitPointerEventScope {
                while (true) {
                    val down = awaitFirstDown()
                    selectedAction = keySpec.centerAction
                    dragOffset = Offset(0f, 0f)

                    drag(down.id) { change ->
                        dragOffset += change.positionChange()

                        var newSelectedAction : Action = keySpec.centerAction

                        if (dragOffset.getDistance() >= dragThreshold) {
                            if (dragOffset.x > 0 && dragOffset.y < 0 && keySpec.topRightAction != null) {
                                newSelectedAction = keySpec.topRightAction
                            } else if (dragOffset.x < 0 && dragOffset.y < 0 && keySpec.topLeftAction != null) {
                                newSelectedAction = keySpec.topLeftAction
                            } else if (dragOffset.x > 0 && dragOffset.y > 0 && keySpec.bottomRightAction != null) {
                                newSelectedAction = keySpec.bottomRightAction
                            } else if (dragOffset.x < 0 && dragOffset.y > 0 && keySpec.bottomLeftAction != null) {
                                newSelectedAction = keySpec.bottomLeftAction
                            }
                        }

                        if (newSelectedAction != selectedAction) {
                            haptic.performHapticFeedback(HapticFeedbackType.SegmentTick)
                        }
                        selectedAction = newSelectedAction

                        change.consume()
                    }

                    haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                    selectedAction?.let(onKeyAction)
                    selectedAction = null
                }
            }
        }
    ) {

        selectedAction?.let {
            Layout(
                content = {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape,
                        shadowElevation = 3.dp,
                        modifier = Modifier.defaultMinSize(32.dp, 32.dp),
                    ) {
                        Box(
                            modifier = Modifier.wrapContentSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            AnimatedContent(it) {
                                KeyLabel(
                                    label = it.popupLabel,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                },
            ) { measurables, constraints ->
                val placeable = measurables.first().measure(constraints)
                layout(width = 0, height = 0) {
                    placeable.placeRelative(x = (constraints.maxWidth / 2 - placeable.width / 2), y = -placeable.height - 8.dp.roundToPx())
                }
            }
        }

        Surface(
            shape = shape,
            color = containerColor,
            modifier = modifier.fillMaxSize().align(Alignment.Center)
        ) {
            Box {
                KeyLabel(
                    label = keySpec.centerAction.label,
                    color = labelColor,
                    modifier = Modifier.align(Alignment.Center)
                )
                keySpec.topLeftAction?.let {
                    KeyLabel(
                        label = it.label,
                        color = labelColor.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                }
                keySpec.topRightAction?.let {
                    KeyLabel(
                        label = it.label,
                        color = labelColor.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
                keySpec.bottomLeftAction?.let {
                    KeyLabel(
                        label = it.label,
                        color = labelColor.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.BottomStart)
                    )
                }
                keySpec.bottomRightAction?.let {
                    KeyLabel(
                        label = it.label,
                        color = labelColor.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            }
        }
    }
}

@Composable
fun SelectorKey(
    keySpec: KeySpec.SelectorKeySpec,
    modifier: Modifier = Modifier,
    shape: Shape = KeyDefaults.Shape,
    onKeyAction: (Action) -> Unit = {},
    labelColor: Color = MaterialTheme.colorScheme.onSurface,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainer
) {
    val nItems = 5 // Number of items visible at once

    val itemHeight = 40.dp
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    val minOffset = -(keySpec.actions.size - nItems/2 - 1) * itemHeightPx
    val maxOffset = (nItems/2) * itemHeightPx

    var isSelecting by remember { mutableStateOf(false) }

    var dragOffset by remember { mutableFloatStateOf(0f) }
    var scrollOffset by remember { mutableFloatStateOf(0f) }

    var selectedAction by remember { mutableStateOf(keySpec.actions[keySpec.initialSelectedIndex]) }

    val coroutineScope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val down = awaitFirstDown()
                        dragOffset = (keySpec.actions.size/2 - keySpec.initialSelectedIndex - 1) * itemHeightPx
                        scrollOffset = dragOffset

                        val touchSlop = viewConfiguration.touchSlop
                        val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()

                        val longPressDetectionJob = coroutineScope.launch {
                            delay(longPressTimeout)
                            isSelecting = true
                            haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                        }

                        drag(down.id) { change ->
                            val dragAmount = change.positionChange().y

                            if (!isSelecting && abs(dragAmount) > touchSlop) {
                                longPressDetectionJob.cancel()
                                isSelecting = true
                                haptic.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                            }

                            if (isSelecting) {
                                dragOffset = (dragOffset + dragAmount).coerceIn(minOffset, maxOffset)

                                // Translate drag offset to scroll offset and apply non-linear transfer function for snappy feeling
                                val dragOffsetItemIndex = floor(dragOffset/itemHeightPx).toInt()
                                val dragOffsetFractionalIndex = (dragOffset/itemHeightPx - floor(dragOffset/itemHeightPx))
                                scrollOffset = dragOffsetItemIndex*itemHeightPx + EaseInOutQuart.transform(dragOffsetFractionalIndex)*itemHeightPx

                                val selectedIndex = (nItems/2 - scrollOffset / itemHeightPx).roundToInt().coerceIn(0, keySpec.actions.lastIndex)
                                val newSelectedAction = keySpec.actions[selectedIndex]
                                if (newSelectedAction != selectedAction) {
                                    haptic.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
                                }
                                selectedAction = newSelectedAction
                            }

                            change.consume()
                        }
                        longPressDetectionJob.cancel()

                        isSelecting = false
                        haptic.performHapticFeedback(HapticFeedbackType.GestureEnd)
                        onKeyAction(selectedAction)
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // The actual button
        Surface(
            shape = shape,
            color = containerColor,
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Icon(
                    Icons.Default.KeyboardArrowUp,
                    contentDescription = null,
                    tint = labelColor.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.TopCenter).size(12.dp)
                )
                KeyLabel(
                    selectedAction.label,
                    color = labelColor,
                    modifier = Modifier.align(Alignment.Center)
                )
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = labelColor.copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.BottomCenter).size(12.dp)
                )

            }
        }

        if (isSelecting) {
            // The popup
            Popup(alignment = Alignment.TopCenter, offset = IntOffset(0, -250)) {
                Box(
                    Modifier
                        .width(160.dp)
                        .height(itemHeight * nItems)
                        .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(12.dp))
                        .clipToBounds()
                        .padding(vertical = 8.dp)
                ) {
                    // Scrollable content
                    val centerOffsetPx = itemHeightPx * nItems/2
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(unbounded = true, align = Alignment.TopCenter)
                            .offset(y = scrollOffset.toDp()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for ((index, action) in keySpec.actions.withIndex()) {
                            val distanceFromCenter = abs(index*itemHeightPx + scrollOffset - centerOffsetPx + itemHeightPx/2)/itemHeightPx
                            val alpha = 1f - (distanceFromCenter * 0.3f)
                            val scale = 1f - (distanceFromCenter * 0.1f)

                            KeyLabel(
                                action.label,
                                color = Color.White.copy(alpha.coerceIn(0.5f, 1f)),
                                modifier = Modifier.height(itemHeight).scale(scale.coerceIn(0.5f, 1f))
                            )
                        }
                    }

                    // Center highlight line
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {
                        HorizontalDivider()
                        Spacer(Modifier.height(itemHeight))
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun KeyLabel(
    label: ActionLabel?,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.labelLarge,
    modifier: Modifier = Modifier
) {
    when (label) {
        null ->
            Box(
                modifier
                    .padding(2.dp)
                    .size(3.dp)
                    .background(MaterialTheme.colorScheme.secondary, CircleShape)
            )

        is ActionLabel.Text ->
            Text(
                text = label.text,
                color = color,
                style = style,
                modifier = modifier.padding(4.dp, 0.dp)
            )

        is ActionLabel.Icon ->
            Icon(
                label.icon,
                label.description,
                tint = color,
                modifier = modifier.padding(2.dp).size(style.fontSize.toDp())
            )
    }
}


@Preview
@Composable
private fun SingleActionDefaultKeyPreview() {
    Row(Modifier.height(54.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Key(Keys.keySpec0, Modifier.weight(1f))
        Key(Keys.keySpecBackspace, Modifier.weight(1f))
        Key(Keys.keySpecPi, Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun DualActionDefaultKeyPreview() {
    Row(Modifier.height(54.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Key(Keys.keySpecSin, Modifier.weight(1f))
        Key(Keys.keySpecCos, Modifier.weight(1f))
        Key(Keys.keySpecTan, Modifier.weight(1f))
    }
}



@Preview
@Composable
private fun SelectionKeyPreview() {
    Row(Modifier.height(54.dp), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Key(Keys.keySpecSiLength, Modifier.weight(1f))
        Key(Keys.keySpecImperialLength, Modifier.weight(1f))
        Key(Keys.keySpecSiLength, Modifier.weight(1f))
    }
}