package com.kotori.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.coroutines.flow.Flow

object SearchRepository {

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
}