package com.jherkenhoff.qalculate.data

import androidx.datastore.core.DataStore
import com.jherkenhoff.qalculate.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepository(private val dataStore: DataStore<UserPreferences>) {

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data


    suspend fun setSyncUnits(enabled: Boolean) = dataStore.updateData { it.toBuilder().setSyncUnits(enabled).build() }

    suspend fun setAbbreviateNames(enabled: Boolean) = dataStore.updateData { it.toBuilder().setAbbreviateNames(enabled).build() }
    suspend fun setNegativeExponents(enabled: Boolean) = dataStore.updateData { it.toBuilder().setNegativeExponents(enabled).build() }
    suspend fun setSpacious(enabled: Boolean) = dataStore.updateData { it.toBuilder().setSpacious(enabled).build() }

    suspend fun setIsAltKeyboardOpen(open: Boolean) = dataStore.updateData { it.toBuilder().setAltKeyboardOpen(open).build() }
}