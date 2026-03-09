package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.KeypadDefinition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun KeypadSwitch2(
    keypads: List<KeypadDefinition>,
    activeKeypad: Int,
    modifier: Modifier = Modifier,
    onKeypadChanged: (Int) -> Unit = {},
    compact: Boolean = false
) {
    var expanded by remember{ mutableStateOf(false) }

    Row(verticalAlignment = Alignment.Bottom) {
        val c = MaterialTheme.colorScheme.surfaceContainerHighest

        Box(modifier = Modifier.drawBehind {
            val radius = size.height/2
            val path = Path().apply {
                moveTo(0f, size.height)
                lineTo(0f, 0f)
                lineTo(size.width-radius, 0f)
                arcTo(
                    rect = Rect(
                        center = Offset(size.width-radius, radius),
                        radius = radius
                    ),
                    -90f,
                    90f,
                    false
                )
                lineTo(size.width, size.height-radius)
                arcTo(
                    rect = Rect(
                        center = Offset(size.width+radius, size.height-radius),
                        radius = radius
                    ),
                    180f,
                    -90f,
                    false
                )
            }
            drawPath(path, c)
        }
            .padding(horizontal = 8.dp)
        ) {

            SharedTransitionLayout {

                AnimatedContent(
                    targetState = expanded
                ) { isExpanded ->
                    if (!isExpanded) {
                        TextButton(
                            modifier = Modifier.sharedElement(
                                rememberSharedContentState(key = activeKeypad),
                                animatedVisibilityScope = this@AnimatedContent
                            ),
                            onClick = { expanded = true }
                        ) {
                            Icon(keypads[activeKeypad].icon, null)
                            Text(keypads[activeKeypad].name)
                        }

                    } else {

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                            items(keypads.size, key = { it }) { item ->

                                val isSelected = item == activeKeypad

                                TextButton(
                                    modifier =
                                        if (isSelected)
                                            Modifier.sharedElement(
                                                rememberSharedContentState(key = item),
                                                animatedVisibilityScope = this@AnimatedContent
                                            )
                                        else Modifier,
                                    onClick = {
                                        onKeypadChanged(item)
                                        expanded = false
                                    }
                                ) {
                                    Icon(keypads[item].icon, null)
                                    Text(keypads[item].name)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.weight(1f))
        AnimatedContent(false) {
            if (it) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuggestionChip(
                        label = { Text("Moin") },
                        onClick = {}
                    )
                    SuggestionChip(
                        label = { Text("Moin") },
                        onClick = {}
                    )
                    IconButton({}) {
                        Icon(Icons.Default.Close, "Dismiss autocomplete suggestions")
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.End) {
                    IconButton({}, enabled = false) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
                    }
                    IconButton({}, enabled = false) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                    }
                    IconButton({}, enabled = false) {
                        Icon(Icons.AutoMirrored.Filled.Undo, null)
                    }
                    IconButton({}, enabled = false) {
                        Icon(Icons.AutoMirrored.Filled.Redo, null)
                    }
                }
                Spacer(Modifier.width(16.dp))
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    var activeKeypad = remember { mutableIntStateOf(0) }

    KeypadSwitch2(
        listOf(
            KeypadDefinition(
                name = "Basic",
                icon = Icons.Default.Calculate,
                sections = emptyList(),
                imeEnabled = false
            ),
            KeypadDefinition(
                name = "Advanced",
                icon = Icons.Default.Science,
                sections = emptyList(),
                imeEnabled = false
            ),
        ),
        activeKeypad.intValue,
        onKeypadChanged = { activeKeypad.intValue = it }
    )
}