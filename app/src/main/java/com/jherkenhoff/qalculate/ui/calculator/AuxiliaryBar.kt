package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.domain.AutocompleteResult
import com.jherkenhoff.qalculate.model.AutocompleteItem
import com.jherkenhoff.qalculate.model.KeyAction
import com.jherkenhoff.qalculate.model.KeyLabel
import com.jherkenhoff.qalculate.model.Keys

private val auxiliaryKeys = arrayOf(Keys.keyLeft, Keys.keyRight, Keys.keyUndo, Keys.keyRedo)

@Composable
fun AuxiliaryBar(
    autocompleteResult: AutocompleteResult,
    onAutocompleteClick: (AutocompleteItem) -> Unit = {},
    keyboardEnable: Boolean,
    onKeyboardEnableChange: (Boolean) -> Unit = {},
    onKeyAction: (KeyAction) -> Unit = {},
) {
    val fadeWidth = 40f

    var autocompleteDismissed by remember{ mutableStateOf(false) }

    if (autocompleteResult.relevantText.isEmpty()) {
        autocompleteDismissed = false
    }

    AnimatedContent(!autocompleteDismissed && autocompleteResult.items.isNotEmpty()) {
        if (it) {
            Row {
                LazyRow(
                    modifier = Modifier.height(48.dp).weight(1f)
                        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                        .drawWithContent {
                            drawContent()
                            drawRect(brush = Brush.horizontalGradient(0f to Color.White, 1f to Color.Transparent, startX = this.size.width-fadeWidth, endX = this.size.width), blendMode = BlendMode.DstIn)
                            drawRect(brush = Brush.horizontalGradient(0f to Color.Transparent, 1f to Color.White, startX = 0f, endX = fadeWidth), blendMode = BlendMode.DstIn)
                        },
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item{
                        Spacer(Modifier.width(fadeWidth.toDp()))
                    }
                    items(autocompleteResult.items) { it ->
                        SuggestionChip(
                            label = { Text(it.name) },
                            onClick = { onAutocompleteClick(it) }
                        )
                    }
                    item{
                        Spacer(Modifier.width(fadeWidth.toDp()))
                    }
                }

                IconButton({ autocompleteDismissed = true }) {
                    Icon(Icons.Default.Close, "Dismiss autocomplete suggestions")
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
                for (key in auxiliaryKeys) {
                    IconButton({ key.clickAction?.let { onKeyAction(it) } }) {
                        when (val label = key.label) {
                            is KeyLabel.Text -> Text(
                                label.text,
                                style = MaterialTheme.typography.labelLarge
                            )

                            is KeyLabel.Icon -> Icon(
                                label.icon,
                                label.description,
                                modifier = Modifier.size(MaterialTheme.typography.labelLarge.lineHeight.toDp())
                            )
                        }
                    }
                }
                IconButton({}) {
                    Icon(Icons.Default.MoreVert, null)
                }
            }
        }
    }
}