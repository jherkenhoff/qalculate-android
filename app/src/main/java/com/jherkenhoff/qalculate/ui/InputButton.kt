package com.jherkenhoff.qalculate.ui

import android.text.Layout
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.ui.theme.QalculateTheme


@Composable
fun InputButton(
    primaryText : String,
    secondaryText : String
) {
    Button(onClick = { /*TODO*/ }) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = secondaryText, style=MaterialTheme.typography.bodySmall)
            Text(text = primaryText, style=MaterialTheme.typography.bodyLarge)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QalculateTheme {
        InputButton(primaryText = "1", secondaryText = "2")
    }
}