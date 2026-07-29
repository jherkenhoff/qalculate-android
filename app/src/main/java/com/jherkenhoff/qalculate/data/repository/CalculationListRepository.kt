package com.jherkenhoff.qalculate.data.repository

import android.util.Log
import androidx.room.withTransaction
import com.jherkenhoff.qalculate.data.database.QalculateDatabase
import com.jherkenhoff.qalculate.data.database.dao.CalculationHistoryItemDao
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime

class CalculationListRepository(
    private val db: QalculateDatabase,
    private val dao: CalculationHistoryItemDao
) {
    fun allItems(): Flow<List<CalculationHistoryItemData>> = dao.getAll()
    fun allItemsSorted(): Flow<List<CalculationHistoryItemData>> = dao.getAllSorted()
    fun allItemsById(): Flow<Map<Long, CalculationHistoryItemData>> = allItems().map {
        items -> items.associateBy { it.id }
    }

    suspend fun getItem(id: Long) = dao.getItem(id)
    suspend fun addItem(item : CalculationHistoryItemData) = dao.insert(item)
    suspend fun updateItem(item: CalculationHistoryItemData) = dao.update(item)
    suspend fun deleteItem(item: CalculationHistoryItemData) {
        db.withTransaction {
            dao.delete(item)
            if (dao.count() == 0) {
                dao.insert(CalculationHistoryItemData.empty())
            }
        }
    }
    suspend fun deleteAll() {
        db.withTransaction {
            dao.deleteAll()
            dao.insert(CalculationHistoryItemData.empty())
        }
    }
    suspend fun updateSortIndex(id: Long, sortIndex: Int) = dao.updateSortIndex(id, sortIndex)
    suspend fun updateSortIndex(ids: List<Long>) {
        db.withTransaction {
            ids.forEachIndexed { idx, id ->
                dao.updateSortIndex(id, idx)
            }
        }
    }
}