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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.common.SingleEnumSelectDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorTopBar(
    userPreferences: UserPreferences,
    onUserPreferencesChanged: (UserPreferences) -> Unit,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {

    var angleUnitDialogOpen by remember { mutableStateOf(false) }
    var approximationModeDialogOpen by remember { mutableStateOf(false) }
    var numericalDisplayModeDialogOpen by remember { mutableStateOf(false) }

    Surface(
        modifier,
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column() {
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
                AssistChip(
                    onClick = { angleUnitDialogOpen = true },
                    label = { Text(
                        when (userPreferences.angleUnit) {
                            UserPreferences.AngleUnit.DEGREES -> "DEG"
                            UserPreferences.AngleUnit.RADIANS -> "RAD"
                            UserPreferences.AngleUnit.GRADIANS -> "GRA"
                        }

                    ) },
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                FilterChip(
                    selected = userPreferences.approximationMode == UserPreferences.ApproximationMode.EXACT,
                    onClick = { approximationModeDialogOpen = true },
                    label = { Text(
                        when (userPreferences.approximationMode) {
                            UserPreferences.ApproximationMode.EXACT -> "EXACT"
                            UserPreferences.ApproximationMode.TRY_EXACT -> "EXACT"
                            UserPreferences.ApproximationMode.APPROXIMATE -> "APPROX"
                        }

                    ) },
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
                AssistChip(
                    onClick = { numericalDisplayModeDialogOpen = true },
                    label = { Text(
                        when (userPreferences.numericalDisplayMode) {
                            UserPreferences.NumericalDisplayMode.NORMAL -> "NORM"
                            UserPreferences.NumericalDisplayMode.SCIENTIFIC -> "SCI"
                            UserPreferences.NumericalDisplayMode.ENGINEERING -> "ENG"
                        }

                    ) },
                    modifier = Modifier.padding(horizontal = 3.dp)
                )
            }
        }
    }

    if (angleUnitDialogOpen) {
        SingleEnumSelectDialog<UserPreferences.AngleUnit>(
            "Angle unit",
            enumLabelMap = { when (it) {
                UserPreferences.AngleUnit.DEGREES -> "Degrees"
                UserPreferences.AngleUnit.RADIANS -> "Radians"
                UserPreferences.AngleUnit.GRADIANS -> "Gradians"
            }},
            currentSelection = userPreferences.angleUnit,
            onSelect = { onUserPreferencesChanged(userPreferences.copy(angleUnit = it)) },
            onDismissRequest = { angleUnitDialogOpen = false }
        )
    }

    if (approximationModeDialogOpen) {
        SingleEnumSelectDialog<UserPreferences.ApproximationMode>(
            "Approximation mode",
            enumLabelMap = { when (it) {
                UserPreferences.ApproximationMode.EXACT -> "Always exact"
                UserPreferences.ApproximationMode.TRY_EXACT -> "Try exact"
                UserPreferences.ApproximationMode.APPROXIMATE -> "Approximate"
            }},
            currentSelection = userPreferences.approximationMode,
            onSelect = { onUserPreferencesChanged(userPreferences.copy(approximationMode = it)) },
            onDismissRequest = { approximationModeDialogOpen = false }
        )
    }

    if (numericalDisplayModeDialogOpen) {
        SingleEnumSelectDialog<UserPreferences.NumericalDisplayMode>(
            "Numerical display",
            enumLabelMap = { when (it) {
                UserPreferences.NumericalDisplayMode.NORMAL -> "Normal"
                UserPreferences.NumericalDisplayMode.SCIENTIFIC -> "Scientific"
                UserPreferences.NumericalDisplayMode.ENGINEERING -> "Engineering"
            }},
            currentSelection = userPreferences.numericalDisplayMode,
            onSelect = { onUserPreferencesChanged(userPreferences.copy(numericalDisplayMode = it)) },
            onDismissRequest = { numericalDisplayModeDialogOpen = false }
        )
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