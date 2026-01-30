package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.KeyPositionSpec
import kotlin.math.roundToInt

class GridScope {
    internal data class PositionedItem(
        val positionSpec: KeyPositionSpec,
        val content: @Composable () -> Unit
    )

    internal val items = mutableListOf<PositionedItem>()

    fun item(
        positionSpec: KeyPositionSpec,
        content: @Composable () -> Unit
    ) {
        items += PositionedItem(positionSpec, content)
    }

    fun item(
        row: Int,
        col: Int,
        rowSpan: Int = 1,
        colSpan: Int = 1,
        content: @Composable () -> Unit
    ) {
        item(KeyPositionSpec(row, col, rowSpan, colSpan), content)
    }
}

@Composable
fun GridLayout(
    rows: Int,
    cols: Int,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp,
    content: GridScope.() -> Unit
) {
    val scope = GridScope().apply(content)
    val items = scope.items

    Layout(
        modifier = modifier,
        content = { items.forEach { it.content() } }
    ) { measurables, constraints ->

        val hSpace = horizontalSpacing.roundToPx()
        val vSpace = verticalSpacing.roundToPx()

        val totalWidth = constraints.maxWidth

        val cellWidth = (totalWidth - hSpace * (cols - 1)) / cols


        //val cellHeight = (constraints.maxHeight - vSpace * (rows - 1)) / rows
        val cellHeight = (cellWidth * aspectRatio).roundToInt()

        val placeables = items.indices.map { i ->
            val width = cellWidth * items[i].positionSpec.colSpan + hSpace * (items[i].positionSpec.colSpan - 1)
            val height = cellHeight * items[i].positionSpec.rowSpan + vSpace * (items[i].positionSpec.rowSpan - 1)

            measurables[i].measure(
                Constraints.fixed(width, height)
            )
        }

        val totalHeight = cellHeight * rows + vSpace * (rows - 1)

        layout(totalWidth, totalHeight) {
            items.indices.map { i ->
                val x = items[i].positionSpec.col * (cellWidth + hSpace)
                val y = items[i].positionSpec.row * (cellHeight + vSpace)
                placeables[i].placeRelative(x, y)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Default() {
    GridLayout(
        3,
        3,
        aspectRatio = 0.5f,
        horizontalSpacing = 8.dp,
        verticalSpacing = 8.dp,
    ) {
        item(0, 0) {
            Box(Modifier.background(Color.Gray))
        }
        item(0, 1) {
            Box(Modifier.background(Color.Gray))
        }
        item(0, 2) {
            Box(Modifier.background(Color.Gray))
        }
        item(1, 0, 2, 1) {
            Box(Modifier.background(Color.Gray))
        }
        item(1, 1, 1, 1) {
            Box(Modifier.background(Color.Gray))
        }
        item(2, 1, 1, 2) {
            Box(Modifier.background(Color.Gray))
        }
    }
}