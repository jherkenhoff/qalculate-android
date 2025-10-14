package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
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

@Composable
fun CalculatorChips(
    userPreferences: UserPreferences,
    modifier: Modifier = Modifier,
    onUserPreferencesChanged: (UserPreferences) -> Unit = {},
) {

    var angleUnitDialogOpen by remember { mutableStateOf(false) }
    var approximationModeDialogOpen by remember { mutableStateOf(false) }
    var numericalDisplayModeDialogOpen by remember { mutableStateOf(false) }

    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AssistChip(
            onClick = { angleUnitDialogOpen = true },
            label = { Text(
                when (userPreferences.angleUnit) {
                    UserPreferences.AngleUnit.DEGREES -> "DEG"
                    UserPreferences.AngleUnit.RADIANS -> "RAD"
                    UserPreferences.AngleUnit.GRADIANS -> "GRA"
                }

            ) }
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

            ) }
        )
        AssistChip(
            onClick = { numericalDisplayModeDialogOpen = true },
            label = { Text(
                when (userPreferences.numericalDisplayMode) {
                    UserPreferences.NumericalDisplayMode.NORMAL -> "NORM"
                    UserPreferences.NumericalDisplayMode.SCIENTIFIC -> "SCI"
                    UserPreferences.NumericalDisplayMode.ENGINEERING -> "ENG"
                }

            ) }
        )
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

@Composable
@Preview(showBackground = true)
private fun DefaultPreview() {
    CalculatorChips(
        userPreferences = UserPreferences(),
        onUserPreferencesChanged = {}
    )
}