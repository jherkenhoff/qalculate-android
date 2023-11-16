package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme

@Composable
fun QalculateApp() {
    QalculateTheme {
        CalculatorScreen()
    }
}

@Preview(showBackground = false)
@Composable
private fun DefaultPreview() {
    QalculateApp()
}