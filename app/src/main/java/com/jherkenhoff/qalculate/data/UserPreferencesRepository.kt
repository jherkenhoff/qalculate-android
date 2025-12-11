package com.jherkenhoff.qalculate.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jherkenhoff.qalculate.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val DECIMAL_SEPARATOR_KEY = stringPreferencesKey("decimal_separator")
        val ANGLE_UNIT_KEY = stringPreferencesKey("angle_unit")
        val MULTIPLICATION_SIGN_KEY = stringPreferencesKey("multiplication_sign")
        val DIVISION_SIGN_KEY = stringPreferencesKey("division_sign")
        val ABBREVIATE_NAMES_KEY = booleanPreferencesKey("abbreviate_names")
        val NEGATIVE_EXPONENTS_KEY = booleanPreferencesKey("negative_exponents")
        val SPACIEOUS_OUTPUT_KEY = booleanPreferencesKey("spacious_output")
        val APPROXIMATION_MODE_KEY = stringPreferencesKey("approximation_mode")
        val NUMERICAL_DISPLAY_MODE_KEY = stringPreferencesKey("numerical_display_mode")
        val NUMBER_FRACTION_FORMAT_KEY = stringPreferencesKey("number_fraction_format")
        val USE_DENOMINATOR_PREFIX_KEY = booleanPreferencesKey("use_denominator_prefix")
        val PLACE_UNITS_SEPARATELY = booleanPreferencesKey("place_units_separately")
        val PRESERVE_FORMAT_KEY = booleanPreferencesKey("preserve_format")
        val EXP_DISPLAY_KEY = stringPreferencesKey("exp_display")
    }

    inline fun <reified T : Enum<T>> String?.toEnum() : T? {
        return enumValues<T>().firstOrNull { it.name == this }
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->

        return@map UserPreferences(
            decimalSeparator = preferences[DECIMAL_SEPARATOR_KEY].toEnum<UserPreferences.DecimalSeparator>() ?: UserPreferences.Default.decimalSeparator,
            angleUnit = preferences[ANGLE_UNIT_KEY].toEnum<UserPreferences.AngleUnit>() ?: UserPreferences.Default.angleUnit,
            multiplicationSign = preferences[MULTIPLICATION_SIGN_KEY].toEnum<UserPreferences.MultiplicationSign>() ?: UserPreferences.Default.multiplicationSign,
            divisionSign = preferences[DIVISION_SIGN_KEY].toEnum<UserPreferences.DivisionSign>() ?: UserPreferences.Default.divisionSign,
            abbreviateNames = preferences[ABBREVIATE_NAMES_KEY] ?: UserPreferences.Default.abbreviateNames,
            negativeExponents = preferences[NEGATIVE_EXPONENTS_KEY] ?: UserPreferences.Default.negativeExponents,
            spaciousOutput = preferences[SPACIEOUS_OUTPUT_KEY] ?: UserPreferences.Default.spaciousOutput,
            approximationMode = preferences[APPROXIMATION_MODE_KEY].toEnum<UserPreferences.ApproximationMode>() ?: UserPreferences.Default.approximationMode,
            numericalDisplayMode = preferences[NUMERICAL_DISPLAY_MODE_KEY].toEnum<UserPreferences.NumericalDisplayMode>() ?: UserPreferences.Default.numericalDisplayMode,
            numberFractionFormat = preferences[NUMBER_FRACTION_FORMAT_KEY].toEnum<UserPreferences.NumberFractionFormat>() ?: UserPreferences.Default.numberFractionFormat,
            useDenominatorPrefix = preferences[USE_DENOMINATOR_PREFIX_KEY] ?: UserPreferences.Default.useDenominatorPrefix,
            placeUnitsSeparately = preferences[PLACE_UNITS_SEPARATELY] ?: UserPreferences.Default.placeUnitsSeparately,
            preserveFormat = preferences[PRESERVE_FORMAT_KEY] ?: UserPreferences.Default.preserveFormat,
            expDisplay = preferences[EXP_DISPLAY_KEY].toEnum<UserPreferences.ExpDisplay>() ?: UserPreferences.Default.expDisplay,
        )
    }

    suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit {
            it[DECIMAL_SEPARATOR_KEY] = userPreferences.decimalSeparator.name
            it[ANGLE_UNIT_KEY] = userPreferences.angleUnit.name
            it[MULTIPLICATION_SIGN_KEY] = userPreferences.multiplicationSign.name
            it[DIVISION_SIGN_KEY] = userPreferences.divisionSign.name
            it[ABBREVIATE_NAMES_KEY] = userPreferences.abbreviateNames
            it[NEGATIVE_EXPONENTS_KEY] = userPreferences.negativeExponents
            it[SPACIEOUS_OUTPUT_KEY] = userPreferences.spaciousOutput
            it[APPROXIMATION_MODE_KEY] = userPreferences.approximationMode.name
            it[NUMERICAL_DISPLAY_MODE_KEY] = userPreferences.numericalDisplayMode.name
            it[NUMBER_FRACTION_FORMAT_KEY] = userPreferences.numberFractionFormat.name
            it[USE_DENOMINATOR_PREFIX_KEY] = userPreferences.useDenominatorPrefix
            it[PLACE_UNITS_SEPARATELY] = userPreferences.placeUnitsSeparately
            it[PRESERVE_FORMAT_KEY] = userPreferences.preserveFormat
            it[EXP_DISPLAY_KEY] = userPreferences.expDisplay.name
        }
    }
}