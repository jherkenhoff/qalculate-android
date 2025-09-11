package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import com.jherkenhoff.qalculate.model.Key
import com.jherkenhoff.qalculate.model.KeyLabel
import com.jherkenhoff.qalculate.model.KeyRole
import kotlinx.coroutines.delay


@Composable
fun Key(
    key: Key,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    var showPopup by remember { mutableStateOf(false) }

    val popupEnabled = when(key.role) {
        KeyRole.SYSTEM -> false
        else -> true
    }

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

    Box(
        modifier = modifier.fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        showPopup = true
                        haptic.performHapticFeedback(HapticFeedbackType.VirtualKey)
                        try {
                            awaitRelease()
                            onClick()
                        } finally {
                            delay(50)
                            showPopup = false
                        }
                    },
                    onLongPress = {
                        onLongClick()
                    }
                )
            }
    ) {
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
                            when (val label = key.label) {
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
                when (val label = key.label) {
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
}