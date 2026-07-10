package com.jherkenhoff.qalculate.data.repository

import com.jherkenhoff.qalculate.data.database.dao.CalculationHistoryItemDao
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationHistoryStore(
    private val dao: CalculationHistoryItemDao
) {
    fun allItems(): Flow<List<CalculationHistoryItemData>> = dao.getAll()

    fun allItemsById(): Flow<Map<Long, CalculationHistoryItemData>> = dao.getAll().map {
        items -> items.associateBy { it.id }
    }
    suspend fun getItem(id: Long) = dao.getItem(id)
    suspend fun addItem(item : CalculationHistoryItemData) = dao.insert(item)
    suspend fun updateItem(item: CalculationHistoryItemData) = dao.update(item)
    suspend fun deleteItem(item: CalculationHistoryItemData) = dao.delete(item)
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun updateSortIndex(id: Long, sortIndex: Int) = dao.updateSortIndex(id, sortIndex)
}