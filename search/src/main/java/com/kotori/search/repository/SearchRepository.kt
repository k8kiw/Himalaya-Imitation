package com.kotori.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kotori.common.base.BaseRepository
import com.kotori.common.database.AppDatabase
import com.kotori.common.database.SearchHistory
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import org.koin.experimental.property.inject
import java.util.*

class SearchRepository(database: AppDatabase): BaseRepository() {

    private val historyDao = database.searchHistoryDao()

    /**
     * 搜索关键词获取结果
     */
    fun getSearchResultPagingData(keyword: String) : Flow<PagingData<Album>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            pagingSourceFactory = { SearchResultPagingSource(keyword) }
        ).flow
    }

    /**
     * 获取回一个flow，view model需要collect后转为stateflow
     */
    fun getAllHistories() = historyDao.getHistories()

    /**
     * 用suspend封装好数据库操作，view model中只需要放在协程中那就是异步了
     */
    suspend fun insertHistory(history: SearchHistory) {
        // 指定在IO协程中操作
        withContext(Dispatchers.IO) {
            historyDao.insertOrUpdate(history)
        }
    }

    suspend fun deleteHistory(history: SearchHistory) {
        // 指定在IO协程中操作
        withContext(Dispatchers.IO) {
            historyDao.deleteHistories(history)
        }
    }
}