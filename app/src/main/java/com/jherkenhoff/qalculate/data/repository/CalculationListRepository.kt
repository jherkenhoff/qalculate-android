package com.jherkenhoff.qalculate.data.repository

import androidx.room.withTransaction
import com.jherkenhoff.qalculate.data.database.QalculateDatabase
import com.jherkenhoff.qalculate.data.database.dao.CalculationHistoryItemDao
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationListRepository(
    private val db: QalculateDatabase,
    private val dao: CalculationHistoryItemDao
) {
    fun allItems(): Flow<List<CalculationHistoryItemData>> = dao.getAll()

    fun allItemsSorted(): Flow<List<CalculationHistoryItemData>> = dao.getAllSorted()

    fun allItemsById(): Flow<Map<Long, CalculationHistoryItemData>> = allItems().map {
        items -> items.associateBy { it.id }
    }

    suspend fun ensureNotEmpty() {
        if (dao.count() == 0) {
            dao.insert(CalculationHistoryItemData.empty())
        }
    }

    suspend fun getItem(id: Long) = dao.getItem(id)

    suspend fun addItem(item : CalculationHistoryItemData) = dao.insert(item)

    suspend fun updateItem(item: CalculationHistoryItemData) = dao.update(item)

    suspend fun deleteItem(item: CalculationHistoryItemData) {
        db.withTransaction {
            dao.delete(item)
            ensureNotEmpty()
        }
    }

    suspend fun deleteAll() {
        db.withTransaction {
            dao.deleteAll()
            ensureNotEmpty()
        }
    }

    suspend fun updateSortIndex(id: Long, sortIndex: Double) = dao.updateSortIndex(id, sortIndex)

    suspend fun updateSortIndex(ids: List<Long>) {
        db.withTransaction {
            ids.forEachIndexed { idx, id ->
                dao.updateSortIndex(id, idx.toDouble())
            }
        }
    }

    suspend fun insertAbove(
        referenceId: Long,
        item: CalculationHistoryItemData
    ) : Long {
        val reference = dao.getItem(referenceId)
        val previous = dao.getPrevious(reference.sortIndex)

        val newSortIndex = if (previous != null) {
            (previous.sortIndex + reference.sortIndex) / 2.0
        } else {
            reference.sortIndex - 1.0
        }

        return dao.insert(
            item.copy(sortIndex = newSortIndex)
        )
    }

    suspend fun insertBelow(
        referenceId: Long,
        item: CalculationHistoryItemData
    ) : Long {
        val reference = dao.getItem(referenceId)
        val next = dao.getNext(reference.sortIndex)

        val newSortIndex = if (next != null) {
            (next.sortIndex + reference.sortIndex) / 2.0
        } else {
            reference.sortIndex + 1.0
        }

        return dao.insert(
            item.copy(sortIndex = newSortIndex)
        )
    }
}