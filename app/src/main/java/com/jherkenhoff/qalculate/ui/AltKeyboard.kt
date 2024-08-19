package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AltKeyboard(
    modifier: Modifier = Modifier,
    onKey: (String) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            AltKeyboardSelector(
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            BasicAltKeyboard(
                onKey = onKey,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
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
    onKey: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AltKeyboardButton(text="7", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="8", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="9", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="⌫", onClick={}, color = MaterialTheme.colorScheme.error)
            AltKeyboardButton(text="AC", onClick={}, color = MaterialTheme.colorScheme.error)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AltKeyboardButton(text="4", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="5", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="6", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="×", onClick={}, color = MaterialTheme.colorScheme.primary)
            AltKeyboardButton(text="÷", onClick={}, color = MaterialTheme.colorScheme.primary)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AltKeyboardButton(text="1", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="2", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="3", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="+", onClick={}, color = MaterialTheme.colorScheme.primary)
            AltKeyboardButton(text="-", onClick={}, color = MaterialTheme.colorScheme.primary)
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AltKeyboardButton(text="0", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text=".", onClick={}, color = MaterialTheme.colorScheme.secondary)
            AltKeyboardButton(text="e", onClick={}, color = MaterialTheme.colorScheme.primary)
            AltKeyboardButton(text="Ans", onClick={}, color = MaterialTheme.colorScheme.primary)
            AltKeyboardButton(text="=", onClick={}, color = MaterialTheme.colorScheme.primary)
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
    color: Color = MaterialTheme.colorScheme.secondary
) {
    Button(
        onClick = onClick,
        modifier = modifier.weight(1f).height(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    AltKeyboard()
}