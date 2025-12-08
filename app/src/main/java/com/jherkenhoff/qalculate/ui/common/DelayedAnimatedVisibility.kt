package com.jherkenhoff.qalculate.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun DelayedAnimatedVisibility(
    condition: Boolean,
    delayMillis: Long = 1000L, // how long the condition must be true
    enter: EnterTransition,
    exit: ExitTransition,
    content: @Composable () -> Unit
) {
    var show by remember { mutableStateOf(false) }
    val currentCondition by rememberUpdatedState(condition)

    // Launch effect whenever the condition changes
    LaunchedEffect(condition) {
        if (condition) {
            delay(delayMillis)
            // Only show if the condition is still true after the delay
            if (currentCondition) show = true
        } else {
            show = false
        }
    }

    AnimatedVisibility(
        visible = show,
        enter = enter,
        exit = exit
    ) {
        content()
    }
}
