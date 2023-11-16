package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CalculatorScreen() {
    AppScaffold(
        topPanel = { PromptSection() },
        inputSection = { paddingValues ->  Numpad(modifier= Modifier.padding(paddingValues)) }
    )
}