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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CalculationHistoryItemData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: CalculationHistoryItemData)

    @Delete
    suspend fun delete(entity: CalculationHistoryItemData): Int

    @Delete
    suspend fun delete(items: List<CalculationHistoryItemData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: Collection<CalculationHistoryItemData>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: CalculationHistoryItemData)
}