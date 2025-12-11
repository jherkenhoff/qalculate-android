package com.jherkenhoff.qalculate.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedListItem(
    headlineContent: @Composable () -> Unit,
    top: Boolean = false,
    bottom: Boolean = false,
    modifier: Modifier = Modifier,
    overlineContent: (@Composable () -> Unit)? = null,
    supportingContent: (@Composable () -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    colors: ListItemColors = ListItemDefaults.colors(),
    tonalElevation: Dp = ListItemDefaults.Elevation,
    shadowElevation: Dp = ListItemDefaults.Elevation
) {
    val topRadius = if (top) 16.dp else 6.dp
    val bottomRadius = if (bottom) 16.dp else 6.dp
    ListItem(
        headlineContent,
        modifier.clip(RoundedCornerShape(topRadius, topRadius, bottomRadius, bottomRadius)),
        overlineContent,
        supportingContent,
        leadingContent,
        trailingContent,
        colors,
        tonalElevation,
        shadowElevation
    )
}

@Preview
@Composable
fun Default() {
    val N = 5
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 0..N) {
            SegmentedListItem(
                {
                    Text("Headline")
                },
                i == 0,
                i == N,
                overlineContent = { Text("Overline") }
            )
        }
    }
}