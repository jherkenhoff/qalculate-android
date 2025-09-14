package com.jherkenhoff.qalculate.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jherkenhoff.libqalculate.DigitGrouping
import com.jherkenhoff.libqalculate.ExpDisplay
import com.jherkenhoff.libqalculate.IntervalDisplay
import com.jherkenhoff.libqalculate.MultiplicationSign
import com.jherkenhoff.libqalculate.NumberFractionFormat
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.qalculate.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val DECIMAL_SEPARATOR_KEY = stringPreferencesKey("decimal_separator")
        val NEGATIVE_EXPONENTS = booleanPreferencesKey("negative_exponents")
        val ABBREVIATE_NAMES = booleanPreferencesKey("abbreviate_names")
        val SPACIOUS = booleanPreferencesKey("spacious")
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map {

        val decimalSeparator = when (it[DECIMAL_SEPARATOR_KEY]) {
            "COMMA" -> UserPreferences.DecimalSeparator.COMMA
            else    -> UserPreferences.DecimalSeparator.DOT
        }

        return@map UserPreferences(
            decimalSeparator
        )
    }

    suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit {
            it[DECIMAL_SEPARATOR_KEY] = when (userPreferences.decimalSeparator) {
                UserPreferences.DecimalSeparator.DOT -> "DOT"
                UserPreferences.DecimalSeparator.COMMA -> "COMMA"
            }
        }
    }

    val printOptionsFlow : Flow<PrintOptions> = userPreferencesFlow.map {
        var po = PrintOptions()

        po.negative_exponents = false
        po.abbreviate_names   = true
        po.spacious           = true
        po.interval_display   = IntervalDisplay.INTERVAL_DISPLAY_CONCISE
        po.decimalpoint_sign  = "."
        po.number_fraction_format = NumberFractionFormat.FRACTION_DECIMAL
        po.digit_grouping = DigitGrouping.DIGIT_GROUPING_NONE
        po.min_exp = 4
        po.exp_display = ExpDisplay.EXP_POWER_OF_10
        po.multiplication_sign = MultiplicationSign.MULTIPLICATION_SIGN_ASTERISK
        po.use_unicode_signs = 1

        return@map po
    }
}