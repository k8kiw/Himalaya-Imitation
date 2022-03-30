package com.kotori.common.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "search_history")
data class SearchHistory(
    @PrimaryKey(autoGenerate = true) var id: Int?,
    @ColumnInfo(name = "keyword") val keyword: String,
    @ColumnInfo(name = "time") val time: Date
) {
    // 屏蔽id后的构造
    constructor(keyword: String,time: Date): this(null, keyword, time)
}
