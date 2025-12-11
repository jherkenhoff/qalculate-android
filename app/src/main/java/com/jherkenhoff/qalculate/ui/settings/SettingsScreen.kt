package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AppSettingsAlt
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jherkenhoff.libqalculate.libqalculateConstants
import com.jherkenhoff.qalculate.BuildConfig
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.LicenseText


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateUp: () -> Unit = {}
) {
    SettingsScreenContent(
        userPreferences = viewModel.userPreferences.collectAsStateWithLifecycle().value,
        onUserPreferenceChange = viewModel::updateUserPreferences,
        onNavigateUp = onNavigateUp,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    userPreferences: UserPreferences,
    onUserPreferenceChange: (UserPreferences) -> Unit = {},
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
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
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
                onSelect = { onUserPreferenceChange(userPreferences.copy(decimalSeparator = it)) },
                top = true
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
                onSelect = { onUserPreferenceChange(userPreferences.copy(divisionSign = it)) },
                bottom = true
            )
        }
    }
}


@Preview
@Composable
private fun DefaultPreview() {
    SettingsScreenContent(UserPreferences())
}
