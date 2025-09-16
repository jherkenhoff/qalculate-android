package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.jherkenhoff.qalculate.ui.common.SingleEnumSelectDialog

@Composable
fun SettingsListItem(
    title: String
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text("Description")},
        trailingContent = { Switch(checked = false, onCheckedChange = {  })  }
    )
    Dialog(
        {}
    ) {

    }
}

@Composable
inline fun <reified T : Enum<T>> SingleEnumSelectSettingsListItem(
    title: String,
    crossinline enumLabelMap: (T) -> String,
    currentSelection: T,
    crossinline onSelect: (T) -> Unit
) {
    var dialogOpen by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(enumLabelMap(currentSelection))},
        modifier = Modifier.clickable { dialogOpen = true }
    )

    if (dialogOpen) {
        SingleEnumSelectDialog<T>(
            title = title,
            enumLabelMap = enumLabelMap,
            currentSelection = currentSelection,
            onSelect = { dialogOpen = false; onSelect(it) },
            onDismissRequest = { dialogOpen = false },
        )
    }
}