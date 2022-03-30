package com.kotori.common.database

import androidx.room.TypeConverter
import java.util.*

/**
 * 在开发时用Date对象，数据库处存的是时间戳
 */
class DateConverter {
    @TypeConverter
    fun timestampToDate(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}