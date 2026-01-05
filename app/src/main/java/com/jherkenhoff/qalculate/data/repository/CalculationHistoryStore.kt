package com.jherkenhoff.qalculate.data.repository

import com.jherkenhoff.qalculate.data.database.dao.CalculationHistoryItemDao
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import kotlinx.coroutines.flow.Flow

class CalculationHistoryStore(
    private val dao: CalculationHistoryItemDao
) {
    fun allItems(): Flow<List<CalculationHistoryItemData>> = dao.getAll()

    suspend fun addItem(item : CalculationHistoryItemData) = dao.insert(item)

    suspend fun deleteItem(item: CalculationHistoryItemData) = dao.delete(item)

    suspend fun deleteAll() = dao.deleteAll()
}