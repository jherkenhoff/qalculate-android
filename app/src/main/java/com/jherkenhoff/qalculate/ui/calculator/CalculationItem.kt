package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter

private const val overflowFraction = 0.9f

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CalculationItem(
    inputFieldValue: TextFieldValue,
    parsedText: String,
    resultText: String,
    modifier: Modifier = Modifier,
    autocompleteResult: AutocompleteResult? = null,
    expanded: Boolean = false,
    onFocusChange: (FocusState) -> Unit = {},
    onInputFieldValueChange: (TextFieldValue) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onSubmit: () -> Unit = {},
    onAutocompleteClick: (AutocompleteItem) -> Unit = {}
) {
    val color: Color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceContainer else MaterialTheme.colorScheme.background,
        label = "color",
        animationSpec = tween(200)
    )

    Surface(
        color = color,
        modifier = modifier.padding(vertical = 6.dp)
    ) {
        SharedTransitionLayout(
            Modifier.padding(top = 10.dp, bottom = 6.dp)
        ) {
            with(this@SharedTransitionLayout) {
                AnimatedContent(expanded) {
                    if (it) {
                        ExpandedCalculationItem(
                            inputFieldValue,
                            parsedText,
                            resultText,
                            this@AnimatedContent,
                            autocompleteResult = autocompleteResult,
                            onFocusChange = onFocusChange,
                            onInputFieldValueChange = onInputFieldValueChange,
                            onSubmit = onSubmit,
                            onDeleteClick = onDeleteClick,
                            onAutocompleteClick = onAutocompleteClick
                        )
                    } else {
                        CompactCalculationItem(
                            inputFieldValue,
                            resultText,
                            this@AnimatedContent,
                            onFocusChange = onFocusChange,
                            onInputFieldValueChange = onInputFieldValueChange,
                            onSubmit = onSubmit,
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CompactCalculationItem(
    inputFieldValue: TextFieldValue,
    resultText: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    onFocusChange: (FocusState) -> Unit = {},
    onInputFieldValueChange: (TextFieldValue) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    SubcomposeLayout(
        modifier = modifier
            .clickable(
                onClick = { focusRequester.requestFocus() },
                indication = null, // Disable ripple effect
                interactionSource = remember { MutableInteractionSource() }
            )
    ) { constraints ->

        val inputTextFieldPlaceable = subcompose("inputField") {
            InputTextField(
                inputFieldValue,
                focusRequester,
                focusManager,
                onInputFieldValueChange = onInputFieldValueChange,
                onFocusChange = onFocusChange,
                onSubmit = onSubmit,
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(key = "input_text_field"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }[0].measure(constraints.copy(maxWidth = (constraints.maxWidth*overflowFraction).toInt()))

        val resultSectionPlaceable = subcompose("resultSection") {
            ResultSection(
                resultText,
                onDeleteClick = onDeleteClick,
                modifier = Modifier.sharedElement(
                    rememberSharedContentState(key = "result_section"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }[0].measure(constraints.copy(maxWidth = (constraints.maxWidth*overflowFraction).toInt()))

        val overflow =  inputTextFieldPlaceable.width + resultSectionPlaceable.width > constraints.maxWidth

        val totalHeight = if (overflow) inputTextFieldPlaceable.height + resultSectionPlaceable.height else inputTextFieldPlaceable.height

        layout(constraints.maxWidth, totalHeight) {
            inputTextFieldPlaceable.place(0, 0)
            if (overflow) {
                resultSectionPlaceable.place(constraints.maxWidth-resultSectionPlaceable.width, inputTextFieldPlaceable.height)
            } else {
                resultSectionPlaceable.place(constraints.maxWidth-resultSectionPlaceable.width, 0)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExpandedCalculationItem(
    inputFieldValue: TextFieldValue,
    parsedText: String,
    resultText: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    autocompleteResult: AutocompleteResult? = null,
    onFocusChange: (FocusState) -> Unit = {},
    onInputFieldValueChange: (TextFieldValue) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onSubmit: () -> Unit = {},
    onAutocompleteClick: (AutocompleteItem) -> Unit = {}
) {

    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.defaultMinSize(minHeight = 56.dp)
        ) {
            InputTextField(
                inputFieldValue,
                focusRequester,
                focusManager,
                onInputFieldValueChange = onInputFieldValueChange,
                onFocusChange = onFocusChange,
                onSubmit = onSubmit,
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = "input_text_field"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .weight(1f)
            )
            AnimatedVisibility(visible = inputFieldValue.text.isNotEmpty()) {
                IconButton(
                    onClick = { onInputFieldValueChange(TextFieldValue("")) },
                ) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.clear_input),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
//
//        AnimatedVisibility(autocompleteResult != null && autocompleteResult.items.isNotEmpty()) {
//            LazyRow(
//                modifier = Modifier.padding(horizontal = 8.dp).height(48.dp),
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//            ) {
//                autocompleteResult?.let {
//                    items(it.items) {
//                        SuggestionChip(
//                            label = { Text(it.title) },
//                            onClick = { onAutocompleteClick(it) },
//                        )
//                    }
//                }
//            }
//        }

        HorizontalDivider()

        Text(mathExpressionFormatter(parsedText), modifier = Modifier.padding(start = 16.dp))

        Row{
            Spacer(Modifier.weight(1f))
            ResultSection(
                resultText,
                onDeleteClick = onDeleteClick,
                modifier = Modifier.padding(start = 16.dp).sharedElement(
                    rememberSharedContentState(key = "result_section"),
                    animatedVisibilityScope = animatedVisibilityScope
                )
            )
        }
    }
}


@Composable
fun InputTextField(
    inputFieldValue: TextFieldValue,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    modifier: Modifier = Modifier,
    onInputFieldValueChange: (TextFieldValue) -> Unit,
    onFocusChange: (FocusState) -> Unit = {},
    onSubmit: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier.padding(start = 16.dp)
    ) {
        BasicTextField(
            value = inputFieldValue,
            onValueChange = onInputFieldValueChange,
            modifier = Modifier
                .padding(vertical = 8.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { state -> onFocusChange(state) }
                .onPreviewKeyEvent {
                    // Enter key (on hardware keyboard) should not create a new line
                    // but submit the calculation
                    if (it.type == KeyEventType.KeyDown && it.key == Key.Enter) {
                        onSubmit()
                        true
                    } else {
                        false
                    }
                },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrectEnabled = false,
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Ascii
            ),
            keyboardActions = KeyboardActions(
                onAny = { onSubmit() },
            ),
            cursorBrush = SolidColor(LocalContentColor.current),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
        )

//        class MyPopupPositionProvider : PopupPositionProvider {
//            override fun calculatePosition(
//                anchorBounds: IntRect,
//                windowSize: IntSize,
//                layoutDirection: LayoutDirection,
//                popupContentSize: IntSize
//            ): IntOffset {
//                return anchorBounds.topLeft
//            }
//
//        }
//
//        Popup(
//            MyPopupPositionProvider()
//        ) {
//            Text("aaaaaa")
//        }

        if (inputFieldValue.text.isEmpty()) {
            Text(
                text = stringResource(R.string.textfield_hint),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
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

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Row(
            Modifier.weight(1f, fill=false)
        ) {
            Text(
                "= ",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                mathExpressionFormatter(resultText),
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
private fun EmptyCompactPreview() {
    CalculationItem(
        TextFieldValue(""),
        "0",
        "0",
        expanded = false
    )
}

@Preview(showBackground = true)
@Composable
private fun CompactShortPreview() {
    CalculationItem(
        TextFieldValue("1km + 5m"),
        "1 kilometer + 5 meter",
        "1.005 m",
        expanded = false
    )
}

@Preview(showBackground = true)
@Composable
private fun CompactOverflowPreview() {
    CalculationItem(
        TextFieldValue("boltzmann * planck"),
        "boltzmann*planck",
        "9.1482771E-57 second*joule^2/kelvin",
        expanded = false
    )
}

@Preview(showBackground = true)
@Composable
private fun CompactOverflowOverflowPreview() {
    CalculationItem(
        TextFieldValue("boltzmann * planck"),
        "boltzmann + planck",
        "6.626 070 15 × 10^(−34) Joule·seconds + 13.806 49 peta joule / terra kelvin",
        expanded = false
    )
}

@Preview(showBackground = true)
@Composable
private fun ExpandedShortPreview() {
    CalculationItem(
        TextFieldValue("1km + 5m"),
        "1 kilometer + 5 meter",
        "1.005 m",
        expanded = true
    )
}

@Preview(showBackground = true)
@Composable
private fun ExpandedOverflowPreview() {
    CalculationItem(
        TextFieldValue("boltzmann * planck"),
        "boltzmann*planck",
        "9.1482771E-57 second*joule^2/kelvin",
        expanded = true
    )
}
