package com.jherkenhoff.qalculate.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "calculation_history"
)
data class CalculationHistoryItemData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "sort_index", defaultValue = "0")
    val sortIndex: Int,
    @ColumnInfo(name = "input")
    val input: String,
    @ColumnInfo(name = "parsed")
    val parsed: String,
    @ColumnInfo(name = "result")
    val result: String,
    @ColumnInfo(name = "created")
    val created: LocalDateTime,
    @ColumnInfo(name = "modified", defaultValue = "0")
    val modified: LocalDateTime,
) {
    companion object {
        fun empty() = CalculationHistoryItemData(
            sortIndex = 0,
            input = "",
            parsed = "",
            result = "",
            created = LocalDateTime.now(),
            modified = LocalDateTime.now()
        )
    }
}