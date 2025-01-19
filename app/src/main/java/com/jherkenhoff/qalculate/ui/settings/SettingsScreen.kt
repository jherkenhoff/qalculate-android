package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.qalculate.R


@Composable
fun SettingsScreen(
    onCalculationSettingsClick: () -> Unit = {},
    onInputSettingsClick: () -> Unit = {},
    onOutputSettingsClick: () -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    SettingsScreenContent(
        onCalculationSettingsClick = onCalculationSettingsClick,
        onInputSettingsClick = onInputSettingsClick,
        onOutputSettingsClick = onOutputSettingsClick,
        onNavigateUp = onNavigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    onCalculationSettingsClick: () -> Unit = {},
    onInputSettingsClick: () -> Unit = {},
    onOutputSettingsClick: () -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Settings")},
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.open_menu_content_description)
                        )
                    }

                },

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
                        headlineContent = { Text("Calculation") },
                        supportingContent = { Text("Approximation, Evaluation")},
                        leadingContent = { Icon(Icons.Outlined.Numbers, contentDescription = null)},
                        trailingContent = { Icon(Icons.Outlined.ChevronRight, contentDescription = null)},
                        modifier = Modifier.clickable(onClick = onCalculationSettingsClick)
                    )
                }
                item {
                    ListItem(
                        headlineContent = { Text("Input") },
                        supportingContent = { Text("Localization, Autocompletion")},
                        leadingContent = { Icon(Icons.Outlined.Keyboard, contentDescription = null)},
                        trailingContent = { Icon(Icons.Outlined.ChevronRight, contentDescription = null)},
                        modifier = Modifier.clickable(onClick = onInputSettingsClick)
                    )
                }
                item {
                    ListItem(
                        headlineContent = { Text("Output") },
                        supportingContent = { Text("Expression formatting")},
                        leadingContent = { Icon(Icons.Outlined.TextFields, contentDescription = null)},
                        trailingContent = { Icon(Icons.Outlined.ChevronRight, contentDescription = null)},
                        modifier = Modifier.clickable(onClick = onOutputSettingsClick)
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun DefaultPreview() {
    SettingsScreenContent(
    )
}
