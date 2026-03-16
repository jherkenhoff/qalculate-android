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
                title = { Text(stringResource(R.string.settings_title))},
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
                SettingsHeading(stringResource(R.string.settings_general))
            }

            item {
                SingleEnumSelectSettingsListItem<UserPreferences.DecimalSeparator>(
                    stringResource(R.string.settings_decimal_separator),
                    enumLabelMap = {
                        when (it) {
                            UserPreferences.DecimalSeparator.DOT -> stringResource(R.string.settings_dot)
                            UserPreferences.DecimalSeparator.COMMA -> stringResource(R.string.settings_comma)
                        }
                    },
                    currentSelection = userPreferences.decimalSeparator,
                    onSelect = { onUserPreferenceChange(userPreferences.copy(decimalSeparator = it)) },
                    top = true
                )
            }

            item {
                SingleEnumSelectSettingsListItem<UserPreferences.MultiplicationSign>(
                    stringResource(R.string.settings_multiplication_sign),
                    enumLabelMap = {
                        when (it) {
                            UserPreferences.MultiplicationSign.DOT -> stringResource(R.string.settings_dot)
                            UserPreferences.MultiplicationSign.X -> stringResource(R.string.settings_times)
                            UserPreferences.MultiplicationSign.ASTERISK -> stringResource(R.string.settings_asterisk)
                            UserPreferences.MultiplicationSign.ALTDOT -> stringResource(R.string.settings_alt_dot)
                        }
                    },
                    currentSelection = userPreferences.multiplicationSign,
                    onSelect = { onUserPreferenceChange(userPreferences.copy(multiplicationSign = it)) }
                )
            }

            item {
                SingleEnumSelectSettingsListItem<UserPreferences.DivisionSign>(
                    stringResource(R.string.settings_division_sign),
                    enumLabelMap = {
                        when (it) {
                            UserPreferences.DivisionSign.DIVISION_SLASH -> stringResource(R.string.settings_division_slash)
                            UserPreferences.DivisionSign.SLASH -> stringResource(R.string.settings_slash)
                            UserPreferences.DivisionSign.DIVISION -> stringResource(R.string.settings_division)
                        }
                    },
                    currentSelection = userPreferences.divisionSign,
                    onSelect = { onUserPreferenceChange(userPreferences.copy(divisionSign = it)) },
                    bottom = true
                )
            }


            item {
                SettingsHeading(stringResource(R.string.settings_calculation))
            }

            item {
                SegmentedListItem(
                    headlineContent = { Text(stringResource(R.string.settings_preserve_structure)) },
                    supportingContent = { Text(stringResource(R.string.settings_preserve_structure_summary)) },
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
                SettingsHeading(stringResource(R.string.settings_output))
            }
            item {
                SegmentedListItem(
                    headlineContent = { Text(stringResource(R.string.settings_negative_exponents)) },
                    supportingContent = { Text(stringResource(R.string.settings_negative_exponents_summary, userPreferences.getMultiplicationSignString(), userPreferences.getDivisionSignString())) },
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
                    headlineContent = { Text(stringResource(R.string.settings_abbreviate_names)) },
                    supportingContent = { Text(stringResource(R.string.settings_abbreviate_names_summary))},
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
                    headlineContent = { Text(stringResource(R.string.settings_spacious_output)) },
                    supportingContent = { Text(stringResource(R.string.settings_spacious_output_summary))},
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
                    headlineContent = { Text(stringResource(R.string.settings_allow_prefix_denominator)) },
                    supportingContent = { Text(stringResource(R.string.settings_allow_prefix_denominator_summary, userPreferences.getDivisionSignString()))},
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
                    headlineContent = { Text(stringResource(R.string.settings_isolate_units)) },
                    supportingContent = { Text(stringResource(R.string.settings_isolate_units_summary))},
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
                    stringResource(R.string.settings_exp_display),
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
                    stringResource(R.string.settings_interval_display),
                    enumLabelMap = {
                        when (it) {
                            UserPreferences.IntervalDisplay.CONCISE -> stringResource(R.string.settings_interval_concise)
                            UserPreferences.IntervalDisplay.INTERVAL -> stringResource(R.string.settings_interval_interval)
                            UserPreferences.IntervalDisplay.PLUSMINUS -> stringResource(R.string.settings_interval_plus_minus)
                            UserPreferences.IntervalDisplay.MIDPOINT -> stringResource(R.string.settings_interval_midpoint)
                            UserPreferences.IntervalDisplay.RELATIVE -> stringResource(R.string.settings_interval_relative)
                            UserPreferences.IntervalDisplay.SIGNIFICANT_DIGITS -> stringResource(R.string.settings_interval_significant_digits)
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
