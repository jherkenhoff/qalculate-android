package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Toolbar(
    modifier: Modifier = Modifier
) {

    Surface(
        shape = RoundedCornerShape(100),
        shadowElevation = 3.dp,
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = modifier.height(64.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            IconButton({}) {
                Icon(Icons.AutoMirrored.Default.Undo, null)
            }
            IconButton({}) {
                Icon(Icons.AutoMirrored.Default.Redo, null)
            }
            FilledIconToggleButton(false, {}) {
                Icon(Icons.Filled.Keyboard, null)
            }
            IconButton({}) {
                Icon(Icons.Default.MoreVert, null)
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
private fun DefaultPreview() {
    Toolbar()
}