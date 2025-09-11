package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButtonColors
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter
import com.jherkenhoff.qalculate.R

@Composable
fun InputSheet(
    modifier: Modifier = Modifier,
    result: String,
    parsed: String,
    textFieldValue: () -> TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: (String) -> Unit,
    onClearAll: () -> Unit,
    focusRequester: FocusRequester,
    lastFocusState: androidx.compose.runtime.MutableState<Boolean>,
    placeholdeVisible: Boolean
) {
    // Request focus and show IME on launch
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    androidx.compose.material3.Card(
        shape = RoundedCornerShape(24.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(8.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(top = 18.dp, start = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            // Preview (result)
            AutoSizeText(
                text = mathExpressionFormatter(result),
                alignment = Alignment.CenterEnd,
                style = MaterialTheme.typography.displayMedium,
                minTextSize = 14.sp,
                maxTextSize = 50.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
            )

            // Preview (parsed)
            Text(
                mathExpressionFormatter(parsed, color = true),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .defaultMinSize(minHeight = 24.dp)
                    .wrapContentHeight(),
                textAlign = TextAlign.End
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .height(1.dp)
                    .fillMaxWidth(),
                color = MaterialTheme.colorScheme.outline
            )

            // Input row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier.weight(1f)
                ) {
                    BasicTextField(
                        value = textFieldValue(),
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .padding(vertical = 10.dp, horizontal = 12.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { state -> lastFocusState.value = state.isFocused },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            imeAction = ImeAction.Send
                        ),
                        keyboardActions = KeyboardActions {
                            if (textFieldValue().text.isNotBlank()) onSubmit(textFieldValue().text)
                        },
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                        singleLine = true
                    )

                    if (placeholdeVisible) {
                        Text(
                            text = stringResource(R.string.textfield_hint),
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.outline),
                            modifier = Modifier.padding(start = 8.dp)
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
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.errorContainer,
                                shape = RoundedCornerShape(14.dp)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = stringResource(R.string.clear_input),
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}
// Removed duplicate and stray code after main composable function