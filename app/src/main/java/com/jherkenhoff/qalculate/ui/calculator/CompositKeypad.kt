package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.Key
import com.jherkenhoff.qalculate.model.KeyAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompositKeypad(
    secondaryKeypadVisible: Boolean = true,
    secondaryKeypads: Array<SecondaryKeypadData>,
    primaryKeypadKeys: Array<Array<Key>>,
    activeSecondaryKeypad: Int,
    onActiveSecondaryKeypadChanged: (Int) -> Unit = {},
    onKeyAction: (KeyAction) -> Unit = {}
) {
    Column(
        Modifier.wrapContentHeight()
    ) {
    }
}