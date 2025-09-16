package com.jherkenhoff.qalculate.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
    }

    inline fun <reified T : Enum<T>> String?.toEnum() : T? {
        return enumValues<T>().firstOrNull { it.name == this }
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data.map { preferences ->

        return@map UserPreferences(
            decimalSeparator = preferences[DECIMAL_SEPARATOR_KEY].toEnum<UserPreferences.DecimalSeparator>()?:UserPreferences.DecimalSeparator.DOT,
            angleUnit = preferences[ANGLE_UNIT_KEY].toEnum<UserPreferences.AngleUnit>()?:UserPreferences.AngleUnit.DEGREES,
            multiplicationSign = preferences[MULTIPLICATION_SIGN_KEY].toEnum<UserPreferences.MultiplicationSign>()?:UserPreferences.MultiplicationSign.X,
            divisionSign = preferences[DIVISION_SIGN_KEY].toEnum<UserPreferences.DivisionSign>()?:UserPreferences.DivisionSign.DIVISION,
        )
    }

    suspend fun updateUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit {
            it[DECIMAL_SEPARATOR_KEY] = userPreferences.decimalSeparator.name
            it[ANGLE_UNIT_KEY] = userPreferences.angleUnit.name
            it[MULTIPLICATION_SIGN_KEY] = userPreferences.multiplicationSign.name
            it[DIVISION_SIGN_KEY] = userPreferences.divisionSign.name
        }
    }
}