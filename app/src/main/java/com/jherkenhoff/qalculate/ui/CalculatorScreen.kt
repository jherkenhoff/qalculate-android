package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jherkenhoff.qalculate.ui.theme.TopSheetShape

@Composable
fun CalculatorScreen(viewModel: MainViewModel = viewModel()) {
    CalculatorScreenContent(
        viewModel.parsedString.value,
        viewModel.resultString.value,
        viewModel::calculate
    )
}
@Composable
fun CalculatorScreenContent(
    parsedString: String,
    resultString: String,
    calculate: (String) -> Unit = {},
    initialInputString: String = ""
) {
    var inputTextFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue(initialInputString)) }

    val onInputChanged: (TextFieldValue) -> Unit = {
        inputTextFieldValue = it
        calculate(it.text)
    }

    val onNumpadButtonEvent: (String) -> Unit = {
        val start = inputTextFieldValue.selection.start
        val end = inputTextFieldValue.selection.end

        val text = inputTextFieldValue.text.slice(IntRange(0, start-1)) + it + inputTextFieldValue.text.slice(IntRange(end, inputTextFieldValue.text.length-1))
        val selection = TextRange(start + it.length)
        inputTextFieldValue = TextFieldValue(text, selection)
    }

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Surface(
                shape = TopSheetShape,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Column {
                    Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
                    PromptSection(
                        inputTextFieldValue,
                        parsedString,
                        resultString,
                        onInputChanged
                    )
                }
            }

            Column {
                Numpad(onButtonEvent = onNumpadButtonEvent)
                Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CalculatorScreenContent(
        "cos(0)",
        "1",
        initialInputString = "cos(0)"
    )
}