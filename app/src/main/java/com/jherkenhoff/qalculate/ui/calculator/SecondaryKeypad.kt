package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.KeyAction
import com.jherkenhoff.qalculate.model.Keys


private val keypad = arrayOf(
    arrayOf(Keys.keySin, Keys.keySin, Keys.keySin, Keys.keySin, Keys.keySin),
)

@Composable
fun SecondaryKeypad(
    modifier: Modifier = Modifier,
    onKeyAction: (KeyAction) -> Unit = {},
) {
    val gap = 3.dp
    Column(
        modifier = modifier.padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {

        for (row in keypad) {
            Row(horizontalArrangement = Arrangement.spacedBy(gap)) {
                for (key in row) {
                    Key(
                        key,
                        onClick = { key.clickAction?.let { onKeyAction(it) } },
                        onLongClick = { key.longClickAction?.let { onKeyAction(it) } },
                        modifier = Modifier.weight(key.width.toFloat())
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    SecondaryKeypad()
}