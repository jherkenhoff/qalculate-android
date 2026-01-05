package com.jherkenhoff.qalculate.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jherkenhoff.qalculate.R
import com.jherkenhoff.qalculate.model.UserPreferences
import com.jherkenhoff.qalculate.ui.common.SegmentedListItem


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
        LazyColumn (
            verticalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {

            item {
                SettingsHeading("General")
            }

            item {
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
            }

            item {
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
            }

            item {
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


            item {
                SettingsHeading("Calculation")
            }

            item {
                SegmentedListItem(
                    headlineContent = { Text("Preserve structure") },
                    supportingContent = { Text("Preserve the input structure as much as possible") },
                    trailingContent = {
                        Switch(
                            checked = userPreferences.preserveFormat,
                            onCheckedChange = {
                                onUserPreferenceChange(
                                    userPreferences.copy(
                                        preserveFormat = it
                                    )
                                )
                            }
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    top = true,
                    bottom = true
                )
            }

            item {
                SettingsHeading("Output")
            }
            item {
                SegmentedListItem(
                    headlineContent = { Text("Negative exponents") },
                    supportingContent = { Text("Print A${userPreferences.getMultiplicationSignString()}B⁻¹ instead of A${userPreferences.getDivisionSignString()}B") },
                    trailingContent = {
                        Switch(
                            checked = userPreferences.negativeExponents,
                            onCheckedChange = {
                                onUserPreferenceChange(
                                    userPreferences.copy(
                                        negativeExponents = it
                                    )
                                )
                            }
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                    top = true
                )
            }

            item {
                SegmentedListItem(
                    headlineContent = { Text("Abbreviate names") },
                    supportingContent = { Text("Print m instead of meter")},
                    trailingContent = {
                        Switch(
                            checked = userPreferences.abbreviateNames,
                            onCheckedChange = { onUserPreferenceChange(userPreferences.copy(abbreviateNames = it)) }
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                )
            }

            item {
                SegmentedListItem(
                    headlineContent = { Text("Spacious output") },
                    supportingContent = { Text("Use more spaces to improve readability")},
                    trailingContent = {
                        Switch(
                            checked = userPreferences.spaciousOutput,
                            onCheckedChange = { onUserPreferenceChange(userPreferences.copy(spaciousOutput = it)) }
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                )
            }
            item {
                SegmentedListItem(
                    headlineContent = { Text("Allow prefix in denominator") },
                    supportingContent = { Text("Print km${userPreferences.getDivisionSignString()}ms instead of Mm${userPreferences.getDivisionSignString()}s")},
                    trailingContent = {
                        Switch(
                            checked = userPreferences.useDenominatorPrefix,
                            onCheckedChange = { onUserPreferenceChange(userPreferences.copy(useDenominatorPrefix = it)) }
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                )
            }
            item {
                SegmentedListItem(
                    headlineContent = { Text("Isolate units") },
                    supportingContent = { Text("Place units at the end of the result")},
                    trailingContent = {
                        Switch(
                            checked = userPreferences.placeUnitsSeparately,
                            onCheckedChange = { onUserPreferenceChange(userPreferences.copy(placeUnitsSeparately = it)) }
                        )
                    },
                    colors = ListItemDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.surfaceContainerLowest),
                )
            }
            item {
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
                    onSelect = { onUserPreferenceChange(userPreferences.copy(expDisplay = it)) },
                    bottom = true
                )
            }
            item {
                SingleEnumSelectSettingsListItem<UserPreferences.IntervalDisplay>(
                    "Interval display",
                    enumLabelMap = {
                        when (it) {
                            UserPreferences.IntervalDisplay.CONCISE -> "Concise"
                            UserPreferences.IntervalDisplay.INTERVAL -> "Interval"
                            UserPreferences.IntervalDisplay.PLUSMINUS -> "Plus minus"
                            UserPreferences.IntervalDisplay.MIDPOINT -> "Midpoint"
                            UserPreferences.IntervalDisplay.RELATIVE -> "Relative"
                            UserPreferences.IntervalDisplay.SIGNIFICANT_DIGITS -> "Significant digits"
                        }
                    },
                    currentSelection = userPreferences.intervalDisplay,
                    onSelect = { onUserPreferenceChange(userPreferences.copy(intervalDisplay = it)) },
                    bottom = true
                )
            }
        }
    }
}

@Composable
fun SettingsHeading(
    text: String
) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.tertiary,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 4.dp)
    )
}


@Preview
@Composable
private fun DefaultPreview() {
    SettingsScreenContent(UserPreferences())
}
