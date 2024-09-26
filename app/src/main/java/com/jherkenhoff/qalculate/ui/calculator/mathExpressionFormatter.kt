package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.BaselineShift

@Composable
fun mathExpressionFormatter(
    text: String
): AnnotatedString {
    val tokens = Regex("""<.*?>|([^<]+)?|(&nbsp;)""").findAll(text)

    // TODO: Make font size for subscripts and superscripts smaller

    Log.d("", text)

    return buildAnnotatedString {

        for (token in tokens) {
            when (token.value) {
                "<i>" -> pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
                "</i>" -> pop()
                "<span style=\"color:#800000\">" -> pushStyle(SpanStyle(color = MaterialTheme.colorScheme.error))
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