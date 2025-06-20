package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter


class AutocompletePositionProvider() : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(0, 0)
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CalculationItem(
    inputFieldValue: TextFieldValue,
    parsedText: String,
    resultText: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onFocusChange: (FocusState) -> Unit = {},
    onInputFieldValueChange: (TextFieldValue) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }

    val animatedAlpha: Float by animateFloatAsState(
        targetValue = if (expanded) 1f else 0.0f,
        label = "alpha",
        animationSpec = tween(200)
    )

    val color: Color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceContainerHighest else MaterialTheme.colorScheme.background,
        label = "color",
        animationSpec = tween(200)
    )

    SharedTransitionLayout(
        modifier = modifier
    ) {
        Surface(
            color = color,
        ) {
            Column(
                modifier = modifier.fillMaxWidth().padding(top = 8.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.defaultMinSize(minHeight = 56.dp)
                        .padding(start = 16.dp, end = 4.dp)
                        .clickable(
                            onClick = { focusRequester.requestFocus() },
                            indication = null, // Disable ripple effect
                            interactionSource = remember { MutableInteractionSource() }
                        )
                ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier.weight(1f)
                    ) {
                        BasicTextField(
                            value = inputFieldValue,
                            onValueChange = onInputFieldValueChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .focusRequester(focusRequester)
                                .onFocusChanged { state -> onFocusChange(state) }.onKeyEvent {
                                    if (it.key.keyCode == Key.Enter.keyCode){
                                        onSubmit()
                                        true
                                    }
                                    false
                                },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false,
                                imeAction = ImeAction.Send,
                                keyboardType = KeyboardType.Password
                            ),
                            keyboardActions = KeyboardActions(
                                onAny = { onSubmit() }
                            ),
                            cursorBrush = SolidColor(LocalContentColor.current),
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                        )

                        if (inputFieldValue.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.textfield_hint),
                                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    with(this@SharedTransitionLayout) {
                        AnimatedContent(expanded) { it ->
                            if (!it) {
                                ResultSection(
                                    "= $resultText",
                                    modifier = Modifier.sharedElement(
                                        rememberSharedContentState(key = "result_section"),
                                        animatedVisibilityScope = this@AnimatedContent
                                    ),
                                    onDeleteClick = onDeleteClick
                                )
                            } else {
                                AnimatedVisibility(visible = true) {
                                    IconButton(
                                        onClick = { onInputFieldValueChange(inputFieldValue.copy(text = "")) },
                                    ) {
                                        Icon(
                                            Icons.Outlined.Close,
                                            contentDescription = stringResource(R.string.clear_input),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                with(this@SharedTransitionLayout) {
                    AnimatedVisibility(expanded) {
                        HorizontalDivider()

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                mathExpressionFormatter(parsedText),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                            ResultSection(
                                "= $resultText",
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .weight(1f)
                                    .sharedElement(
                                        rememberSharedContentState(key = "result_section"),
                                        animatedVisibilityScope = this@AnimatedVisibility
                                    ),
                                onDeleteClick = onDeleteClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InputField(
    inputFieldValue: TextFieldValue,
    modifier: Modifier = Modifier,
    onValueChange: (TextFieldValue) -> Unit = {},
) {

    var layoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var textFieldCoords by remember { mutableStateOf<LayoutCoordinates?>(null) }

    val highlightRange = 2..3 // "Compose"

    Box(
        modifier = Modifier.padding(50.dp)
    ) {
        BasicTextField(
            value = inputFieldValue,
            onValueChange = onValueChange,
            modifier = modifier
                .onGloballyPositioned { textFieldCoords = it }
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Password
            ),
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            onTextLayout = { layoutResult = it }
        )

        layoutResult?.let { layout ->
            textFieldCoords?.let { coords ->

                // Get bounding box for first character in highlight
                val startOffset = highlightRange.first
                val box = layout.getBoundingBox(inputFieldValue.selection.end)
                val position = coords.localToWindow(Offset(box.left, box.top))


                DropdownMenu(
                    expanded = true,
                    onDismissRequest = {},
                    offset = DpOffset(position.x.toDp(), position.y.toDp())
                ) {
                    DropdownMenuItem(text = { Text("Edit") }, onClick = {})
                }
            }
        }
    }
}

@Composable
private fun ResultSection(
    resultText: String,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {}
) {
    var menuOpen by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
    ) {
        Text(
            resultText,
            style = MaterialTheme.typography.bodyLarge,
        )
        Box {
            IconButton(
                onClick = { menuOpen = true }
            ) {
                Icon(
                    Icons.Outlined.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Menu(
                menuOpen,
                onDismissRequest = { menuOpen = false },
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
private fun Menu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text("Pin") },
            leadingIcon = { Icon(Icons.Outlined.PushPin, contentDescription = null) },
            onClick = { /* Do something... */ }
        )
        DropdownMenuItem(
            text = { Text("Delete") },
            leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
            onClick = { onDismissRequest(); onDeleteClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CompactPreview() {
    CalculationItem(
        TextFieldValue("1km + 5m"),
        "1 kilometer + 5 meter",
        "1.005 m",
        expanded = false
    )
}

@Preview(showBackground = true)
@Composable
private fun ExpandedPreview() {
    CalculationItem(
        TextFieldValue("1km + 5m"),
        "1 kilometer + 5 meter",
        "1.005 m",
        expanded = true
    )
}

@Preview(showBackground = true)
@Composable
private fun CanvasPreview() {

    val caretSize = 10.dp
    Box(
        modifier = Modifier.padding(50.dp).size(100.dp, 50.dp).drawBehind{
            drawRoundRect(Color.Blue, cornerRadius = CornerRadius(10.dp.toPx()))
            drawPath(
                Path().apply {
                    moveTo(600f, 1200f)
                    lineTo(800f, 800f)
                    lineTo(350f, 400f)
                    lineTo(160f, 25f)
                    close()
                },
                color = Color.Red
            )
        }
    )
}

