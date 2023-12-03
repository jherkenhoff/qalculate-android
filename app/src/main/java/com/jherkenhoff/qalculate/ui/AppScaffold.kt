package com.jherkenhoff.qalculate.ui

import android.graphics.Insets
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.ui.theme.TopSheetShape

@Composable
fun AppScaffold(
    topPanel: @Composable (() -> Unit) = {},
    inputSection: @Composable ((PaddingValues) -> Unit) = {}
) {

    // val topInset = WindowInsets.systemBars.getTop()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        SubcomposeLayout { constraints ->
            val layoutWidth = constraints.maxWidth
            val layoutHeight = constraints.maxHeight

            val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

            layout(layoutWidth, layoutHeight) {
                val topPanelPlaceables =
                    subcompose(AppScaffoldLayoutContent.TopPanel, topPanel).map {
                        it.measure(looseConstraints)
                    }
                val topPanelHeight =
                    topPanelPlaceables.maxByOrNull { it.height }?.height ?: 0

                val dragHandlePlaceables = subcompose(AppScaffoldLayoutContent.DragHandle) {
                    DragHandle()
                }.map {
                    it.measure(looseConstraints)
                }
                val dragHandleHeight = dragHandlePlaceables.maxByOrNull { it.height }?.height ?: 0


                val topSheetPlaceables = subcompose(AppScaffoldLayoutContent.TopSheet) {
                    TopSheet(
                        Modifier.height(topPanelHeight.toDp() + dragHandleHeight.toDp())
                    )
                }.map {
                    it.measure(looseConstraints)
                }

                val inputSectionPlaceables = subcompose(AppScaffoldLayoutContent.InputSection) {
                    val inputSectionPadding = PaddingValues(
                        top = topPanelHeight.toDp() + dragHandleHeight.toDp()
                    )
                    inputSection(inputSectionPadding)
                }.map {
                    it.measure(looseConstraints)
                }

                topSheetPlaceables.forEach {
                    it.place(0, 0)
                }
                topPanelPlaceables.forEach {
                    it.place(0, 0)
                }
                dragHandlePlaceables.forEach {
                    it.place(0, topPanelHeight)
                }
                inputSectionPlaceables.forEach {
                    it.place(0, 0)
                }
            }
        }
    }
}

@Composable
fun TopSheet(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = TopSheetShape,
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
    }
}

@Composable
fun DragHandle() {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.KeyboardArrowDown,
            contentDescription = stringResource(R.string.top_sheet_arrow_down)
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun DefaultPreview() {
    TopSheet(Modifier.height(100.dp))
}

private enum class AppScaffoldLayoutContent {DragHandle, TopPanel, TopSheet, InputSection}
