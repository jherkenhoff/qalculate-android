package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.common.SegmentedListItem
import com.jherkenhoff.qalculate.ui.common.SingleEnumSelectDialog


@Composable
inline fun <reified T : Enum<T>> SingleEnumSelectSettingsListItem(
    title: String,
    crossinline enumLabelMap: (T) -> String,
    currentSelection: T,
    crossinline onSelect: (T) -> Unit,
    top: Boolean = false,
    bottom: Boolean = false
) {
    var dialogOpen by remember { mutableStateOf(false) }

    SegmentedListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(enumLabelMap(currentSelection))},
        modifier = Modifier.clickable { dialogOpen = true },
        top = top,
        bottom = bottom,
        colors = ListItemDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainer)
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