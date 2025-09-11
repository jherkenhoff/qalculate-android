package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorTopBar(
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {  },
    onSettingsClick: () -> Unit = {  }
) {
    Surface(
        modifier,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column() {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeContent))
            Row {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Open navigation menu"
                    )
                }
                Spacer(Modifier.weight(1f))

                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onSettingsClick() }) { Icon(Icons.Default.Settings, contentDescription = "Open settings") }
            }

            Row {
                Spacer(Modifier.weight(1f))
                AssistChip(onClick = {  /*TODO*/}, label = { Text("Degrees") }, modifier = Modifier.padding(horizontal = 3.dp))
                AssistChip(onClick = { /*TODO*/ }, label = { Text("Scientific") }, modifier = Modifier.padding(horizontal = 3.dp))
                FilterChip(
                    onClick = { /*TODO*/ },
                    label = { Text("Exact") },
                    modifier = Modifier.padding(horizontal = 3.dp),
                    colors = FilterChipDefaults.filterChipColors().copy(selectedContainerColor = MaterialTheme.colorScheme.secondary, selectedLabelColor = MaterialTheme.colorScheme.onSecondary),
                    selected = true,
                )
            }
        }
    }
}


@Composable
fun RadioButtonRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth()
            .height(48.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp))
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    CalculatorTopBar(
    )
}