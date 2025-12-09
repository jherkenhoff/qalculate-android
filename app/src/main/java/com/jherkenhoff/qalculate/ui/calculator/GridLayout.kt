package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


class GridScope {

    internal data class PositionedItem(
        val row: Int,
        val col: Int,
        val rowSpan: Int,
        val colSpan: Int,
        val content: @Composable () -> Unit
    )

    internal val items = mutableListOf<PositionedItem>()

    fun item(
        row: Int,
        col: Int,
        content: @Composable () -> Unit
    ) {
        items += PositionedItem(row, col, 1, 1, content)
    }

    fun item(
        row: Int,
        col: Int,
        rowSpan: Int,
        colSpan: Int,
        content: @Composable () -> Unit
    ) {
        items += PositionedItem(row, col, rowSpan, colSpan, content)
    }
}

@Composable
fun GridLayout(
    rows: Int,
    cols: Int,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 8.dp,
    verticalSpacing: Dp = 8.dp,
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
        val totalHeight = constraints.maxHeight

        val cellWidth = (totalWidth - hSpace * (cols - 1)) / cols

        val cellHeight = (totalHeight - vSpace * (rows - 1)) / rows

        val placeables = items.indices.map { i ->
            val width = cellWidth * items[i].colSpan + hSpace * (items[i].colSpan - 1)
            val height = cellHeight * items[i].rowSpan + vSpace * (items[i].rowSpan - 1)

            measurables[i].measure(
                Constraints.fixed(width, height)
            )
        }

        layout(totalWidth, totalHeight) {
            items.indices.map { i ->
                val x = items[i].col * (cellWidth + hSpace)
                val y = items[i].row * (cellHeight + vSpace)
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
        modifier = Modifier.size(100.dp)
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