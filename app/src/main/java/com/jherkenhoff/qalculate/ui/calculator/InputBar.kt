package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.InterceptPlatformTextInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R
import kotlinx.coroutines.awaitCancellation

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InputBar(
    textFieldValue: () -> TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: (String) -> Unit,
    altKeyboardEnabled: Boolean,
    modifier: Modifier = Modifier,
    shadowElevation: Dp = 3.dp,
    onKeyboardToggleClick: () -> Unit = {},
) {

    val focusRequester = remember { FocusRequester() }
    var lastFocusState by remember { mutableStateOf(false) }

    Surface(
        //border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = RoundedCornerShape(100),
        shadowElevation = shadowElevation,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        val placeholdeVisible by remember { derivedStateOf { textFieldValue().text.isEmpty() } }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onKeyboardToggleClick,
            ) {
                Icon(
                    if (altKeyboardEnabled) Icons.Filled.Keyboard else Icons.Filled.Calculate,
                    contentDescription = "Localized description",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.weight(1f)
            ) {
                InterceptPlatformTextInput(
                    interceptor = { request, nextHandler ->
                        if (!altKeyboardEnabled) {
                            nextHandler.startInputMethod(request)
                        } else {
                            awaitCancellation()
                        }
                    },
                ) {
                    BasicTextField(
                        value = textFieldValue(),
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { state -> lastFocusState = state.isFocused },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Uri,
                            imeAction = ImeAction.Go,
                            autoCorrect = false
                        ),
                        keyboardActions = KeyboardActions {
                            if (textFieldValue().text.isNotBlank()) onSubmit(textFieldValue().text)
                        },
                        maxLines = 1,
                        cursorBrush = SolidColor(LocalContentColor.current),
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                    )
                }

                if (placeholdeVisible && !lastFocusState) {
                    Text(
                        text = stringResource(R.string.textfield_hint),
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
                    )
                }
            }
        }
    }

    // Focus the input text field on app startup
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceholderPreview() {
    InputBar(
        {TextFieldValue("")},
        {},
        {},
        false,)
}

@Preview(showBackground = true)
@Composable
private fun WithInputPreview() {
    InputBar(
        {TextFieldValue("1km + 1m")},
        {},
        {},
        false,)
}

@Preview(showBackground = true)
@Composable
private fun WithInputAltKeyboardPreview() {
    InputBar(
        {TextFieldValue("1km + 1m")},
        {},
        {},
        false,)
}