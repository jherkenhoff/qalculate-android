package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.model.KeypadDefinition
import com.jherkenhoff.qalculate.model.KeypadSection
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.common.CalcActionLabelMapper

@Composable
fun Keypad(
    keypadSections: List<KeypadSection>,
    calcActionLabelMapper: CalcActionLabelMapper,
    modifier: Modifier = Modifier,
    onKeyAction: (CalculatorAction) -> Unit = {},
    onActiveKeypadChanged: (Int) -> Unit = {},
) {

    Column() {
        Spacer(Modifier.height(6.dp))

        Column(
            Modifier
                .padding(horizontal = 3.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            keypadSections.forEach { keypad ->
                GridLayout(
                    keypad.rows,
                    keypad.cols,
                    horizontalSpacing = 3.dp,
                    verticalSpacing = 3.dp,
                    aspectRatio = keypad.aspectRatio,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    keypad.keys.forEach {
                        item(it.first.row, it.first.col, it.first.rowSpan, it.first.colSpan) {
                            CalculatorKeyButton(
                                it.second,
                                calcActionLabelMapper,
                                onKeyAction = onKeyAction
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    Keypad(
        emptyList(),
        CalcActionLabelMapper(UserPreferences.Default)
    )
}