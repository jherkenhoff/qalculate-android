
package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuickKeysPanel(
    onKeyAction: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    fun onClick(preCursorText: String, postCursorText: String = "") {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onKeyAction(preCursorText, postCursorText)
    }

    val row1Height = 56.dp
    val row2Height = 56.dp
    Column(
        modifier = modifier
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .height(row1Height + row2Height + 24.dp), // 24.dp for spacing
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row 1: arithmetic symbols
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            QuickKeyButton("(", buttonHeight = row1Height, fontSize = 26, onClick = { onClick("(") })
            QuickKeyButton(")", buttonHeight = row1Height, fontSize = 26, onClick = { onClick(")") })
            QuickKeyButton("÷", buttonHeight = row1Height, fontSize = 26, onClick = { onClick("÷") })
            QuickKeyButton("×", buttonHeight = row1Height, fontSize = 26, onClick = { onClick("×") })
            QuickKeyButton("-", buttonHeight = row1Height, fontSize = 26, onClick = { onClick("-") })
            QuickKeyButton("+", buttonHeight = row1Height, fontSize = 26, onClick = { onClick("+") })
            QuickKeyButton("=", buttonHeight = row1Height, fontSize = 26, onClick = { onClick("=") })
        }
        // Row 2: numbers (full width, easier to tap)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            for (i in 1..9) {
                QuickKeyButton(i.toString(), buttonHeight = row2Height, fontSize = 24, onClick = { onClick(i.toString()) })
            }
            QuickKeyButton("0", buttonHeight = row2Height, fontSize = 24, onClick = { onClick("0") })
        }
    }
}


@Composable
fun QuickKeysPanel2(
    onKeyAction: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    fun onClick(preCursorText: String, postCursorText: String = "") {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        onKeyAction(preCursorText, postCursorText)
    }

    val row1Height = 56.dp
    val row2Height = 56.dp
    Column(
        modifier = modifier
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .height(row1Height + row2Height + 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Row 1: log and trig functions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            QuickKeyButton("log", buttonHeight = row1Height, fontSize = 20, onClick = { onClick("log(", ")") })
            QuickKeyButton("ln", buttonHeight = row1Height, fontSize = 20, onClick = { onClick("ln(", ")") })
            QuickKeyButton("sin", buttonHeight = row1Height, fontSize = 20, onClick = { onClick("sin(", ")") })
            QuickKeyButton("cos", buttonHeight = row1Height, fontSize = 20, onClick = { onClick("cos(", ")") })
            QuickKeyButton("tan", buttonHeight = row1Height, fontSize = 20, onClick = { onClick("tan(", ")") })
        }
        // Row 2: advanced symbols
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            QuickKeyButton("[", buttonHeight = row2Height, fontSize = 20, onClick = { onClick("[") })
            QuickKeyButton("]", buttonHeight = row2Height, fontSize = 20, onClick = { onClick("]") })
            QuickKeyButton("Σ", buttonHeight = row2Height, fontSize = 20, onClick = { onClick("Σ(", ")") })
            QuickKeyButton("∫", buttonHeight = row2Height, fontSize = 20, onClick = { onClick("∫(", ")") })
            QuickKeyButton("dx", buttonHeight = row2Height, fontSize = 20, onClick = { onClick("diff(", ")") })
            QuickKeyButton("∞", buttonHeight = row2Height, fontSize = 20, onClick = { onClick("∞") })
            QuickKeyButton("±", buttonHeight = row2Height, fontSize = 20, onClick = { onClick("±") })
            QuickKeyButton("!", buttonHeight = row2Height, fontSize = 20, onClick = { onClick("!") })
        }
    }
}

@Composable
fun RowScope.QuickKeyButton(
    text: String,
    buttonHeight: androidx.compose.ui.unit.Dp = 52.dp,
    fontSize: Int = 22,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    androidx.compose.material3.ElevatedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .weight(1f)
            .height(buttonHeight),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = fontSize.sp),
            maxLines = 1,
            softWrap = false,
            overflow = androidx.compose.ui.text.style.TextOverflow.Clip,
            modifier = Modifier
                .fillMaxWidth()
                .align(androidx.compose.ui.Alignment.CenterVertically),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    QuickKeysPanel(onKeyAction = { _, _ -> })
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview2() {
    QuickKeysPanel2(onKeyAction = { _, _ -> })
}