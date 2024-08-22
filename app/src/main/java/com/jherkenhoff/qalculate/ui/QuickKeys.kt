package com.jherkenhoff.qalculate.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inidamleader.ovtracker.util.compose.geometry.toIntPx
import java.time.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuickKeys(
    onKey: (String) -> Unit
) {
    val haptic = LocalHapticFeedback.current

    fun onClick(text: String) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onKey(text)
    }

    val state = rememberLazyListState()
    val nPanels = 2

    val leftChevronVisible by remember {
        derivedStateOf {
            state.firstVisibleItemIndex != 0 && state.firstVisibleItemScrollOffset == 0
        }
    }
    val rightChevronVisible by remember {
        derivedStateOf {
            state.firstVisibleItemIndex != nPanels-1  && state.firstVisibleItemScrollOffset == 0
        }
    }


    val chevronWidth = 20.dp

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            AnimatedVisibility(
                visible = leftChevronVisible,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(chevronWidth)
            ) {
                Icon(
                    Icons.Filled.ChevronLeft,
                    contentDescription = "Arrow to the left",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                state = state,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
            ) {
                for (i in 0..nPanels-1) {
                    item(key = i) {
                        QuickKeysPanel(
                            onKeyAction = onKey,
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(
                                    start = if (i != 0) chevronWidth else 0.dp,
                                    end = if (i != nPanels - 1) chevronWidth else 0.dp
                                )
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = rightChevronVisible,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(chevronWidth)
            ) {
                Icon(
                    Icons.Filled.ChevronRight,
                    contentDescription = "Arrow to the right",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QuickKeys(onKey = {})
}