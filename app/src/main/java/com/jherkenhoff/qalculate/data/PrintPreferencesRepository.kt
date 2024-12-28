package com.jherkenhoff.qalculate.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.jherkenhoff.libqalculate.PrintOptions
import com.jherkenhoff.qalculate.model.PrintPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PrintPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val ABBREVIATE_NAMES = booleanPreferencesKey("abbreviate_names")
    }

    val printPreferencesFlow: Flow<PrintPreferences> = dataStore.data.map {
        PrintPreferences(
            abbreviateNames = it[PreferencesKeys.ABBREVIATE_NAMES] ?: false
        )
    }

    suspend fun setAbbreviateNames(enabled: Boolean) {
        dataStore.edit { it[PreferencesKeys.ABBREVIATE_NAMES] = enabled }
    }

    suspend fun getQalculatePrintOptions(): PrintOptions {

        val printPreferences = dataStore.data.first()

        var po = PrintOptions()

        po.abbreviate_names = printPreferences[PreferencesKeys.ABBREVIATE_NAMES] ?: false

        return po
    }
}