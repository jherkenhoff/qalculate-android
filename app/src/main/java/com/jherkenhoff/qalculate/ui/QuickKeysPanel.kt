package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuickKeysPanel(
    //keys: Array<Array<CalculatorKeyAction>>?,
    onKeyAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            for (i in arrayOf("_", "=", "√", "^", "(", ")", "÷", "×", "-", "+")) {
                TextButton(
                    onClick = {onKeyAction(i)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(text = i, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            for (i in intArrayOf(1,2,3,4,5,6,7,8,9,0)) {
                TextButton(
                    onClick = {onKeyAction(i.toString())},
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



@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QuickKeysPanel(onKeyAction = {})
}