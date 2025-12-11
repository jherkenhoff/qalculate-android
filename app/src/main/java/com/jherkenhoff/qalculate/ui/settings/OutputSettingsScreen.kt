package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jherkenhoff.qalculate.model.UserPreferences

@Composable
fun OutputSettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit = {}
) {

    OutputSettingsScreenContent(
        userPreferences = viewModel.userPreferences.collectAsStateWithLifecycle().value,
        onUserPreferenceChange = viewModel::updateUserPreferences,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutputSettingsScreenContent(
    userPreferences: UserPreferences,
    onUserPreferenceChange: (UserPreferences) -> Unit = {},
    onNavigateUp: () -> Unit = {  }
) {
    SubSettingScaffold(
        "Output settings",
        onNavigateUp = onNavigateUp
    ) {
        ListItem(
            headlineContent = { Text("Negative exponents") },
            supportingContent = { Text("Print A${userPreferences.getMultiplicationSignString()}B⁻¹ instead of A${userPreferences.getDivisionSignString()}B")},
            trailingContent = {
                Switch(
                    checked = userPreferences.negativeExponents,
                    onCheckedChange = { onUserPreferenceChange(userPreferences.copy(negativeExponents = it)) }
                )
            }
        )
        ListItem(
            headlineContent = { Text("Abbreviate names") },
            supportingContent = { Text("Print m instead of meter")},
            trailingContent = {
                Switch(
                    checked = userPreferences.abbreviateNames,
                    onCheckedChange = { onUserPreferenceChange(userPreferences.copy(abbreviateNames = it)) }
                )
            }
        )
        ListItem(
            headlineContent = { Text("Spacious output") },
            supportingContent = { Text("Use more spaces to improve readability")},
            trailingContent = {
                Switch(
                    checked = userPreferences.spaciousOutput,
                    onCheckedChange = { onUserPreferenceChange(userPreferences.copy(spaciousOutput = it)) }
                )
            }
        )
        ListItem(
            headlineContent = { Text("Allow prefix in denominator") },
            supportingContent = { Text("Print km${userPreferences.getDivisionSignString()}ms instead of Mm${userPreferences.getDivisionSignString()}s")},
            trailingContent = {
                Switch(
                    checked = userPreferences.useDenominatorPrefix,
                    onCheckedChange = { onUserPreferenceChange(userPreferences.copy(useDenominatorPrefix = it)) }
                )
            }
        )
        ListItem(
            headlineContent = { Text("Isolate units") },
            supportingContent = { Text("Place units at the end of the result")},
            trailingContent = {
                Switch(
                    checked = userPreferences.placeUnitsSeparately,
                    onCheckedChange = { onUserPreferenceChange(userPreferences.copy(placeUnitsSeparately = it)) }
                )
            }
        )
        SingleEnumSelectSettingsListItem<UserPreferences.ExpDisplay>(
            "Exp display",
            enumLabelMap = {
                when (it) {
                    UserPreferences.ExpDisplay.POWER_OF_10 -> "2${userPreferences.getMultiplicationSignString()}10³"
                    UserPreferences.ExpDisplay.LOWERCASE_E -> "XeY"
                    UserPreferences.ExpDisplay.UPPERCASE_E -> "2E3"
                }
            },
            currentSelection = userPreferences.expDisplay,
            onSelect = { onUserPreferenceChange(userPreferences.copy(expDisplay = it)) }
        )
    }
}


@Preview
@Composable
private fun DefaultPreview() {
    OutputSettingsScreenContent(
        userPreferences = UserPreferences(),
    )
}
