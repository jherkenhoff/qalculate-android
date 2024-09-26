package com.jherkenhoff.qalculate.ui.calculator

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ColumnScope.CalculationListItem(
    parsed: String,
    result: String,
    compact: Boolean = true
) {

    Text(
        mathExpressionFormatter(parsed),
        style = MaterialTheme.typography.bodyMedium
    )
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 80.dp)
    ) {
        AutoSizeText(
            text = mathExpressionFormatter(result),
            modifier = Modifier.fillMaxWidth(),
            alignment = Alignment.CenterEnd,
            style = MaterialTheme.typography.displayMedium,
            minTextSize = 14.sp,
            maxTextSize = 50.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun CalculationListItem(
    parsed: String,
    result: String,
    modifier: Modifier = Modifier,
    compact: Boolean = true,
) {
    Column(
        modifier = modifier
    ) {
        CalculationListItem(
            parsed,
            result
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CalculationListItem(
        "1 kilometer + 5 meter",
        "1.005 m",
        modifier = Modifier.padding(16.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewWithLongResult() {
    CalculationListItem(
        "k<sub>B</sub> + c",
        "13.80649 pJ / TK + 299.79246 km / ms",
        modifier = Modifier.padding(16.dp)
    )
}