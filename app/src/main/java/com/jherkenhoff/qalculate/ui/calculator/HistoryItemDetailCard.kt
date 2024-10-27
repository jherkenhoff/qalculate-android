package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.SaveAlt
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.common.mathExpressionFormatter
import com.jherkenhoff.qalculate.ui.common.mathExpressionPlainText


private val HistoryItemShape = RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryItemDetailCard(
    inputString: String,
    parsedString: String,
    resultString: String,
    modifier: Modifier = Modifier,
    onTextToInput: (String) -> Unit = {}
) {
    val clipboardManager = LocalClipboardManager.current

    val annotatedInputString = mathExpressionFormatter(inputString)
    val annotatedParsedString = mathExpressionFormatter(parsedString)
    val annotatedResultString = mathExpressionFormatter(resultString)

    fun handleTextToInput(text: String) {
        onTextToInput(mathExpressionPlainText(text))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
    ) {
        ListItem(
            headlineContent = { Text(annotatedInputString) },
            overlineContent = { Text("Input") },
            trailingContent = {
                Row {
                    IconButton(onClick = { clipboardManager.setText(annotatedInputString) }) { Icon(Icons.Default.ContentCopy, contentDescription = "Copy input") }
                    IconButton(onClick = { handleTextToInput(inputString) }) { Icon(Icons.Default.Keyboard, contentDescription = "Copy input") } // Edit, EditNote, ContentPasteGo, Keyboard, Output
                }
            }
        )
        ListItem(
            headlineContent = { Text(annotatedParsedString) },
            overlineContent = { Text("Parsed") },
            trailingContent = {
                Row {
                    IconButton(onClick = { clipboardManager.setText(annotatedParsedString) }) { Icon(Icons.Default.ContentCopy, contentDescription = "Copy input") }
                    IconButton(onClick = { handleTextToInput(parsedString) }) { Icon(Icons.Default.Keyboard, contentDescription = "Copy input") } // Edit, EditNote, ContentPasteGo, Keyboard, Output
                }
            }
        )
        ListItem(
            headlineContent = { Text(annotatedResultString) },
            overlineContent = { Text("Result") },
            trailingContent = {
                Row {
                    // TODO: Implement "save to variable" functionality
                    //IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Default.SaveAlt, contentDescription = "Save to variable") }
                    IconButton(onClick = { clipboardManager.setText(annotatedResultString) }) { Icon(Icons.Default.ContentCopy, contentDescription = "Copy input") }
                    IconButton(onClick = { handleTextToInput(resultString) }) { Icon(Icons.Default.Keyboard, contentDescription = "Copy input") } // Edit, EditNote, ContentPasteGo, Keyboard, Output
                }
            }
        )

        HorizontalDivider()

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { /*TODO*/ },
            ) {
                Icon(Icons.Outlined.Bookmarks, contentDescription = "Bookmark history entry")
            }
            IconButton(
                onClick = { /*TODO*/ },
                colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete history entry")
            }
        }
    }
}

@Composable
private fun DetailListEntry(
    text: String,
    overlineText: String
) {
    ListItem(
        headlineContent = { Text(text) },
        overlineContent = { Text(overlineText) },
        trailingContent = {
            Row {
                IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Default.SaveAlt, contentDescription = "Save to variable") }
                IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Default.ContentCopy, contentDescription = "Copy input") }
                IconButton(onClick = { /*TODO*/ }) { Icon(Icons.Default.Keyboard, contentDescription = "Copy input") } // Edit, EditNote, ContentPasteGo, Keyboard, Output

            }
        }
    )
}


@Preview()
@Composable
private fun DefaultPreview() {
    HistoryItemDetailCard(
        "c",
        "SpeedOfLight",
        "299.792 458 km/ms"
    )
}