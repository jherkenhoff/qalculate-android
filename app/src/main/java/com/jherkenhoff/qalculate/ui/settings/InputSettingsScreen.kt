package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.data.model.UserPreferences

@Composable
fun InputSettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit = {}
) {
    val userPreferences = viewModel.userPreferences.collectAsStateWithLifecycle().value

    InputSettingsScreenContent(
        userPreferences = userPreferences,
        userPreferencesCallbacks = viewModel.userPreferencesCallbacks,
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputSettingsScreenContent(
    userPreferences: UserPreferences,
    userPreferencesCallbacks: UserPreferencesCallbacks,
    onNavigateUp: () -> Unit = {  }
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Input settings")},
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.open_menu_content_description)
                        )
                    }
                }
            )
        },
        modifier = Modifier.imePadding(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            LazyColumn {
                item {
                    ListItem(
                        headlineContent = { Text("Preserve format") },
                        supportingContent = { Text("Prefer abbreviations of units and functions")},
                        trailingContent = { Switch(checked = userPreferences.abbreviateNames, onCheckedChange = { userPreferencesCallbacks.onAbbreviateNamesChanged(it) }) }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun DefaultPreview() {
    InputSettingsScreenContent(
        userPreferences = UserPreferences.getDefaultInstance(),
        userPreferencesCallbacks = object : UserPreferencesCallbacks{}
    )
}
