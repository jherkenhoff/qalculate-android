package com.jherkenhoff.qalculate.ui.calculator

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlin.math.exp

private enum class SlotsEnum {
    TAB,
    TRAILING,
    BACKGROUND,
    CONTENT
}

@Composable
fun TabPanel(
    tabItems: List<@Composable () -> Unit>,
    activeTabItemIndex: Int,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onTabClicked: (Int) -> Unit = {},
    color: Color = MaterialTheme.colorScheme.surface,
    panelContent: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val f by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label="Tab expansion animation"
    )

    SubcomposeLayout(modifier.pointerInput(Unit) {
    }) { constraints ->

        val tabItemMeasurable = subcompose(SlotsEnum.TAB) {
            SharedTransitionLayout() {
                AnimatedContent(expanded) {
                    if (it) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                        ) {
                            itemsIndexed(tabItems) { i, item ->
                                TextButton(
                                    onClick = { onTabClicked(i); expanded = false },
                                    modifier = Modifier.sharedElement(
                                        rememberSharedContentState(i),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                                ) {
                                    item()
                                }
                            }
                        }
                    } else {
                        TextButton(
                            onClick = { expanded = true },
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .sharedElement(
                                rememberSharedContentState(activeTabItemIndex),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                        ) {
                            tabItems[activeTabItemIndex]()
                        }
                    }
                }
            }
        }
        val tabPlaceable = tabItemMeasurable.map{ it.measure(constraints) }.first()
        val panelContentPlaceable = subcompose(SlotsEnum.CONTENT, panelContent).map{ it.measure(constraints) }.first()

        val tabHeight = tabPlaceable.height
        val totalHeight = tabHeight + panelContentPlaceable.height

        val tabRadius = tabHeight.toFloat()/2

        val tabWidth = (1-f)*tabPlaceable.width + f*(constraints.maxWidth + tabRadius)

        val backgroundPlaceable = subcompose(SlotsEnum.BACKGROUND) {
            Surface(
                shape = TabPanelShape(
                    tabWidth = tabWidth,
                    tabHeight = tabHeight.toFloat(),
                    tabRadius = tabRadius
                ),
                color = color
            ) { }
        }.map { it.measure(Constraints.fixed(constraints.maxWidth, totalHeight)) }.first()

        layout(constraints.maxWidth, totalHeight) {
            backgroundPlaceable.place(0, 0)

            val tabPlaceableX = f*(constraints.maxWidth-tabPlaceable.width)/2
            tabPlaceable.place(tabPlaceableX.toInt(), 0)
            panelContentPlaceable.place(0, tabHeight)
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    var activeKeypad by remember { mutableIntStateOf(0) }

    TabPanel(
        tabItems = listOf(
            { Text("Tab 1") },
            { Text("Tab 2") },
            { Text("Tab 3") },
        ),
        activeKeypad,
        trailingContent = { Text("Trailing content") },
        onTabClicked = { activeKeypad = it }
    ) {
        Text("Main content")
    }
}