package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter


@Composable
fun InputSheet(
    textFieldValue: () -> TextFieldValue,
    parsed: String,
    result: String,
    isAltKeyboardOpen: Boolean,
    onToggleAltKeyboard: () -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClearAll: () -> Unit = {},
) {

    val focusRequester = remember { FocusRequester() }
    var lastFocusState by remember { mutableStateOf(false) }
    val placeholdeVisible by remember { derivedStateOf { textFieldValue().text.isEmpty() && !lastFocusState } }

    // Focus the input text field on app startup
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        //shape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp),
        shape = RoundedCornerShape(25.dp, 25.dp, 25.dp, 25.dp),
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            AutoSizeText(
                text = mathExpressionFormatter(result),
                alignment = Alignment.CenterEnd,
                style = MaterialTheme.typography.displayMedium,
                minTextSize = 14.sp,
                maxTextSize = 50.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .defaultMinSize(minHeight = 60.dp)
            )

            Text(
                // If the parsed expression is empty, add placeholder text to fill the gap.
                mathExpressionFormatter(parsed, color=true).ifEmpty { AnnotatedString(stringResource(R.string.parsed_expr_empty)) },
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .defaultMinSize(minHeight = 24.dp)
                    .wrapContentHeight(),
            )

            HorizontalDivider(
                thickness = Dp.Hairline,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.defaultMinSize(minHeight = 50.dp)
            ) {
                FilledTonalIconToggleButton(
                    checked = isAltKeyboardOpen,
                    onCheckedChange = { onToggleAltKeyboard() },
                    colors = IconButtonDefaults.filledTonalIconToggleButtonColors(),
                    modifier = Modifier
                        .padding(5.dp)
                        .size(40.dp)
                ) {
                    if (isAltKeyboardOpen) {
                        Icon(Icons.Filled.Calculate, contentDescription = null)
                    } else {
                        Icon(Icons.Outlined.Calculate, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.weight(1f)
                ) {
                    BasicTextField(
                        value = textFieldValue(),
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { state -> lastFocusState = state.isFocused },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions {
                            if (textFieldValue().text.isNotBlank()) onSubmit(textFieldValue().text)
                        },
                        cursorBrush = SolidColor(LocalContentColor.current),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    )


                    //  && !lastFocusState
                    if (placeholdeVisible) {
                        Text(
                            text = stringResource(R.string.textfield_hint),
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)),
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                val acButtonVisible by remember {
                    derivedStateOf {
                        textFieldValue().text.isNotEmpty()
                    }
                }

                AnimatedVisibility(visible = acButtonVisible) {
                    IconButton(
                        onClick = onClearAll,
                    ) {
                        Icon(Icons.Outlined.Close, contentDescription = stringResource(R.string.clear_input))
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun DefaultPreview() {
    InputSheet(
        {TextFieldValue("c")},
        "SpeedOfLight",
        "299.792 458 Km/ms",
        false,
        {},
        {},
        {}
    )
}

@Preview
@Composable
private fun PlaceholderPreview() {
    InputSheet(
        {TextFieldValue("")},
        "",
        "0",
        false,
        {},
        {},
        {}
    )
}