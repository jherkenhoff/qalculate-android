package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuickKeys(
    onKey: (String) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                for (i in arrayOf("_", "=", "√", "^", "(", ")", "÷", "×", "-", "+")) {
                    TextButton(
                        onClick = {onKey(i)},
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(text = i.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                for (i in intArrayOf(1,2,3,4,5,6,7,8,9,0)) {
                    TextButton(
                        onClick = {onKey(i.toString())},
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text(text = i.toString(), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

    }
}



@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QuickKeys(onKey = {})
}