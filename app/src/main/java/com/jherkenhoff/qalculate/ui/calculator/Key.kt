package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import android.view.ViewConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.jherkenhoff.qalculate.model.Key
import com.jherkenhoff.qalculate.model.KeyAction
import com.jherkenhoff.qalculate.model.KeyLabel
import com.jherkenhoff.qalculate.model.KeyRole
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt


@Composable
fun Key(
    key: Key,
    modifier: Modifier = Modifier,
    onKeyAction: (KeyAction) -> Unit = {}
) {
    var showPopup by remember { mutableStateOf(false) }

    val containerColor = when (key.role) {
        KeyRole.NUMBER -> MaterialTheme.colorScheme.surfaceBright
        KeyRole.OPERATOR -> MaterialTheme.colorScheme.surfaceContainerHigh
        KeyRole.SYSTEM -> MaterialTheme.colorScheme.secondaryContainer
    }

    val labelColor = when (key.role) {
        KeyRole.NUMBER -> MaterialTheme.colorScheme.onSurface
        KeyRole.OPERATOR -> MaterialTheme.colorScheme.onSurface
        KeyRole.SYSTEM -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    val haptic = LocalHapticFeedback.current

    when (key) {
        is Key.DefaultKey ->
            Box(
                modifier = modifier
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { offset ->
                                showPopup = true
                                haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                                try {
                                    awaitRelease()
                                    key.clickAction?.let { onKeyAction(it) }
                                } finally {
                                    delay(50)
                                    showPopup = false
                                }
                            },
                            onLongPress = {
                                key.longClickAction?.let { onKeyAction(it) }
                            }
                        )
                    }
            ) {

                val popupEnabled = when(key.role) {
                    KeyRole.SYSTEM -> false
                    else -> true
                }

                if (showPopup && popupEnabled) {
                    Layout(
                        content = {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape,
                                shadowElevation = 3.dp,
                                modifier = Modifier.size(38.dp),
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    when (val label = key.clickAction.label) {
                                        is KeyLabel.Blank -> null

                                        is KeyLabel.Text -> Text(
                                            label.text,
                                            color = labelColor,
                                            style = MaterialTheme.typography.labelLarge
                                        )

                                        is KeyLabel.Icon -> Icon(
                                            label.icon,
                                            label.description,
                                            tint = labelColor,
                                            modifier = Modifier.size(MaterialTheme.typography.labelLarge.lineHeight.toDp())
                                        )
                                    }
                                }
                            }
                        }
                    ) { measurables, constraints ->
                        val placeable = measurables.first().measure(constraints)
                        layout(width = 0, height = 0) {
                            placeable.placeRelative(x = (constraints.maxWidth / 2 - placeable.width / 2), y = -placeable.height - 8.dp.roundToPx())
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = containerColor,
                    modifier = modifier.fillMaxSize()
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        when (val label = key.clickAction.label) {
                            is KeyLabel.Blank -> null

                            is KeyLabel.Text -> Text(
                                label.text,
                                color = labelColor,
                                style = MaterialTheme.typography.labelLarge
                            )

                            is KeyLabel.Icon -> Icon(
                                label.icon,
                                label.description,
                                tint = labelColor,
                                modifier = Modifier.size(MaterialTheme.typography.labelLarge.lineHeight.toDp())
                            )
                        }
                    }
                }
            }

        is Key.SelectorKey -> SelectorKey(key, onKeyAction = onKeyAction, modifier = modifier)

    }
}

@Composable
fun SelectorKey(
    key: Key.SelectorKey,
    modifier: Modifier = Modifier,
    onKeyAction: (KeyAction) -> Unit = {},
) {
    val nItems = 5

    val itemHeight = 40.dp
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    val minOffset = -(key.actions.size - nItems/2 - 1) * itemHeightPx
    val maxOffset = (nItems/2) * itemHeightPx

    var isSelecting by remember { mutableStateOf(false) }

    var scrollOffset by remember { mutableFloatStateOf(0f) }

    var selectedAction by remember { mutableStateOf(key.actions[key.initialSelectedIndex]) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val down = awaitFirstDown()
                        scrollOffset = (key.actions.size/2 - key.initialSelectedIndex - 1) * itemHeightPx
                        Log.d("Moni", scrollOffset.toString())

                        val touchSlop = viewConfiguration.touchSlop
                        val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()

                        val longPressDetectionJob = coroutineScope.launch {
                            delay(longPressTimeout)
                            isSelecting = true
                        }

                        drag(down.id) { change ->
                            val dragAmount = change.positionChange().y

                            if (!isSelecting && abs(dragAmount) > touchSlop) {
                                longPressDetectionJob.cancel()
                                isSelecting = true
                            }

                            scrollOffset = (scrollOffset + dragAmount).coerceIn(minOffset, maxOffset)

                            val snappedIndex = (nItems/2 - scrollOffset / itemHeightPx).roundToInt()
                            val clampedSnappedIndex = snappedIndex.coerceIn(0, key.actions.lastIndex)
                            selectedAction = key.actions[clampedSnappedIndex]

                            change.consume()
                        }
                        longPressDetectionJob.cancel()

                        onKeyAction(selectedAction)
                        isSelecting = false
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // The actual button
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.KeyboardArrowUp, null, modifier = Modifier.size(12.dp))
                    when (val label = selectedAction.label) {
                        is KeyLabel.Blank -> null

                        is KeyLabel.Text ->
                            Text(
                                text = label.text,
                                style = MaterialTheme.typography.labelLarge
                            )
                        is KeyLabel.Icon -> Icon(
                            label.icon,
                            label.description,
                            //tint = labelColor,
                            modifier = Modifier.size(MaterialTheme.typography.labelLarge.lineHeight.toDp())
                        )
                    }
                    Icon(Icons.Default.KeyboardArrowDown, null, modifier = Modifier.size(12.dp))
                }
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
                        for ((index, unit) in key.actions.withIndex()) {
                            val distanceFromCenter = abs(index*itemHeightPx + scrollOffset - centerOffsetPx + itemHeightPx/2)/itemHeightPx
                            val alpha = 1f - (distanceFromCenter * 0.3f)
                            val scale = 1f - (distanceFromCenter * 0.1f)

                            when (val label = unit.label) {
                                is KeyLabel.Blank -> null

                                is KeyLabel.Text ->
                                    Text(
                                        text = label.text,
                                        fontSize = 18.sp * scale.coerceIn(0.5f, 1f),
                                        color = Color.White.copy(alpha.coerceIn(0.5f, 1f)),
                                        modifier = Modifier
                                            .height(itemHeight)
                                    )
                                is KeyLabel.Icon -> Icon(
                                    label.icon,
                                    label.description,
                                    tint = Color.White.copy(alpha.coerceIn(0.5f, 1f)),
                                    modifier = Modifier.size(MaterialTheme.typography.labelLarge.lineHeight.toDp())
                                )
                            }
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