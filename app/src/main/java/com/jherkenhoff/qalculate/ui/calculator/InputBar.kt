package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun InputBar(
    textFieldValue: () -> TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val focusRequester = remember { FocusRequester() }
    var lastFocusState by remember { mutableStateOf(false) }
    val placeholdeVisible by remember { derivedStateOf { textFieldValue().text.isEmpty() } }

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        shape = RoundedCornerShape(24.dp),
        //shadowElevation = 3.dp,
        modifier = modifier.fillMaxWidth().heightIn(48.dp, 200.dp)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            BasicTextField(
                value = textFieldValue(),
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer),
            )


            //  && !lastFocusState
            if (placeholdeVisible) {
                Text(
                    text = stringResource(R.string.textfield_hint),
                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
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
private fun DefaultPreview() {
    InputBar(
        {TextFieldValue("1km + 1m")},
        {},
        {})
}

@Preview(showBackground = true)
@Composable
private fun PlaceholderPreview() {
    InputBar(
        {TextFieldValue("")},
        {},
        {})
}

@Preview(showBackground = true)
@Composable
private fun VeryLongInputPreview() {
    InputBar(
        {TextFieldValue("60+25+18+26+32+15+82+61+51+25+15+94+84+25+18+63+25+21+38+49+92+61+62")},
        {},
        {})
}