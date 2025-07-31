package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AltKeyboard(
    modifier: Modifier = Modifier,
    onKey: (String) -> Unit = {},
    onDel: () -> Unit = {},
    onAC: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    BasicAltKeyboard(
        onKey = onKey,
        onDel = onDel,
        onAC = onAC,
        onSubmit = onSubmit,
        modifier = modifier
    )
}

val basicKeyboardKeys = arrayOf(
    arrayOf("7", "8", "9", "DEL", "AC"),
    arrayOf("4", "5", "6", "×", "÷"),
    arrayOf("1", "2", "3", "+", "-"),
    arrayOf("0", ".", "e", "Ans", "="),
)

@Composable
fun BasicAltKeyboard(
    modifier: Modifier = Modifier,
    onKey: (String) -> Unit = {},
    onDel: () -> Unit = {},
    onAC: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AltKeyboardButton(text="7", onClick={onKey("7")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="8", onClick={onKey("8")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="9", onClick={onKey("9")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="⌫", onClick=onDel, color = MaterialTheme.colorScheme.tertiaryContainer)
            AltKeyboardButton(text="AC", onClick=onAC, color = MaterialTheme.colorScheme.tertiaryContainer)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AltKeyboardButton(text="4", onClick={onKey("4")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="5", onClick={onKey("5")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="6", onClick={onKey("6")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="×", onClick={onKey("×")}, color = MaterialTheme.colorScheme.primaryContainer)
            AltKeyboardButton(text="÷", onClick={onKey("÷")}, color = MaterialTheme.colorScheme.primaryContainer)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AltKeyboardButton(text="1", onClick={onKey("1")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="2", onClick={onKey("2")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="3", onClick={onKey("3")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="+", onClick={onKey("+")}, color = MaterialTheme.colorScheme.primaryContainer)
            AltKeyboardButton(text="-", onClick={onKey("-")}, color = MaterialTheme.colorScheme.primaryContainer)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AltKeyboardButton(text="0", onClick={onKey("0")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text=".", onClick={onKey(".")}, color = MaterialTheme.colorScheme.secondaryContainer)
            AltKeyboardButton(text="e", onClick={onKey("e")}, color = MaterialTheme.colorScheme.primaryContainer)
            AltKeyboardButton(text="Ans", onClick={}, color = MaterialTheme.colorScheme.primaryContainer)
            AltKeyboardButton(icon=Icons.AutoMirrored.Filled.KeyboardReturn, description="Return button", onClick={onSubmit()}, color = MaterialTheme.colorScheme.primaryContainer)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AltKeyboardSelector(
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier
    ) {
        SegmentedButton(
            selected = true,
            onClick = { /*TODO*/ },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
        ) {
            Text(text = "Basic")
        }
        SegmentedButton(
            selected = false,
            onClick = { /*TODO*/ },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
        ) {
            Text(text = "Functions")
        }
    }
}

@Composable
fun RowScope.AltKeyboardButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color
) {
    Button(
        onClick = onClick,
        modifier = modifier.weight(1f).height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(4.dp)
    ) {
        Text(text, style = MaterialTheme.typography.headlineSmall, color = contentColorFor(color))
    }
}

@Composable
fun RowScope.AltKeyboardButton(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary
) {
    Button(
        onClick = onClick,
        modifier = modifier.weight(1f).height(64.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        contentPadding = PaddingValues(4.dp)
    ) {
        Icon(
            icon,
            contentDescription = description,
            tint = contentColorFor(color)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    AltKeyboard()
}