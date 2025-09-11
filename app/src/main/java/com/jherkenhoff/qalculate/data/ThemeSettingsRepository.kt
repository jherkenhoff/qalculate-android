package com.jherkenhoff.qalculate.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeSettingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "themeSettings")

class ThemeSettingsRepository(private val context: Context) {
    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("darkTheme")
    }

    val isDarkTheme: Flow<Boolean> = context.themeSettingsDataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }

    suspend fun saveDarkTheme(isDark: Boolean) {
        context.themeSettingsDataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }
}
