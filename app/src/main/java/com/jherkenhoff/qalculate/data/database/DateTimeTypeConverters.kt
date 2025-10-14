package com.jherkenhoff.qalculate.data.database

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId


/**
 * Room [TypeConverter] functions for various `java.time.*` classes.
 */
object DateTimeTypeConverters {
    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {  LocalDateTime.ofInstant(
            Instant.ofEpochSecond(value),
            ZoneId.systemDefault()
        )}
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.atZone(ZoneId.systemDefault())?.toEpochSecond()
    }
}
