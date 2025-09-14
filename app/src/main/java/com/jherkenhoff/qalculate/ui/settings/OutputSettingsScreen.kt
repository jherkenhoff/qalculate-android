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
            headlineContent = { Text("Abbreviate names") },
            supportingContent = { Text("Prefer abbreviations of units and functions")},
            trailingContent = { Switch(checked = false, onCheckedChange = {  }) }
        )
        ListItem(
            headlineContent = { Text("Negative exponents") },
            supportingContent = { Text("Use m^-1 instead of 1/m")},
            trailingContent = { Switch(checked = false, onCheckedChange = {  }) }
        )
        ListItem(
            headlineContent = { Text("Spacious output") },
            supportingContent = { Text("Description")},
            trailingContent = { Switch(checked = false, onCheckedChange = {  }) }
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
