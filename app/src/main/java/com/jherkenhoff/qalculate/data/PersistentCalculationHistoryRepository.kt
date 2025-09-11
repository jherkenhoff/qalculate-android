package com.jherkenhoff.qalculate.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jherkenhoff.qalculate.data.model.CalculationHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.calculationHistoryDataStore: DataStore<Preferences> by preferencesDataStore(name = "calculationHistory")

class PersistentCalculationHistoryRepository(private val context: Context) {
    companion object {
        private val HISTORY_KEY = stringPreferencesKey("calculation_history")
    }

    fun observeCalculationHistory(): Flow<List<CalculationHistoryItem>> =
        context.calculationHistoryDataStore.data.map { preferences ->
            preferences[HISTORY_KEY]?.let {
                try {
                    Json.decodeFromString<List<CalculationHistoryItem>>(it)
                } catch (e: Exception) {
                    emptyList()
                }
            } ?: emptyList()
        }

    suspend fun appendCalculation(calculation: CalculationHistoryItem) {
        context.calculationHistoryDataStore.edit { preferences ->
            val current = preferences[HISTORY_KEY]?.let {
                try {
                    Json.decodeFromString<MutableList<CalculationHistoryItem>>(it)
                } catch (e: Exception) {
                    mutableListOf()
                }
            } ?: mutableListOf()
            current.add(calculation)
            preferences[HISTORY_KEY] = Json.encodeToString(current)
        }
    }

    suspend fun appendCalculation(input: String, parsed: String, result: String) {
        appendCalculation(
            CalculationHistoryItem(
                java.time.LocalDateTime.now().toString(),
                input,
                parsed,
                result
            )
        )
    }
}
