package com.jherkenhoff.qalculate.ui.calculator

import android.content.ClipData
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitLongPressOrCancellation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter
import com.jherkenhoff.qalculate.ui.common.mathExpressionPlainText
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import sh.calvin.reorderable.ReorderableCollectionItemScope

private val largeCornerRadius = 16.dp
private val smallCornerRadius = 4.dp

fun Modifier.onLongPress(
    onLongPress: () -> Unit
): Modifier = pointerInput(onLongPress) {
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)

        val longPress = awaitLongPressOrCancellation(down.id)
        if (longPress != null) {
            onLongPress()
            // Consume everything until the finger is released.
            do {
                val event = awaitPointerEvent()
                event.changes.forEach { it.consume() }
            } while (event.changes.any { it.pressed })

        }
    }
}
@Composable
fun ReorderableCollectionItemScope.ActiveCalculationListItem(
    id: Long,
    input: TextFieldValue,
    parsed: String,
    result: String,
    executionOrderNumber: Int?,
    interceptKeyboard: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    userPreferences: UserPreferences,
    modifier: Modifier = Modifier,
    onInputChange: (TextFieldValue) -> Unit = {},
    onClick: () -> Unit = {},
    onUserpreferencesChanged: (UserPreferences) -> Unit = {},
    onDragStopped: () -> Unit = {},
    onAction: (CalculatorAction) -> Unit = {}
) {
    var menuOpen by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current

    with(sharedTransitionScope) {

        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = RoundedCornerShape(largeCornerRadius),
            modifier = modifier.fillMaxWidth().clickable { onClick() }.sharedElement(
                rememberSharedContentState("container"),
                animatedVisibilityScope,
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(40.dp)
                ) {
                    Text(
                        executionOrderNumber?.toString() ?: "•",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.TopCenter).padding(6.dp)
                            .sharedElement(
                                rememberSharedContentState("number"),
                                animatedVisibilityScope
                            )
                    )
                    Icon(
                        Icons.Default.DragIndicator,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center).draggableHandle(
                            onDragStopped = onDragStopped
                        )
                    )

                }
                VerticalDivider(Modifier.fillMaxHeight().padding(vertical = 8.dp))

                Column(Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InputField(
                            value = input,
                            onValueChange = onInputChange,
                            interceptKeyboard = interceptKeyboard,
                            onSubmit = {},
                            modifier = Modifier.weight(1f)
                        )
                        Box(Modifier.width(40.dp)) {
                            IconButton(onClick = { menuOpen = true }) {
                                Icon(
                                    Icons.Outlined.MoreVert,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Menu(
                                menuOpen,
                                id,
                                onDismissRequest = { menuOpen = false },
                                onAction = onAction
                            )
                        }
                    }
                    HorizontalDivider()
                    Text(
                        mathExpressionFormatter(parsed),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(6.dp))

                    AutoSizeText(
                        text = mathExpressionFormatter(result),
                        alignment = Alignment.CenterEnd,
                        style = MaterialTheme.typography.displayMedium,
                        minTextSize = 14.sp,
                        maxTextSize = 40.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 8.dp)
                            .size(40.dp)
                            .sharedElement(
                                rememberSharedContentState("result"),
                                animatedVisibilityScope
                            ).onLongPress({
                                clipboardManager.setText(
                                    AnnotatedString(mathExpressionPlainText(result))
                                )
                            })
                    )
                }

            }
        }

    }
}


@Composable
fun PassiveCalculationListItem(
    id: Long,
    input: String,
    result: String,
    executionOrderNumber: Int?,
    topRounded: Boolean,
    bottomRounded: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onAction: (CalculatorAction) -> Unit = {}
) {
    var menuOpen by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current

    with(sharedTransitionScope) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(
                topStart = if (topRounded) largeCornerRadius else smallCornerRadius,
                topEnd = if (topRounded) largeCornerRadius else smallCornerRadius,
                bottomStart = if (bottomRounded) largeCornerRadius else smallCornerRadius,
                bottomEnd = if (bottomRounded) largeCornerRadius else smallCornerRadius
            ),
            modifier = modifier.fillMaxWidth().clickable { onClick() }.sharedElement(
                rememberSharedContentState("container"),
                animatedVisibilityScope
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .fillMaxHeight()
                ) {
                    Text(
                        executionOrderNumber?.toString() ?: "•",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.Center).padding(6.dp)
                            .sharedElement(
                                rememberSharedContentState("number"),
                                animatedVisibilityScope
                            )
                    )
                }
                VerticalDivider(Modifier.padding(vertical = 8.dp))
                Text(
                    input,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 8.dp)
                        .sharedElement(
                            rememberSharedContentState("input"),
                            animatedVisibilityScope
                        ).onLongPress({
                            clipboardManager.setText(
                                AnnotatedString(input)
                            )
                        })
                )
                Spacer(Modifier.weight(1f))
                Text(
                    mathExpressionFormatter(result),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .sharedElement(
                            rememberSharedContentState("result"),
                            animatedVisibilityScope
                        ).onLongPress({
                            clipboardManager.setText(
                                AnnotatedString(mathExpressionPlainText(result))
                            )
                        })
                )
                Box(
                ) {
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
                        id,
                        onDismissRequest = { menuOpen = false },
                        onAction = onAction
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun InputField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    interceptKeyboard: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val focusRequester = remember { FocusRequester() }

    val placeholderVisible by remember { derivedStateOf { value.text.isEmpty() } }

    LaunchedEffect(true) {
        focusRequester.requestFocus()
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
    ) {
        InterceptPlatformTextInput(
            interceptor = { request, nextHandler ->
                if (interceptKeyboard) {
                    awaitCancellation()
                } else {
                    nextHandler.startInputMethod(request)
                }
            }
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Send,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions { onSubmit() },
                cursorBrush = SolidColor(LocalContentColor.current),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
            )
        }

        if (value.text.isEmpty()) {
            Text(
                text = stringResource(R.string.textfield_hint),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun Menu(
    expanded: Boolean,
    calculationId: Long,
    onDismissRequest: () -> Unit,
    onAction: (CalculatorAction) -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.add_calculation_above)) },
            leadingIcon = { Icon(Icons.Default.KeyboardArrowUp, contentDescription = null) },
            onClick = { onDismissRequest(); onAction(CalculatorAction.AddCalculation(calculationId, CalculatorAction.AddCalculation.Direction.ABOVE)) }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.add_calculation_below)) },
            leadingIcon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = null) },
            onClick = { onDismissRequest(); onAction(CalculatorAction.AddCalculation(calculationId, CalculatorAction.AddCalculation.Direction.BELOW)) }
        )
        HorizontalDivider()
        DropdownMenuItem(
            text = { Text("Delete") },
            leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
            onClick = { onDismissRequest(); onAction(CalculatorAction.DeleteCalculation(calculationId)) }
        )
    }
}

//
//@Preview
//@Composable
//private fun ActivePreview() {
//    SharedTransitionLayout() {
//        AnimatedVisibility(true) {
//            ActiveCalculationListItem(
//                1,
//                TextFieldValue("1+1"),
//                "1+1",
//                "2",
//                interceptKeyboard = true,
//                sharedTransitionScope = this@SharedTransitionLayout,
//                animatedVisibilityScope = this@AnimatedVisibility,
//                userPreferences = UserPreferences()
//            )
//        }
//    }
//}

@Preview
@Composable
private fun PassivePreview() {
    SharedTransitionLayout() {
        AnimatedVisibility(true) {
            PassiveCalculationListItem(
                0,
                "1+1",
                "0",
                1,
                topRounded = false,
                bottomRounded = false,
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@AnimatedVisibility
            )
        }
    }
}

//
//@Preview
//@Composable
//private fun TransitionPreview() {
//
//    var expanded by remember { mutableStateOf(false) }
//
//    Box(Modifier.fillMaxWidth().height(400.dp)) {
//        SharedTransitionLayout() {
//            AnimatedContent(expanded) {
//                if (it)
//                    ActiveCalculationListItem(
//                        1,
//                        TextFieldValue("1+1"),
//                        "1+1",
//                        "2",
//                        interceptKeyboard = true,
//                        sharedTransitionScope = this@SharedTransitionLayout,
//                        animatedVisibilityScope = this@AnimatedContent,
//                        userPreferences = UserPreferences()
//                    )
//                else
//                    PassiveCalculationListItem(
//                        1,
//                        "1+1",
//                        "0",
//                        topRounded = false,
//                        bottomRounded = false,
//                        sharedTransitionScope = this@SharedTransitionLayout,
//                        animatedVisibilityScope = this@AnimatedContent
//                    )
//            }
//        }
//    }
//}