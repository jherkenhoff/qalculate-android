package com.jherkenhoff.qalculate.ui

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R

@Composable
fun InputBar(
    inputFieldValue: TextFieldValue,
    onInputFieldValueChanged: (TextFieldValue) -> Unit,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp),
        horizontalArrangement = Arrangement.End
    ) {

        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(100),
                ),
            verticalAlignment=Alignment.CenterVertically
        ){

            IconButton(
                onClick = { /* doSomething() */ },
            ) {
                Icon(Icons.AutoMirrored.Outlined.List, contentDescription = "Localized description", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            ExpressionInputTextField(
                inputFieldValue,
                onValueChange = onInputFieldValueChanged,
                onFocused = {},
                focusState = false,
                onSubmit = {},
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        FilledIconButton(
            onClick = { /*TODO*/ },
            colors = IconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
        ) {
            Icon(Icons.Outlined.Send, contentDescription = "Submit calculation")
        }
    }
}

@Composable
private fun ExpressionInputTextField(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onFocused: (Boolean) -> Unit,
    focusState: Boolean,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var lastFocusState by remember { mutableStateOf(false) }

    Box(modifier = modifier){
        BasicTextField(
            value = textFieldValue,
            onValueChange = { onValueChange(it) },
            modifier = modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .onFocusChanged { state ->
                    if (lastFocusState != state.isFocused) {
                        onFocused(state.isFocused)
                    }
                    lastFocusState = state.isFocused
                },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Go,
                autoCorrect = false
            ),
            keyboardActions = KeyboardActions {
                if (textFieldValue.text.isNotBlank()) onSubmit(textFieldValue.text)
            },
            maxLines = 1,
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
        )

        val disableContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        if (textFieldValue.text.isEmpty() && !focusState) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = stringResource(R.string.textfield_hint),
                style = MaterialTheme.typography.bodyLarge.copy(color = disableContentColor)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    InputBar(TextFieldValue(""), {})
}