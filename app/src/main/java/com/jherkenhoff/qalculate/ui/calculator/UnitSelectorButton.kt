package com.jherkenhoff.qalculate.ui.calculator

import android.view.ViewConfiguration
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun UnitSelectorButton(
    onUnitSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val siUnits = listOf(
        "nm" to "nanometer",
        "μm" to "micrometer",
        "mm" to "millimeter",
        "cm" to "centimeter",
        "m" to "meter",
        "km" to "kilometer",
        "Mm" to "megameter",
        "Gm" to "gigameter"
    )

    val itemHeight = 40.dp
    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    val middleIndex = siUnits.indexOfFirst { it.first == "m" }
    val minOffset = -(middleIndex) * itemHeightPx
    val maxOffset = (siUnits.lastIndex - middleIndex) * itemHeightPx

    var isSelecting by remember { mutableStateOf(false) }

    val scrollOffset = remember { Animatable(0f) }

    val currentIndex by remember { derivedStateOf {
            val calculatedIndex = middleIndex + (scrollOffset.value / itemHeightPx - 0.5).toInt()
            calculatedIndex.coerceIn(0, siUnits.lastIndex)
        }
    }

    val selectedUnit = siUnits[currentIndex]

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val down = awaitFirstDown()
                        coroutineScope.launch {
                            scrollOffset.snapTo(0f)
                        }

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

                            coroutineScope.launch {
                                val newOffset = (scrollOffset.value + dragAmount).coerceIn(minOffset, maxOffset)
                                scrollOffset.snapTo(newOffset)
                            }

                            change.consume()
                        }
                        longPressDetectionJob.cancel()

                        val finalOffset = scrollOffset.value.coerceIn(minOffset, maxOffset)
                        val snappedIndex = (finalOffset / itemHeightPx).roundToInt()
                        val clampedSnappedIndex = (middleIndex - snappedIndex).coerceIn(0, siUnits.lastIndex)
                        onUnitSelected(siUnits[clampedSnappedIndex].second)
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
                Text(
                    text = if (isSelecting) selectedUnit.first else "m",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        if (isSelecting) {
            // The popup
            Popup(alignment = Alignment.TopCenter, offset = IntOffset(0, -250)) {
                Box(
                    Modifier
                        .width(160.dp)
                        .height(itemHeight * 5)
                        .background(Color.Black.copy(alpha = 0.85f), RoundedCornerShape(12.dp))
                        .clipToBounds()
                        .padding(vertical = 8.dp)
                ) {
                    // Scrollable content
                    val centerOffsetPx = itemHeightPx * 2 // center of 5-item list
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(unbounded = true, align = Alignment.TopCenter)
                            .offset(y = scrollOffset.value.toDp()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for ((index, unit) in siUnits.withIndex()) {
                            val distanceFromCenter = abs(index*itemHeightPx + scrollOffset.value - centerOffsetPx)/itemHeightPx
                            val alpha = 1f - (distanceFromCenter * 0.3f)
                            val scale = 1f - (distanceFromCenter * 0.1f)

                            Text(
                                text = unit.second,
                                fontSize = 18.sp * scale.coerceIn(0.5f, 1f),
                                color = Color.White.copy(alpha.coerceIn(0.5f, 1f)),
                                modifier = Modifier
                                    .height(itemHeight)
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



@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    UnitSelectorButton(
        { }
    )
}