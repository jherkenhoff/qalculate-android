package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter


private val HistoryItemShape = RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
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
                inputString = inputText,
                parsedString = parsedText,
                resultString = resultText,
                onTextToInput = { onTextToInput(it); detailDialogOpen = false }
            )
        }
    }

//    SwipeToDismissBox(
//        state = dismissState,
//        backgroundContent = {
//            val color by animateColorAsState(
//                when (dismissState.targetValue) {
//                    SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surfaceContainer
//                    SwipeToDismissBoxValue.StartToEnd -> MaterialTheme.colorScheme.errorContainer
//                    SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.secondaryContainer
//                }
//            )
//            Box(
//                Modifier
//                    .fillMaxSize()
//                    .background(color, shape = HistoryItemShape))
//        }
//
//    ) {
    Surface(
        shape = HistoryItemShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.combinedClickable(
            onClick = { detailDialogOpen = true },
            onLongClick = {  }
        )
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                mathExpressionFormatter(parsedText, color=false),
                style = MaterialTheme.typography.bodySmall
            )
            AutoSizeText(
                text = mathExpressionFormatter(resultText),
                alignment = Alignment.CenterEnd,
                style = MaterialTheme.typography.displayMedium,
                minTextSize = 14.sp,
                maxTextSize = 30.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }

}


@Preview()
@Composable
private fun DefaultPreview() {
    HistoryItem(
        "1km + 5m",
        "1 kilometer + 5 meter",
        "1.005 m"
    )
}

@Preview()
@Composable
private fun DefaultPreview2() {
    HistoryItem(
        "c",
        "SpeedOfLight",
        "299.792 458 km/ms"
    )
}