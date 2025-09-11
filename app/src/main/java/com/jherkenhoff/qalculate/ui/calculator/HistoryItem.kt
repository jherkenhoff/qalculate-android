package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter


@Composable
fun HistoryItem(
    inputText: String,
    parsedText: String,
    resultText: String,
    modifier: Modifier = Modifier,
    onTextToInput: (String) -> Unit = {},
) {

    var detailDialogOpen by remember { mutableStateOf(false) }

    val dismissState = rememberSwipeToDismissBoxState()

    AnimatedVisibility(visible = detailDialogOpen) {
        Dialog(
            onDismissRequest = { detailDialogOpen = false }
        ) {
            HistoryItemDetailCard(
                inputString = "",
                parsedString = parsedText,
                resultString = resultText,
                onTextToInput = { onTextToInput(it); detailDialogOpen = false }
            )
        }
    }

    androidx.compose.material3.Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            SelectionContainer {
                AutoSizeText(
                    text = mathExpressionFormatter(parsedText),
                    alignment = Alignment.CenterEnd,
                    style = MaterialTheme.typography.bodyLarge,
                    minTextSize = 14.sp,
                    maxTextSize = 24.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            SelectionContainer {
                AutoSizeText(
                    text = mathExpressionFormatter(resultText),
                    alignment = Alignment.CenterEnd,
                    style = MaterialTheme.typography.titleLarge,
                    minTextSize = 14.sp,
                    maxTextSize = 28.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    HistoryItem(
        "1km + 5m",
        "1 kilometer + 5 meter",
        "1.005 m"
    )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview2() {
    HistoryItem(
        "c",
        "SpeedOfLight",
        "299.792 458 km/ms"
    )
}