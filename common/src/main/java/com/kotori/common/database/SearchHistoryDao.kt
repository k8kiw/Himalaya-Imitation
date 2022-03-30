package com.kotori.common.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    // 按照时间倒序查询
    @Query("SELECT * FROM search_history ORDER BY time DESC")
    fun getHistories(): Flow<List<SearchHistory>>

    // 按keyword查询
    @Query("SELECT * FROM search_history WHERE keyword = :keyword")
    suspend fun getHistoriesByKeyword(keyword: String): List<SearchHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistories(vararg searchHistories: SearchHistory)

    @Update
    suspend fun updateHistories(vararg searchHistories: SearchHistory)

    @Delete
    suspend fun deleteHistories(vararg searchHistories: SearchHistory)

    @Transaction
    suspend fun insertOrUpdate(history: SearchHistory) {
        // 拿到搜索内容相同的历史记录
        val existHistories = getHistoriesByKeyword(history.keyword)
        // 如果没有则插入，如果有则删除再插
        if (existHistories.isNotEmpty()) {
            deleteHistories(*existHistories.toTypedArray())
        }
        insertHistories(history)
    }
}