package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.jherkenhoff.qalculate.R


@Composable
fun SettingsScreen(
    openDrawer: () -> Unit = {}
) {
    SettingsScreenContent(
        openDrawer = openDrawer,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    openDrawer: () -> Unit = {  }
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            CenterAlignedTopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text("Settings")},
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
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
                        headlineContent = { Text("Input") },
                        supportingContent = { Text("Format, Localization, Autocomplete")},
                        leadingContent = { Icon(Icons.Outlined.Keyboard, contentDescription = null)})
                }
                item {
                    ListItem(
                        headlineContent = { Text("Output") },
                        supportingContent = { Text("Format, Localization, Autocomplete")},
                        leadingContent = { Icon(Icons.Outlined.TextFields, contentDescription = null)})
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
