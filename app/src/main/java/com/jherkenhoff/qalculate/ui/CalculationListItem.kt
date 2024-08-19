package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.CalculationItem(
    parsed: String,
    result: String
) {
    Text(
        messageFormatter(parsed),
        style = MaterialTheme.typography.bodyMedium
    )
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier.fillMaxWidth().defaultMinSize(minHeight = 80.dp)
    ) {
        Text(
            messageFormatter(result),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
        )
    }
}
@Composable
fun CalculationItem(
    parsed: String,
    result: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        CalculationItem(
            parsed,
            result
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CalculationItem(
        "1 kilometer + 5 meter",
        "1.005 m",
        modifier = Modifier.padding(16.dp)
    )
}
