package com.jherkenhoff.qalculate.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface CalculationHistoryItemDao {

    @Query("SELECT * FROM calculation_history")
    fun getAll(): Flow<List<CalculationHistoryItemData>>

    @Query("SELECT * FROM calculation_history WHERE id = :id")
    suspend fun getItem(id: Long): CalculationHistoryItemData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CalculationHistoryItemData): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: CalculationHistoryItemData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: Collection<CalculationHistoryItemData>)

    @Delete
    suspend fun delete(entity: CalculationHistoryItemData): Int

    @Delete
    suspend fun delete(items: List<CalculationHistoryItemData>)

    @Query("DELETE FROM calculation_history")
    suspend fun deleteAll()

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: CalculationHistoryItemData)

    @Query("""
        UPDATE calculation_history
        SET sort_index = :sortIndex
        WHERE id = :id
        """)
    suspend fun updateSortIndex(id: Long, sortIndex: Int)
}