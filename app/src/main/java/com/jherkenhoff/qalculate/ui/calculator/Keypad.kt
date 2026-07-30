package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.CalculatorAction
import com.jherkenhoff.qalculate.model.KeypadSection
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.common.CalcActionLabelMapper

@Composable
fun Keypad(
    keypadSections: List<KeypadSection>,
    calcActionLabelMapper: CalcActionLabelMapper,
    modifier: Modifier = Modifier,
    onKeyAction: (CalculatorAction) -> Unit = {},
) {


    Column(
        modifier.padding(horizontal = 3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        keypadSections.forEach { keypad ->
            val items = keypad.keys.map {
                GridItem(
                    it.first.row,
                    it.first.column,
                    it.first.rowSpan,
                    it.first.columnSpan,
                    key = it.hashCode()
                ) {
                    CalculatorKeyButton(
                        it.second,
                        calcActionLabelMapper,
                        onKeyAction = onKeyAction
                    )
                }
            }

            GridLayout(
                keypad.rows,
                keypad.cols,
                items,
                horizontalSpacing = 3.dp,
                verticalSpacing = 3.dp,
                aspectRatio = keypad.aspectRatio,
                modifier = Modifier.fillMaxWidth()
            )
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