package com.jherkenhoff.qalculate.ui.calculator

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ChipColors
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    var numberFractionFormatDialogOpen by remember { mutableStateOf(false) }

    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Chip(
            text =when (userPreferences.angleUnit) {
                UserPreferences.AngleUnit.DEGREES -> "DEG"
                UserPreferences.AngleUnit.RADIANS -> "RAD"
                UserPreferences.AngleUnit.GRADIANS -> "GRA"
            },
            onClick = { angleUnitDialogOpen = true }
        )
        Chip(
            text =
                when (userPreferences.numericalDisplayMode) {
                    UserPreferences.NumericalDisplayMode.NORMAL -> "NORM"
                    UserPreferences.NumericalDisplayMode.SCIENTIFIC -> "SCI"
                    UserPreferences.NumericalDisplayMode.ENGINEERING -> "ENG"
                },
            onClick = { numericalDisplayModeDialogOpen = true }
        )
        Chip(
            text =
                when (userPreferences.numberFractionFormat) {
                    UserPreferences.NumberFractionFormat.FRACTION_DECIMAL -> "0.00"
                    UserPreferences.NumberFractionFormat.FRACTION_DECIMAL_EXACT -> "0.00"
                    UserPreferences.NumberFractionFormat.FRACTION_FRACTIONAL -> "1/2"
                    UserPreferences.NumberFractionFormat.FRACTION_COMBINED -> "1+1/2"
                    UserPreferences.NumberFractionFormat.FRACTION_PERCENT -> "%"
                    UserPreferences.NumberFractionFormat.FRACTION_PERMILLE -> "‰"
                    UserPreferences.NumberFractionFormat.FRACTION_PERMYRIAD -> "‱"
                },
            onClick = { numericalDisplayModeDialogOpen = true }
        )
        Chip(
            text = when (userPreferences.approximationMode) {
                UserPreferences.ApproximationMode.EXACT -> "EXACT"
                UserPreferences.ApproximationMode.TRY_EXACT -> "EXACT"
                UserPreferences.ApproximationMode.APPROXIMATE -> "APPROX"
            },
            onClick = { approximationModeDialogOpen = true },
            highlight = true
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

    if (numberFractionFormatDialogOpen) {
        SingleEnumSelectDialog<UserPreferences.NumberFractionFormat>(
            "Number fraction format",
            enumLabelMap = { when (it) {
                UserPreferences.NumberFractionFormat.FRACTION_DECIMAL -> "Decimal"
                UserPreferences.NumberFractionFormat.FRACTION_DECIMAL_EXACT -> "Decimal exact"
                UserPreferences.NumberFractionFormat.FRACTION_FRACTIONAL -> "Fractional"
                UserPreferences.NumberFractionFormat.FRACTION_COMBINED -> "Combined"
                UserPreferences.NumberFractionFormat.FRACTION_PERCENT -> "Percent"
                UserPreferences.NumberFractionFormat.FRACTION_PERMILLE -> "Permille"
                UserPreferences.NumberFractionFormat.FRACTION_PERMYRIAD -> "Permyriad"
            }},
            currentSelection = userPreferences.numberFractionFormat,
            onSelect = { onUserPreferencesChanged(userPreferences.copy(numberFractionFormat = it)) },
            onDismissRequest = { numberFractionFormatDialogOpen = false }
        )
    }
}

@Composable
fun Chip(
    text: String,
    onClick: () -> Unit = {},
    highlight: Boolean = false
) {
    Surface(
        color = if (highlight) MaterialTheme.colorScheme.tertiary else Color.Transparent,
        border = if (highlight) null else BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(28.dp),
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Text(text, style = MaterialTheme.typography.labelSmall)
        }
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