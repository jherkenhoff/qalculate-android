package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp

@Composable
fun KeypadLayout(
    expansionRatio: Float,
    modifier: Modifier = Modifier,
    weightNumpad: Float = 1f,
    weightAuxiliary: Float = 0.6f,
    weightExtended: Float = 0.6f,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->

        val invExpansionRatio = 1 - expansionRatio

        val gapHeightPx = with(this) {8.dp.toPx()}

        val rowsNumpad = measurables.filter{ it.layoutId == "numpad" }
        val rowsAuxiliary = measurables.filter{ it.layoutId == "auxiliary" }
        val rowsExtended = measurables.filter{ it.layoutId == "extended" }

        val nRows = rowsNumpad.size + rowsAuxiliary.size + rowsExtended.size
        val nGaps = nRows - 1

        val heightAvailable = constraints.maxHeight - nGaps*gapHeightPx

        val heightUnit = heightAvailable / (rowsNumpad.size*weightNumpad
                + rowsAuxiliary.size*weightAuxiliary
                + rowsExtended.size*weightExtended)

        val totalHeightExtendedSection = heightUnit*weightExtended * rowsExtended.size + gapHeightPx * rowsExtended.size
        val scaleFactor = 1f + invExpansionRatio*(heightAvailable-totalHeightExtendedSection)/heightAvailable/2

        val yOffset = invExpansionRatio*totalHeightExtendedSection

        // Measure
        val numpadPlaceables = rowsNumpad.map {
            it.measure(Constraints(
                minWidth = constraints.maxWidth,
                maxWidth = constraints.maxWidth,
                minHeight = (heightUnit*weightNumpad*scaleFactor).roundToPx(),
                maxHeight = (heightUnit*weightNumpad*scaleFactor).roundToPx()
            ))
        }

        val auxiliaryPlaceables = rowsAuxiliary.map {
            it.measure(Constraints(
                minWidth = constraints.maxWidth,
                maxWidth = constraints.maxWidth,
                minHeight = (heightUnit*weightAuxiliary*scaleFactor).roundToPx(),
                maxHeight = (heightUnit*weightAuxiliary*scaleFactor).roundToPx()
            ))
        }

        val extendedPlaceables = rowsExtended.map {
            it.measure(Constraints(
                minWidth = constraints.maxWidth,
                maxWidth = constraints.maxWidth,
                minHeight = (heightUnit*weightExtended).roundToPx(),
                maxHeight = (heightUnit*weightExtended).roundToPx()
            ))
        }

        var y = 0f

        layout(constraints.maxWidth, constraints.maxHeight) {
            extendedPlaceables.forEach {
                it.placeRelative(0, y.roundToPx() - yOffset.roundToPx())
                y += it.height + gapHeightPx
            }

            y -= yOffset.roundToPx()

            auxiliaryPlaceables.forEach {
                it.placeRelative(0, y.roundToPx())
                y += it.height+gapHeightPx
            }

            numpadPlaceables.forEach {
                it.placeRelative(0, y.roundToPx())
                y += it.height + gapHeightPx
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {

    val expansionRatio = animateFloatAsState(0f, label="Extension")

    KeypadLayout(
        expansionRatio = expansionRatio.value
    ) {
        Box(modifier = Modifier.background(Color.Green).layoutId("extended"))
        Box(modifier = Modifier.background(Color.Green).layoutId("extended"))
        Box(modifier = Modifier.background(Color.Blue).layoutId("auxiliary"))
        Box(modifier = Modifier.background(Color.Red).layoutId("numpad"))
        Box(modifier = Modifier.background(Color.Red).layoutId("numpad"))
        Box(modifier = Modifier.background(Color.Red).layoutId("numpad"))
    }
}