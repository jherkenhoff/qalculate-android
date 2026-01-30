package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.Action
import com.jherkenhoff.qalculate.model.KeypadSpec

@Composable
fun Keypad(
    keypads: List<KeypadSpec>,
    activeKeypadIndex: Int,
    modifier: Modifier = Modifier,
    onKeyAction: (Action) -> Unit = {},
    onActiveKeypadChanged: (Int) -> Unit = {},
) {
    val activeKeypad = keypads[activeKeypadIndex]

    Column(modifier) {
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
                KeypadSwitch(
                    keypads,
                    activeKeypadIndex,
                    onKeypadChanged = onActiveKeypadChanged
                )
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

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
        ) {
            Column() {
                Spacer(Modifier.height(6.dp))

//                val auxiliaryActions = listOf(
//                    Action.MoveCursor(
//                        ActionLabel.Icon(Icons.Default.ChevronLeft, "Move cursor to the left"),
//                        -1,
//                        enabled = (inputTextFieldValue.selection.end != 0)
//                    ),
//                    Action.MoveCursor(
//                        ActionLabel.Icon(Icons.Default.ChevronRight, "Move cursor to the right"),
//                        1,
//                        enabled = (inputTextFieldValue.selection.end != inputTextFieldValue.text.length)
//                    ),
//                    Action.Undo(
//                        ActionLabel.Icon(Icons.AutoMirrored.Filled.Undo, "Undo"),
//                        enabled = undoState.canUndo
//                    ),
//                    Action.Redo(
//                        ActionLabel.Icon(Icons.AutoMirrored.Filled.Redo, "Redo"),
//                        enabled = undoState.canRedo
//                    ),
//                )

//                        AuxiliaryBar(
//                            autocompleteResult = internalAutocompleteResult,
//                            keyboardEnable = keyboardEnabled,
//                            auxiliaryActions = auxiliaryActions,
//                            onAutocompleteClick = onAutocompleteClick,
//                            onKeyboardEnableChange = { keyboardEnabled = it },
//                            onAction = onKeyAction,
//                            onAutocompleteDismiss = { autocompleteDismissed = true },
//                            modifier = Modifier.fillMaxWidth()
//                        )

                Column(
                    Modifier
                        .padding(horizontal = 3.dp),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    activeKeypad.sections.map { keypad ->
                        GridLayout(
                            keypad.rows,
                            keypad.cols,
                            horizontalSpacing = 3.dp,
                            verticalSpacing = 3.dp,
                            aspectRatio = keypad.aspectRatio,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            keypad.keys.forEach {
                                item(it.first.row, it.first.col, it.first.rowSpan, it.first.colSpan) {
                                    Key(
                                        it.second,
                                        onKeyAction = onKeyAction
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(WindowInsets.safeContent.getBottom(LocalDensity.current).toDp()))

            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    Keypad(
        emptyList(),
        0
    )
}