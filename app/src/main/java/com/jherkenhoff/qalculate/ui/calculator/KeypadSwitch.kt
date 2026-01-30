package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.qalculate.model.KeypadSpec

@Composable
fun KeypadSwitch(
    keypads: List<KeypadSpec>,
    activeKeypad: Int,
    modifier: Modifier = Modifier,
    onKeypadChanged: (Int) -> Unit = {},
    compact: Boolean = false
) {
    var expanded by remember{ mutableStateOf(false) }

    val activeKeypad = keypads[activeKeypad]

    Box(modifier) {
        TextButton({ expanded = true }) {
            if (activeKeypad.icon !== null) {
                Icon(activeKeypad.icon, null)
            }
            AnimatedVisibility(!compact) {
                Row{
                    Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                    Text(activeKeypad.name)
                }
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            keypads.mapIndexed { i, keypad ->
                DropdownMenuItem(
                    text = { Text(keypad.name) },
                    onClick = { onKeypadChanged(i) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    KeypadSwitch(
        listOf(KeypadSpec(
            name = "Advanced",
            icon = Icons.Default.Science,
            sections = emptyList(),
            imeEnabled = false
        )),
        0
    )
}