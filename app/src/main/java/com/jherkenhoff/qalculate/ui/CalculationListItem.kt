package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ParagraphIntrinsics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.createFontFamilyResolver
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.inidamleader.ovtracker.util.compose.AutoSizeText

@Composable
fun ColumnScope.CalculationListItem(
    parsed: String,
    result: String,
    compact: Boolean = true
) {

    Text(
        messageFormatter(parsed),
        style = MaterialTheme.typography.bodyMedium
    )
    Box(
        contentAlignment = Alignment.CenterEnd,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 80.dp)
    ) {
        AutoSizeText(
            text = messageFormatter(result),
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