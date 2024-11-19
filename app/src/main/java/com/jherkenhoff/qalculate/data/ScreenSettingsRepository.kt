package com.jherkenhoff.qalculate.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.screenSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "screenSettings")

class ScreenSettingsRepository(private val context : Context) {

    private companion object {
        val IS_ALT_KEYBOARD_OPEN = booleanPreferencesKey("isAltKeyboardOpen")
    }

    val isAltKeyboardOpen: Flow<Boolean> = context.screenSettingsDataStore.data
        .map { preferences ->
            preferences[IS_ALT_KEYBOARD_OPEN] ?: true
        }

    suspend fun saveAltKeyboardOpen(isAltKeyboardOpen: Boolean) {
        context.screenSettingsDataStore.edit { preferences ->
            preferences[IS_ALT_KEYBOARD_OPEN] = isAltKeyboardOpen
        }
    }
}
