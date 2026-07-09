package com.jherkenhoff.qalculate.ui.calculator

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private enum class SlotsEnum {
    ACTIVE_TAB, TAB, TRAILING, SHEET, TOP, SHEET_CONTENT
}

@Composable
private fun TabButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    showText: Boolean = true,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClicked: () -> Unit = {},
) {
    TextButton(
        onClick = onClicked,
        colors = ButtonDefaults.textButtonColors().copy(contentColor = color),
        modifier = modifier.height(48.dp).padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null)
            AnimatedVisibility(showText) {
                Text(text)
            }
        }
    }
}

@Composable
fun TabPanel(
    tabItems: List<Pair<ImageVector, String>>,
    activeTabItemIndex: Int,
    trailingContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    topContent: @Composable (padding: Dp) -> Unit,
    collapse: Boolean = false,
    onTabClicked: (Int) -> Unit = {},
    color: Color = MaterialTheme.colorScheme.surface,
    panelContent: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    val f by animateFloatAsState(
        targetValue = if (expanded) 1f else 0f,
        label="Tab expansion animation"
    )

    SubcomposeLayout(
        modifier.pointerInput(Unit) {}
    ) { constraints ->

        val activeTabItemPlaceable = subcompose(SlotsEnum.ACTIVE_TAB) {
            TabButton(
                icon = tabItems[activeTabItemIndex].first,
                text = tabItems[activeTabItemIndex].second,
                showText = !collapse,
                color = MaterialTheme.colorScheme.primary
            )
        }.map { it.measure(constraints) }.first()

        val tabItemMeasurable = subcompose(SlotsEnum.TAB) {
            AnimatedContent(
                expanded
            ) {
                if (it) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                    ) {
                        itemsIndexed(tabItems) { i, item ->
                            TabButton(
                                icon = item.first,
                                text = item.second,
                                onClicked = { onTabClicked(i); expanded = false },
                                color = if (i == activeTabItemIndex) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                } else {
                    TabButton(
                        icon = tabItems[activeTabItemIndex].first,
                        text = tabItems[activeTabItemIndex].second,
                        showText = !collapse,
                        onClicked = { expanded = true },
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        val tabPlaceable = tabItemMeasurable.map{ it.measure(constraints) }.first()
        val tabHeight = tabPlaceable.height

        val sheetContentPlaceable = subcompose(SlotsEnum.SHEET_CONTENT, panelContent).map{
            it.measure(constraints.copy(maxHeight = constraints.maxHeight-tabHeight))
        }.first()

        val tabRadius = tabHeight.toFloat()/2
        val tabWidth = (1-f)*tabPlaceable.width + f*(constraints.maxWidth + tabRadius)

        val trailingContentPlaceable = subcompose(SlotsEnum.TRAILING, trailingContent).map {
            it.measure(Constraints(maxWidth = constraints.maxWidth - activeTabItemPlaceable.width, maxHeight = tabHeight))
        }.first()

        val sheetHeight = tabHeight + sheetContentPlaceable.height
        val sheetPlaceable = subcompose(SlotsEnum.SHEET) {
            Surface(
                shape = TabPanelShape(
                    tabWidth = tabWidth,
                    tabHeight = tabHeight.toFloat(),
                    tabRadius = tabRadius
                ),
                color = color,
                shadowElevation = 6.dp
            ) { }
        }.map { it.measure(Constraints.fixed(constraints.maxWidth, sheetHeight)) }.first()

        val topPlaceable = subcompose(SlotsEnum.TOP) {
            topContent(tabHeight.toDp())
        }.map {
            it.measure(Constraints(constraints.maxWidth, constraints.maxWidth, 0, constraints.maxHeight-sheetContentPlaceable.height))
        }.first()

        val totalHeight = if (tabHeight > topPlaceable.height) sheetHeight else sheetHeight + topPlaceable.height - tabHeight

        layout(constraints.maxWidth, totalHeight) {
            topPlaceable.place(
                x = 0,
                y = if (topPlaceable.height > tabHeight) 0 else tabHeight - topPlaceable.height
            )

            val sheetOffset = if (tabHeight > topPlaceable.height) 0 else topPlaceable.height - tabHeight

            trailingContentPlaceable.place(
                x = constraints.maxWidth - trailingContentPlaceable.width,
                y = tabPlaceable.height/2 - trailingContentPlaceable.height/2 + sheetOffset
            )

            sheetPlaceable.place(0, sheetOffset)

            val tabPlaceableX = f*(constraints.maxWidth-tabPlaceable.width)/2
            tabPlaceable.place(tabPlaceableX.toInt(), sheetOffset)

            sheetContentPlaceable.place(0, tabHeight+sheetOffset)
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    var activeKeypad by remember { mutableIntStateOf(0) }

    TabPanel(
        tabItems = listOf(
            Pair(Icons.Default.Calculate, "Tab 1"),
            Pair(Icons.Default.Key, "Tab 2"),
            Pair(Icons.Default.AccessTime, "Tab 3"),
        ),
        topContent = { Surface(color = Color.Red, modifier = Modifier.fillMaxWidth().height(100.dp)) {} },
        activeTabItemIndex = activeKeypad,
        collapse = false,
        trailingContent = {
            Text(
                "Trailing content",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize().background(Color.Green)
            )
                          },
        onTabClicked = { activeKeypad = it }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        ) {
            Text("Main content")
        }
    }
}