package com.jherkenhoff.qalculate.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.em

@Composable
fun mathExpressionFormatter(
    text: String,
    color: Boolean = true
): AnnotatedString {
    val tokens = Regex("""<.*?>|(&[a-z]+;)+|([^<&]+)?""").findAll(text)

    // TODO: Implement <small> tags. (Apparently only used for base designation? https://github.com/Qalculate/libqalculate/blob/21f28b27bf99dc6d9f3325c4960a92ec9ee8934d/libqalculate/MathStructure-print.cc#L3604 )

    return buildAnnotatedString {

        for (token in tokens) {
            when (token.value) {
                "<i>" -> pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                "</i>" -> pop()
                "<span style=\"color:#800000\">" -> if (color) pushStyle(SpanStyle(color = MaterialTheme.colorScheme.error))
                "<span style=\"color:#005858\">" -> if (color) pushStyle(SpanStyle(color = MaterialTheme.colorScheme.primary))
                "<span style=\"color:#585800\">" -> if (color) pushStyle(SpanStyle(color = MaterialTheme.colorScheme.secondary))
                "<span style=\"color:#008000\">" -> if (color) pushStyle(SpanStyle(color = MaterialTheme.colorScheme.tertiary))
                "</span>" -> if (color) pop()
                "<sup>" -> pushStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 0.7.em))
                "</sup>" -> pop()
                "<sub>" -> pushStyle(SpanStyle(baselineShift = BaselineShift.Subscript, fontSize = 0.7.em))
                "</sub>" -> pop()
                "&nbsp;" -> append("")
                "&lt;" -> append("<")
                "&gt;" -> append(">")
                "&amp;" -> append("&")
                else -> append(token.value)
            }
        }

    }
}
