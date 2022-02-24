package com.kotori.search.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kotori.common.sdk.SDKCallbackExt
import com.ximalaya.ting.android.opensdk.model.album.Album

class SearchResultPagingSource(private val keyword: String): PagingSource<Int, Album>() {
    override fun getRefreshKey(state: PagingState<Int, Album>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        return try {
            // 获取页号，起始1
            val page = params.key ?: 1

            // 请求
            val resultList = SDKCallbackExt.getSearchedAlbums(keyword, page)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (resultList.isNotEmpty()) page + 1 else null
            LoadResult.Page(resultList, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}