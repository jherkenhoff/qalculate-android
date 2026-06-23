package com.jherkenhoff.qalculate.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jherkenhoff.qalculate.data.database.dao.CalculationHistoryItemDao
import com.jherkenhoff.qalculate.data.database.model.CalculationHistoryItemData

@Database(
    version = 2,
    exportSchema = true,
    entities = [CalculationHistoryItemData::class],
    autoMigrations = [
        AutoMigration(from = 1, to = 2, spec = QalculateDatabase.Migration1to2::class)
    ]
)
@TypeConverters(DateTimeTypeConverters::class)
abstract class QalculateDatabase : RoomDatabase() {
    abstract fun calculationHistoryItemDao(): CalculationHistoryItemDao

    class Migration1to2 : AutoMigrationSpec {
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                UPDATE calculation_history
                SET modified = created
                WHERE modified = 0 OR modified IS NULL
            """.trimIndent())

            db.execSQL("""
                UPDATE calculation_history
                SET sort_index = id
                WHERE sort_index IS NULL
            """.trimIndent())

            super.onPostMigrate(db)
        }
    }
}