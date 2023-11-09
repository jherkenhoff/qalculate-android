package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme

@Composable
fun HistoryList() {
    Row(
        Modifier.fillMaxWidth()
    ) {
        HistoryEntry()
        HistoryEntry()
    }
}

@Preview(showBackground = false)
@Composable
private fun DefaultPreview() {
    QalculateTheme {
        HistoryList()
    }
}