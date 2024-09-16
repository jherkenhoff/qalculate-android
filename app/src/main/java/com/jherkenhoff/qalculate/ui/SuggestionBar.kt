package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup

@Composable
fun SuggestionBar() {
    Row {
        SuggestionItem()
        SuggestionItem()
        SuggestionItem()
    }
}

@Composable
fun SuggestionItem() {
    Column {
        Text(text = "c")
        Text(text = "Speed of Light in Vacuum")
    }

}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    SuggestionBar()
}