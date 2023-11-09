package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme

@Composable
fun InputSection(
    paddingValues : PaddingValues = PaddingValues()
) {
    Box (
        Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Row() {
            InputButton(primaryText = "2", secondaryText = "1")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QalculateTheme {
        InputSection(PaddingValues(top=10.dp))
    }
}