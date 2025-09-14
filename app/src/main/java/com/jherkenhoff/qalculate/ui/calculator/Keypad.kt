package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.Key
import com.jherkenhoff.qalculate.model.KeyAction


@Composable
fun Keypad(
    keys: Array<Array<Key>>,
    modifier: Modifier = Modifier,
    topKeyCornerSize: CornerSize = KeyDefaults.Shape.topStart,
    onKeyAction: (KeyAction) -> Unit = {},
    compact: Boolean = true
) {
    val gap = 3.dp

    val height = animateDpAsState(if (compact) 38.dp else 42.dp)

    Column(
        modifier = modifier.padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {
        for ((i, row) in keys.withIndex()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(gap),
                modifier = Modifier.height(height.value)
            ) {

                for ((j, key) in row.withIndex()) {

                    var shape = KeyDefaults.Shape
                    if (i == 0) {
                        if ((j == 0)) {
                            shape = KeyDefaults.Shape.copy(topStart = topKeyCornerSize)
                        } else if (j == row.lastIndex) {
                            shape = KeyDefaults.Shape.copy(topEnd = topKeyCornerSize)
                        }
                    }

                    Key(
                        key,
                        onKeyAction = onKeyAction,
                        shape = shape,
                        modifier = Modifier.weight(key.width.toFloat()).fillMaxSize()
                    )
                }
            }
        }
    }
}