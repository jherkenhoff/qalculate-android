package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorTopBar(
    userPreferences: UserPreferences,
    onUserPreferencesChanged: (UserPreferences) -> Unit,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    Surface(
        modifier,
        color = MaterialTheme.colorScheme.surfaceContainer,
        shadowElevation = 6.dp
    ) {
        Column {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.safeContent))
            Row {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Open navigation menu"
                    )
                }
                Spacer(Modifier.weight(1f))

                Spacer(Modifier.weight(1f))
                IconButton(onClick = { onSettingsClick() }) { Icon(Icons.Default.Settings, contentDescription = "Open settings") }
            }
            Row(Modifier.padding(horizontal = 8.dp)) {
                Spacer(Modifier.weight(1f))
                CalculatorChips(
                    userPreferences = userPreferences,
                    onUserPreferencesChanged = onUserPreferencesChanged
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    CalculatorTopBar(
        UserPreferences(),
        onUserPreferencesChanged = {}
    )
}