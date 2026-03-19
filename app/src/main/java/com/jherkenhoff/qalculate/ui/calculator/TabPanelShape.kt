package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class TabPanelShape(
    private val tabWidth: Float,
    private val tabHeight: Float,
    private val tabRadius: Float
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val path = Path().apply {

            moveTo(0f, tabHeight)
            lineTo(0f, 0f)
            lineTo(tabWidth-tabRadius, 0f)
            arcTo(
                rect = Rect(
                    center = Offset(tabWidth-tabRadius, tabRadius),
                    radius = tabRadius
                ),
                -90f,
                90f,
                false
            )
            lineTo(tabWidth, tabHeight-tabRadius)
            arcTo(
                rect = Rect(
                    center = Offset(tabWidth+tabRadius, tabHeight-tabRadius),
                    radius = tabRadius
                ),
                180f,
                -90f,
                false
            )
            lineTo(size.width, tabHeight)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)

            close()
        }

        return Outline.Generic(path)
    }
}



@Preview
@Composable
private fun DefaultPreview() {
    Surface(
        shape = TabPanelShape(100f, 100f,50f),
        modifier = Modifier.size(400.dp, 400.dp)
    ) {}
}