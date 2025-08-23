package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem

@Composable
fun AuxiliaryBar(
    autocompleteResult: AutocompleteResult?,
    onAutocompleteClick: (AutocompleteItem) -> Unit = {},
    keyboardEnable: Boolean,
    onKeyboardEnableChange: (Boolean) -> Unit = {}
) {

    AnimatedContent(autocompleteResult != null && autocompleteResult.items.isNotEmpty()) {
        if (it) {
            LazyRow(
                modifier = Modifier.padding(horizontal = 8.dp).height(48.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                autocompleteResult?.let { it ->
                    items(it.items) { it ->
                        SuggestionChip(
                            label = { Text(it.name) },
                            onClick = { onAutocompleteClick(it) }
                        )
                    }
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilledIconToggleButton(keyboardEnable, onKeyboardEnableChange) {
                    Icon(Icons.Filled.Keyboard, null)
                }
                IconButton({}) {
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowLeft, null)
                }
                IconButton({}) {
                    Icon(Icons.AutoMirrored.Default.KeyboardArrowRight, null)
                }
                IconButton({}) {
                    Icon(Icons.AutoMirrored.Default.Undo, null)
                }
                IconButton({}) {
                    Icon(Icons.AutoMirrored.Default.Redo, null)
                }
                IconButton({}) {
                    Icon(Icons.Default.MoreVert, null)
                }
            }
        }
    }
}