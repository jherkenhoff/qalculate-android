package com.jherkenhoff.qalculate.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun AltKeyboard() {
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltKeyboardSelector() {

    SingleChoiceSegmentedButtonRow {
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