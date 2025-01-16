package com.jherkenhoff.qalculate.data

import androidx.datastore.core.DataStore
import com.jherkenhoff.qalculate.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepository(private val dataStore: DataStore<UserPreferences>) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data

    suspend fun setIsAltKeyboardOpen(open: Boolean) {
        dataStore.updateData { preferences ->
            preferences.toBuilder().setAltKeyboardOpen(open).build()
        }
    }
}