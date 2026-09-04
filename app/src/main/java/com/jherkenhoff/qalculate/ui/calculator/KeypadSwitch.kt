package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Science
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.domain.model.KeypadDefinition

@Composable
fun KeypadSwitch(
    keypads: List<KeypadDefinition>,
    activeKeypad: Int,
    modifier: Modifier = Modifier,
    onKeypadChanged: (Int) -> Unit = {},
    compact: Boolean = false
) {
    var expanded by remember{ mutableStateOf(false) }

    val activeKeypad = keypads[activeKeypad]


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
            Box(modifier) {
                TextButton({ expanded = true }) {
                    Icon(activeKeypad.icon, null)
                    AnimatedVisibility(!compact) {
                        Row{
                            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                            Text(activeKeypad.name)
                        }
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    keypads.mapIndexed { i, keypad ->
                        DropdownMenuItem(
                            text = { Text(keypad.name) },
                            leadingIcon = { Icon(keypad.icon, null) },
                            onClick = { onKeypadChanged(i); expanded = false }
                        )
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
    KeypadSwitch(
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
        0
    )
}