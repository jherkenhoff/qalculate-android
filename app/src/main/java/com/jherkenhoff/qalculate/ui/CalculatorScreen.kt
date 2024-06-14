package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getTextAfterSelection
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    var inputTextFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(initialInputString))
    }

    val onInputChanged: (TextFieldValue) -> Unit = {
        inputTextFieldValue = it
        calculate(it.text)
    }

    val onNumpadButtonEvent: (String) -> Unit = {
        val start = inputTextFieldValue.selection.start
        val end = inputTextFieldValue.selection.end

        val text = inputTextFieldValue.text.slice(
            IntRange(
                0,
                start - 1
            )
        ) + it + inputTextFieldValue.text.slice(IntRange(end, inputTextFieldValue.text.length - 1))
        val selection = TextRange(start + it.length)
        inputTextFieldValue = TextFieldValue(text, selection)
    }

    Column(modifier = Modifier.background(color = MaterialTheme.colorScheme.background).imePadding().fillMaxSize()) {

        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
            CalculationList(
                parsedString,
                resultString,
                modifier = Modifier.weight(1.0f)
            )
            InputBar(
                inputTextFieldValue,
                onInputFieldValueChanged = onInputChanged
            )
        }

        QuickKeys(onKey = {
            inputTextFieldValue = inputTextFieldValue.addText(it)
            onInputChanged(inputTextFieldValue)
        })
    }
}




private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start,
        this.selection.end,
        newString
    )
    val newSelection = TextRange(
        start = newText.length,
        end = newText.length
    )

    return this.copy(text = newText, selection = newSelection)
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CalculatorScreenContent(
        "<i>asd",
        "1",
        initialInputString = "7 T"
    )
}