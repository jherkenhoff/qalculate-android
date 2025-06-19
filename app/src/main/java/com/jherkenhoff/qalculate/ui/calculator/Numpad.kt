package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun Numpad(
    expansionRatio: Float,
    modifier: Modifier = Modifier,
    onKey: (String) -> Unit = {},
    onDel: () -> Unit = {},
    onAC: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    KeypadLayout(
        expansionRatio = expansionRatio,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.layoutId("extended"),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButtonSmall(text = "x", onClick = { onKey("_") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "", onClick = { onKey("=") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "", onClick = { onKey("sqrt()") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "", onClick = { onKey("^") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "log", onClick = { onKey(")") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "ln", onClick = { onKey(")") }, color = MaterialTheme.colorScheme.surfaceVariant)
        }
        Row(
            modifier = Modifier.layoutId("extended"),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButtonSmall(text = "π", onClick = { onKey("_") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "e", onClick = { onKey("=") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "i", onClick = { onKey("sqrt()") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "sin", onClick = { onKey("^") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "cos", onClick = { onKey(")") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "tan", onClick = { onKey(")") }, color = MaterialTheme.colorScheme.surfaceVariant)
        }
        Row(
            modifier = Modifier.layoutId("auxiliary"),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButtonSmall(text = "%", onClick = { onKey("%") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "±", onClick = { onKey("pi") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "", onClick = { onKey("sqrt()") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "", onClick = { onKey("^") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "", onClick = { onKey(")") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "", onClick = { onKey(")") }, color = MaterialTheme.colorScheme.surfaceVariant)
        }
        Row(
            modifier = Modifier.layoutId("auxiliary"),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KeypadButtonSmall(text = "_", onClick = { onKey("_") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "=", onClick = { onKey("=") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "√", onClick = { onKey("sqrt()") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "^", onClick = { onKey("^") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = "(", onClick = { onKey(")") }, color = MaterialTheme.colorScheme.surfaceVariant)
            KeypadButtonSmall(text = ")", onClick = { onKey(")") }, color = MaterialTheme.colorScheme.surfaceVariant)
        }
        Row(modifier = Modifier.layoutId("numpad"), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeypadButtonLarge(text="7", onClick={onKey("7")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="8", onClick={onKey("8")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="9", onClick={onKey("9")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="⌫", onClick=onDel, color = MaterialTheme.colorScheme.tertiaryContainer)
            KeypadButtonLarge(text="AC", onClick=onAC, color = MaterialTheme.colorScheme.tertiaryContainer)
        }
        Row(modifier = Modifier.layoutId("numpad"), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeypadButtonLarge(text="4", onClick={onKey("4")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="5", onClick={onKey("5")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="6", onClick={onKey("6")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="×", onClick={onKey("×")}, color = MaterialTheme.colorScheme.primaryContainer)
            KeypadButtonLarge(text="÷", onClick={onKey("÷")}, color = MaterialTheme.colorScheme.primaryContainer)
        }
        Row(modifier = Modifier.layoutId("numpad"), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeypadButtonLarge(text="1", onClick={onKey("1")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="2", onClick={onKey("2")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="3", onClick={onKey("3")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="+", onClick={onKey("+")}, color = MaterialTheme.colorScheme.primaryContainer)
            KeypadButtonLarge(text="-", onClick={onKey("-")}, color = MaterialTheme.colorScheme.primaryContainer)
        }
        Row(modifier = Modifier.layoutId("numpad"), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeypadButtonLarge(text="0", onClick={onKey("0")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text=".", onClick={onKey(".")}, color = MaterialTheme.colorScheme.secondaryContainer)
            KeypadButtonLarge(text="e", onClick={onKey("e")}, color = MaterialTheme.colorScheme.primaryContainer)
            KeypadButtonLarge(text="Ans", onClick={}, color = MaterialTheme.colorScheme.primaryContainer)
            KeypadButtonLarge(icon=Icons.AutoMirrored.Filled.KeyboardReturn, description="Return button", onClick={onSubmit()}, color = MaterialTheme.colorScheme.primaryContainer)
        }
    }
}

@Composable
fun RowScope.KeypadButtonLarge(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary
) {
    Button(
        onClick = onClick,
        modifier = modifier.weight(1f).fillMaxHeight(),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
    }
}


@Composable
fun RowScope.KeypadButtonSmall(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary
) {
    Button(
        onClick = onClick,
        modifier = modifier.weight(1f).fillMaxHeight(),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(text, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onSurface, maxLines = 1)
    }
}

@Composable
fun RowScope.KeypadButtonLarge(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary
) {
    Button(
        onClick = onClick,
        modifier = modifier.weight(1f).fillMaxHeight(),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = description,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BasicPreview() {
    Numpad(
        expansionRatio = 0f
    )
}