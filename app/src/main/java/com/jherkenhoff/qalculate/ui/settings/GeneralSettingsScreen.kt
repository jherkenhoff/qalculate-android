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
fun GeneralSettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit = {}
) {
    GeneralSettingsScreenContent(
        userPreferences = viewModel.userPreferences.collectAsStateWithLifecycle().value,
        onUserPreferenceChange = viewModel::updateUserPreferences,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettingsScreenContent(
    userPreferences: UserPreferences,
    onUserPreferenceChange: (UserPreferences) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    SubSettingScaffold(
        "General settings",
        onNavigateUp = onNavigateUp
    ) {
        SingleEnumSelectSettingsListItem<UserPreferences.DecimalSeparator>(
            "Decimal separator",
            enumLabelMap = {
                when (it) {
                    UserPreferences.DecimalSeparator.DOT -> "Dot"
                    UserPreferences.DecimalSeparator.COMMA -> "Comma"
                }
            },
            currentSelection = userPreferences.decimalSeparator,
            onSelect = { onUserPreferenceChange(userPreferences.copy(decimalSeparator = it)) }
        )

        SingleEnumSelectSettingsListItem<UserPreferences.MultiplicationSign>(
            "Multiplication sign",
            enumLabelMap = {
                when (it) {
                    UserPreferences.MultiplicationSign.DOT -> "Dot"
                    UserPreferences.MultiplicationSign.X -> "Times"
                    UserPreferences.MultiplicationSign.ASTERISK -> "Asterisk"
                    UserPreferences.MultiplicationSign.ALTDOT -> "Alt. dot"
                }
            },
            currentSelection = userPreferences.multiplicationSign,
            onSelect = { onUserPreferenceChange(userPreferences.copy(multiplicationSign = it)) }
        )


        SingleEnumSelectSettingsListItem<UserPreferences.DivisionSign>(
            "Division sign",
            enumLabelMap = {
                when (it) {
                    UserPreferences.DivisionSign.DIVISION_SLASH -> "Division slash"
                    UserPreferences.DivisionSign.SLASH -> "Slash"
                    UserPreferences.DivisionSign.DIVISION -> "Division"
                }
            },
            currentSelection = userPreferences.divisionSign,
            onSelect = { onUserPreferenceChange(userPreferences.copy(divisionSign = it)) }
        )
    }
}


@Preview
@Composable
private fun DefaultPreview() {
    GeneralSettingsScreenContent(
        userPreferences = UserPreferences()
    )
}
