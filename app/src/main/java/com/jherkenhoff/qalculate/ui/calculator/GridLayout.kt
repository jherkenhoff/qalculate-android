package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

data class GridItem(
    val row: Int,
    val column: Int,
    val rowSpan: Int = 1,
    val columnSpan: Int = 1,
    val key: Any? = null,
    val content: @Composable () -> Unit,
)

@Composable
fun GridLayout(
    rows: Int,
    cols: Int,
    items: List<GridItem>,
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f,
    horizontalSpacing: Dp = 0.dp,
    verticalSpacing: Dp = 0.dp
) {
    Layout(
        modifier = modifier,
        content = { items.forEach {
            key(it.key) {
                it.content()
            }
        }}
    ) { measurables, constraints ->

        val hSpace = horizontalSpacing.roundToPx()
        val vSpace = verticalSpacing.roundToPx()

        val totalWidth = constraints.maxWidth

        val cellWidth = (totalWidth - hSpace * (cols - 1)) / cols


        //val cellHeight = (constraints.maxHeight - vSpace * (rows - 1)) / rows
        val cellHeight = (cellWidth * aspectRatio).roundToInt()

        val placeables = items.indices.map { i ->
            val width = cellWidth * items[i].columnSpan + hSpace * (items[i].columnSpan - 1)
            val height = cellHeight * items[i].rowSpan + vSpace * (items[i].rowSpan - 1)

            measurables[i].measure(
                Constraints.fixed(width, height)
            )
        }

        val totalHeight = cellHeight * rows + vSpace * (rows - 1)

        layout(totalWidth, totalHeight) {
            items.indices.forEach { i ->
                val x = items[i].column * (cellWidth + hSpace)
                val y = items[i].row * (cellHeight + vSpace)
                placeables[i].placeRelative(x, y)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Default() {
    val items = listOf(
        GridItem(0, 0) { Box(Modifier.background(Color.Gray)) },
        GridItem(0, 1) { Box(Modifier.background(Color.Gray)) },
        GridItem(0, 2) { Box(Modifier.background(Color.Gray)) },
        GridItem(1, 0, 2, 1) { Box(Modifier.background(Color.Gray)) },
        GridItem(1, 1) { Box(Modifier.background(Color.Gray)) },
        GridItem(2, 1, 1, 2) { Box(Modifier.background(Color.Gray)) }
    )

    GridLayout(
        3,
        3,
        items,
        aspectRatio = 0.5f,
        horizontalSpacing = 8.dp,
        verticalSpacing = 8.dp,
    )
}