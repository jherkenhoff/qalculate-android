package com.jherkenhoff.qalculate.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jherkenhoff.qalculate.data.database.dao.CalculationHistoryItemDao
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData

@Database(
    entities = [CalculationHistoryItemData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class QalculateDatabase : RoomDatabase() {
    abstract fun calculationHistoryItemDao(): CalculationHistoryItemDao
}