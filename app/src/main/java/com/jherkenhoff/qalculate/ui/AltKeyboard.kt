package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AltKeyboard(
    modifier: Modifier = Modifier,
    onKey: (String) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            AltKeyboardSelector(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            BasicAltKeyboard(
                onKey = onKey,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

val basicKeyboardKeys = arrayOf(
    arrayOf("7", "8", "9", "DEL", "AC"),
    arrayOf("4", "5", "6", "×", "÷"),
    arrayOf("1", "2", "3", "+", "-"),
    arrayOf("0", ".", "e", "Ans", "="),
)

@Composable
fun BasicAltKeyboard(
    modifier: Modifier = Modifier,
    onKey: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        for (row in basicKeyboardKeys) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (key in row) {
                    Button(
                        onClick = { onKey(key) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = key)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltKeyboardSelector(
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        SegmentedButton(
            selected = true,
            onClick = { /*TODO*/ },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
        ) {
            Text(text = "Basic")
        }
        SegmentedButton(
            selected = false,
            onClick = { /*TODO*/ },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
        ) {
            Text(text = "Func.")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    AltKeyboard()
}