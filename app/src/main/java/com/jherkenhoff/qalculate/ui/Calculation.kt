package com.jherkenhoff.qalculate.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R

@Composable
fun ColumnScope.Calculation(
    input: TextFieldValue,
    parsed: String,
    result: String,
    editMode: Boolean,
    modifier: Modifier = Modifier,
    divider: String = "",
    onInputChanged: (TextFieldValue) -> Unit = {}
) {

    AnimatedVisibility(
        visible = (divider != ""),
    ) {
        Divider(text = divider)
    }

    Text(
        messageFormatter(parsed),
        style = MaterialTheme.typography.bodyMedium
    )
    Text(
        messageFormatter("="+result),
        style = MaterialTheme.typography.displayMedium,
        textAlign = TextAlign.End,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(8.dp))

    InputExpression(
        textFieldValue = input,
        onValueChange = onInputChanged,
        onFocused = {},
        focusState = false,
        onSubmit = {},
        editMode = editMode
    )

}
@Composable
fun Calculation(
    inputTextFieldValue: TextFieldValue,
    parsed: String,
    result: String,
    editMode: Boolean,
    modifier: Modifier = Modifier,
    divider: String = "",
    onInputChanged: (TextFieldValue) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        Calculation(
            inputTextFieldValue,
            parsed,
            result,
            editMode,
            divider = divider,
            onInputChanged = onInputChanged
        )
    }
}

@Composable
private fun Divider(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(16.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}


@Composable
private fun InputExpression(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    onFocused: (Boolean) -> Unit,
    focusState: Boolean,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
    editMode: Boolean = false,
) {
    var lastFocusState by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.height(40.dp)
    ) {

        AnimatedVisibility(
            visible = editMode,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Surface(
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.onSurface),
                shape = RoundedCornerShape(100),
                modifier = Modifier.fillMaxSize()
            ) {}
        }
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            AnimatedVisibility(editMode) {
                IconButton(
                    onClick = { /* doSomething() */ },
                ) {
                    Icon(Icons.AutoMirrored.Outlined.List, contentDescription = "Localized description", tint = MaterialTheme.colorScheme.onSurface)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier.weight(1f)
            ) {
                BasicTextField(
                    value = textFieldValue,
                    onValueChange = onValueChange,
                    modifier = modifier
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
                    textStyle = MaterialTheme.typography.bodyMedium,
                )

                if (textFieldValue.text.isEmpty() && !focusState) {
                    Text(
                        text = stringResource(R.string.textfield_hint),
                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
                    )
                }
            }
        }

    }
}


@Composable
private fun messageFormatter(
    text: String
): AnnotatedString {
    val tokens = Regex("""<.*?>|([^<]+)?|(&nbsp;)""").findAll(text)

    return buildAnnotatedString {

        for (token in tokens) {
            when (token.value) {
                "<i>" -> pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                "</i>" -> pop()
                "<span style=\"color:#005858\">" -> pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
                "<span style=\"color:#585800\">" -> pushStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary))
                "<span style=\"color:#008000\">" -> pushStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary))
                "</span>" -> pop()
                "<sup>" -> pushStyle(SpanStyle(baselineShift = BaselineShift.Superscript))
                "</sup>" -> pop()
                "<sub>" -> pushStyle(SpanStyle(baselineShift = BaselineShift.Subscript))
                "</sub>" -> pop()
                "&nbsp;" -> append("\n")
                else -> append(token.value)
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    Calculation(
        TextFieldValue("1km + 5m"),
        "1 kilometer + 5 meter",
        "1.005 m",
        editMode = false,
        divider = "Yesterday",
        modifier = Modifier.padding(16.dp)
    )
}


@Preview(showBackground = true)
@Composable
private fun EditablePreview() {
    Calculation(
        TextFieldValue("1km + 5m"),
        "1 kilometer + 5 meter",
        "1.005 m",
        editMode = true,
        modifier = Modifier.padding(16.dp)
    )
}