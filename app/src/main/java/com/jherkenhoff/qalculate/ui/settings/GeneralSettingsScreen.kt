package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
        ListItem(
            headlineContent = { Text("Decimal separator") },
            trailingContent = {

                SingleChoiceSegmentedButtonRow {
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        onClick = { onUserPreferenceChange(userPreferences.copy(decimalSeparator = UserPreferences.DecimalSeparator.DOT)) },
                        selected = userPreferences.decimalSeparator == UserPreferences.DecimalSeparator.DOT,
                        label = { Text("Dot") }
                    )
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        onClick = { onUserPreferenceChange(userPreferences.copy(decimalSeparator = UserPreferences.DecimalSeparator.COMMA)) },
                        selected = userPreferences.decimalSeparator == UserPreferences.DecimalSeparator.COMMA,
                        label = { Text("Comma") }
                    )
                }
            }
        )
        ListItem(
            headlineContent = { Text("Multiplication sign") },
            supportingContent = { Text("Dot") }
        )
        ListItem(
            headlineContent = { Text("Negative exponents") },
            supportingContent = { Text("Use m^-1 instead of 1/m")},
            trailingContent = { Switch(checked = false, onCheckedChange = {  }) }
        )
        ListItem(
            headlineContent = { Text("Spacious output") },
            supportingContent = { Text("Description")},
            trailingContent = { Switch(checked = false, onCheckedChange = {  })  }
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
