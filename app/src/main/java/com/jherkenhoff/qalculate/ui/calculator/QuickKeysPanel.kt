package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuickKeysPanel(
    onKeyAction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    fun onClick(text: String) {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onKeyAction(text)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            for (i in arrayOf("_", "=", "√", "^", "(", ")", "÷", "×", "-", "+")) {
                TextButton(
                    onClick = {onClick(i)},
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = i,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            for (i in intArrayOf(1,2,3,4,5,6,7,8,9,0)) {
                TextButton(
                    onClick = {onClick(i.toString())},
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = i.toString(),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
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