package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme

@Composable
fun HistoryEntry() {
    Column(
        Modifier.fillMaxWidth()
    ) {
        Text(
            text="Moin",
            style= MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun DefaultPreview() {
    QalculateTheme {
        HistoryEntry()
    }
}