package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun CalculationListItem(
    parsed: String,
    result: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(vertical = 10.dp, horizontal = 16.dp)) {
        Text(messageFormatter(parsed))
        Spacer(modifier = Modifier.weight(1f))
        Text(messageFormatter(result))
    }
}

@Composable
fun CalculationList(
    currentParsed: String,
    currentResult: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.weight(1f))
        DateDivider("Today")
        CalculationListItem("", "")
        CalculationListItem("", "")
        DateDivider("Now")
        CalculationListItem(currentParsed, currentResult)
    }
}

@Composable
private fun DateDivider(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}


@Composable
fun messageFormatter(
    text: String
): AnnotatedString {
    val tokens = Regex("""<.*?>|([^<]+)?|(&nbsp;)""").findAll(text)

    return buildAnnotatedString {

        for (token in tokens) {
            when (token.value) {
                "<i>" -> pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                "</i>" -> pop()
                "<span style=\"color:#005858\">" -> pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
                "<span style=\"color:#585800\">" -> pushStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary))
                "<span style=\"color:#008000\">" -> pushStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary))
                "</span>" -> pop()
                "<sup>" -> pushStyle(SpanStyle(baselineShift = BaselineShift.Superscript))
                "</sup>" -> pop()
                "<sub>" -> pushStyle(SpanStyle(baselineShift = BaselineShift.Subscript))
                "</sub>" -> pop()
                "&nbsp;" -> append("\n")
                else -> append(token.value)
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CalculationList("7 T", "7 T")
}