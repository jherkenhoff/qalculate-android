package com.jherkenhoff.qalculate.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ScreenSettingsRepository(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val IS_ALT_KEYBOARD_OPEN = booleanPreferencesKey("isAltKeyboardOpen")
    }

    val isAltKeyboardOpen: Flow<Boolean> = dataStore.data
        .map { it[IS_ALT_KEYBOARD_OPEN] ?: true }

    suspend fun saveAltKeyboardOpen(isAltKeyboardOpen: Boolean) {
        dataStore.edit { it[IS_ALT_KEYBOARD_OPEN] = isAltKeyboardOpen }
    }
}
