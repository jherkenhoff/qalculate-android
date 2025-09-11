package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.Key
import com.jherkenhoff.qalculate.model.KeyAction
import com.jherkenhoff.qalculate.model.Keys

val primaryKeypadKeys = arrayOf(
    arrayOf(Keys.keyEuler, Keys.keyPi, Keys.key7, Keys.key8, Keys.key9, Keys.keyBackspace, Keys.keyClearAll),
    arrayOf(Keys.keySqrt, Keys.keyPower, Keys.key4, Keys.key5, Keys.key6, Keys.keyMultiply, Keys.keyDivide),
    arrayOf(Keys.keyBracketOpen, Keys.keyBracketClose, Keys.key1, Keys.key2, Keys.key3, Keys.keyPlus, Keys.keyMinus),
    arrayOf(Keys.keyUnderscore, Keys.keyEqual, Keys.key0, Keys.keyDecimal, Keys.keyAns, Keys.keyReturn),
)


val secondaryKeypadKeys = arrayOf(
    arrayOf(Keys.keySin, Keys.keySin, Keys.keySin, Keys.keySin, Keys.keySin),
)

@Composable
fun Keypad(
    keys: Array<Array<Key>>,
    modifier: Modifier = Modifier,
    onKeyAction: (KeyAction) -> Unit = {},
    compact: Boolean = true
) {
    val gap = 3.dp

    val height = animateDpAsState(if (compact) 38.dp else 42.dp)

    Column(
        modifier = modifier.padding(gap),
        verticalArrangement = Arrangement.spacedBy(gap)
    ) {

        for (row in keys) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(gap),
                modifier = Modifier.height(height.value)
            ) {
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
    Keypad(
        primaryKeypadKeys
    )
}