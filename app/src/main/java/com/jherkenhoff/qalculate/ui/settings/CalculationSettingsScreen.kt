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
fun CalculationSettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit = {}
) {
    CalculationSettingsScreenContent(
        userPreferences = viewModel.userPreferences.collectAsStateWithLifecycle().value,
        onUserPreferenceChange = viewModel::updateUserPreferences,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculationSettingsScreenContent(
    userPreferences: UserPreferences,
    onUserPreferenceChange: (UserPreferences) -> Unit = {},
    onNavigateUp: () -> Unit = {  }
) {

    SubSettingScaffold(
        "Calculation settings",
        onNavigateUp = onNavigateUp
    ) {
        ListItem(
            headlineContent = { Text("Preserve structure") },
            supportingContent = { Text("Preserve the input structure as much as possible")},
            trailingContent = {
                Switch(
                    checked = userPreferences.preserveFormat,
                    onCheckedChange = { onUserPreferenceChange(userPreferences.copy(preserveFormat = it)) }
                )
            }
        )
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    CalculationSettingsScreenContent(
        userPreferences = UserPreferences()
    )
}
