package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter
import kotlinx.coroutines.awaitCancellation


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputSheet(
    textFieldValue: TextFieldValue,
    parsedString: String,
    resultString: String,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
    interceptKeyboard: Boolean = false,
    onClearAll: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
    var lastFocusState by remember { mutableStateOf(false) }
    val placeholdeVisible by remember { derivedStateOf { textFieldValue.text.isEmpty() && !lastFocusState } }

    var angleUnitDialogOpen = remember { mutableStateOf(false) }

    // Focus the input text field on app startup
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(modifier = modifier.fillMaxWidth()) {
        AutoSizeText(
            text = mathExpressionFormatter(resultString),
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

        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.defaultMinSize(minHeight = 64.dp).padding(horizontal = 16.dp)
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
                    value = textFieldValue,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { state -> lastFocusState = state.isFocused },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Send,
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions {
                        if (textFieldValue.text.isNotBlank()) onSubmit(textFieldValue.text)
                    },
                    cursorBrush = SolidColor(LocalContentColor.current),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                )
            }

            //  && !lastFocusState
            if (placeholdeVisible) {
                Text(
                    text = stringResource(R.string.textfield_hint),
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)),
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            mathExpressionFormatter(parsedString, color=true),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .defaultMinSize(minHeight = 24.dp)
                .wrapContentHeight(),
        )
        Spacer(Modifier.height(20.dp))
    }

    AnimatedVisibility(angleUnitDialogOpen.value) {
        AngleUnitDialog(onDismissRequest = { angleUnitDialogOpen.value = false })
    }
}

@Composable
fun AngleUnitDialog(
    onDismissRequest: () -> Unit = {  }
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Angle unit", style = MaterialTheme.typography.headlineSmall)
                RadioButtonRow(text = "Degrees", selected = false, onClick = {  })
                RadioButtonRow(text = "Radians", selected = true, onClick = {  })
                RadioButtonRow(text = "Gradians", selected = false, onClick = {  })
                HorizontalDivider()
                RadioButtonRow(text = "Arcminute", selected = false, onClick = {  })
                RadioButtonRow(text = "Arcsecond", selected = false, onClick = {  })
                RadioButtonRow(text = "Turn", selected = false, onClick = {  })
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    InputSheet(
        TextFieldValue("c"),
        "SpeedOfLight",
        "299.792 458 Km/ms",
        {},
        {},
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaceholderPreview() {
    InputSheet(
        TextFieldValue(""),
        "",
        "0",
        {},
        {},
    )
}