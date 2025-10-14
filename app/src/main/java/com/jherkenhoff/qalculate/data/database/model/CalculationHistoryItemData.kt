package com.jherkenhoff.qalculate.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "calculation_history")
data class CalculationHistoryItemData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "input")
    val input: String,
    @ColumnInfo(name = "parsed")
    val parsed: String,
    @ColumnInfo(name = "result")
    val result: String,
    @ColumnInfo(name = "created")
    val created: LocalDateTime,
)