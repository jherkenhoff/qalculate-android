package com.jherkenhoff.qalculate.ui.calculator

import android.content.ClipData
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DragIndicator
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter
import com.jherkenhoff.qalculate.ui.common.mathExpressionPlainText
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import kotlin.math.exp

@Composable
fun CalculationHistoryItem2(
    calculationNumber: Int,
    input: TextFieldValue,
    parsed: String,
    result: String,
    index: Int,
    count: Int,
    interceptKeyboard: Boolean,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
    onClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    var menuOpen by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }

    val calculationNumberVerticalBias by animateFloatAsState(if (expanded) -1f else 0f)
    val verticalDividerPadding by animateDpAsState(if (expanded) 0.dp else 8.dp)
    val leadingContentColor by animateColorAsState(if (expanded) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)

    val topCornerRadius by animateDpAsState(if (expanded || index == 0) 16.dp else 4.dp)
    val bottomCornerRadius by animateDpAsState(if (expanded || index == count-1) 16.dp else 4.dp)

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(
            topStart = topCornerRadius,
            topEnd = topCornerRadius,
            bottomStart = bottomCornerRadius,
            bottomEnd = bottomCornerRadius),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .animateContentSize()
    ) {
        SharedTransitionLayout(Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .fillMaxHeight()
                        .background(leadingContentColor)
                ) {
                    Text(
                        calculationNumber.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .align(
                                BiasAlignment(
                                    horizontalBias = 0f,
                                    verticalBias = calculationNumberVerticalBias
                                )
                            )
                            .padding(6.dp)
                    )
                    this@Row.AnimatedVisibility(
                        visible = expanded,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Icon(
                            Icons.Default.DragIndicator,
                            null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                }
                VerticalDivider(Modifier.padding(vertical = verticalDividerPadding))
                AnimatedContent(expanded, modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    if (it) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                        ) {
                            InputField(
                                value = input,
                                onValueChange = {},
                                focusRequester = focusRequester,
                                interceptKeyboard = interceptKeyboard,
                                onSubmit = {}
                            )
                            HorizontalDivider()
                            Text(
                                mathExpressionFormatter(parsed),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(Modifier.height(6.dp))
                            Text("= " + mathExpressionFormatter(result), modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp), textAlign = TextAlign.End)
                        }
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            Text(input.text, modifier = Modifier.padding(vertical = 8.dp))
                            Text("= " + mathExpressionFormatter(result), textAlign = TextAlign.End, modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }

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
                        onDismissRequest = { menuOpen = false },
                        onDeleteClick = onDeleteClick
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
    focusRequester: FocusRequester,
    interceptKeyboard: Boolean,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {

    var lastFocusState by remember { mutableStateOf(false) }
    val placeholderVisible by remember { derivedStateOf { value.text.isEmpty() } }

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
                    .padding(vertical = 6.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { state -> lastFocusState = state.isFocused },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Send,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions { onSubmit() },
                cursorBrush = SolidColor(LocalContentColor.current),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
            )
        }

        //  && !lastFocusState
        if (placeholderVisible) {
            Text(
                text = stringResource(com.jherkenhoff.qalculate.R.string.textfield_hint),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                        alpha = 0.8f
                    )
                ),
            )
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

    val clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            Modifier
                .weight(1f, fill = false)
                .combinedClickable(
                    interactionSource = null,
                    indication = null,
                    onClick = {},
                    onLongClick = {
                        scope.launch {
                            clipboard.setClipEntry(
                                ClipEntry(
                                    ClipData.newPlainText(null, mathExpressionPlainText(resultText))
                                )
                            )
                        }
                    }
                )
        ) {
            Text(
                "= ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.alignByBaseline(),
            )
            Text(
                mathExpressionFormatter(resultText),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignByBaseline()
            )
        }

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
            text = { Text("Delete") },
            leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
            onClick = { onDismissRequest(); onDeleteClick() }
        )
        DropdownMenuItem(
            text = { Text(stringResource(com.jherkenhoff.qalculate.R.string.add_new_calculation_above)) },
            leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
            onClick = { onDismissRequest(); onDeleteClick() }
        )
        DropdownMenuItem(
            text = { Text(stringResource(com.jherkenhoff.qalculate.R.string.add_new_calculation_below)) },
            leadingIcon = { Icon(Icons.Default.Add, contentDescription = null) },
            onClick = { onDismissRequest(); onDeleteClick() }
        )
    }
}

@Preview
@Composable
private fun List() {
    val n = 4
    var activeIdx by remember{ mutableIntStateOf(1) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 0..n-1) {
            CalculationHistoryItem2(
                i+1,
                TextFieldValue("1km + 5m"),
                "1 kilometer + 5 meter",
                "1.005 m",
                index = i,
                count = n,
                expanded = i == activeIdx,
                onClick = { activeIdx = i },
                interceptKeyboard = true
            )
        }
    }
}

@Preview
@Composable
private fun OverflowPreview() {
    CalculationHistoryItem2(
        1,
        TextFieldValue("boltzmann * planck"),
        "boltzmann*planck",
        "9.1482771E-57 second*joule^2/kelvin",
        index = 0,
        count = 1,
        interceptKeyboard = true
    )
}

@Preview
@Composable
private fun OverflowOverflowPreview() {
    CalculationHistoryItem2(
        1,
        TextFieldValue("boltzmann + planck"),
        "boltzmann + planck",
        "6.626 070 15 × 10^(−34) Joule·seconds + 13.806 49 peta joule / terra kelvin",
        index = 0,
        count = 1,
        interceptKeyboard = true
    )
}