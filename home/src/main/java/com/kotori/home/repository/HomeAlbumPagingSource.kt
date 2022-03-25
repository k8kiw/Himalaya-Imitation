package com.kotori.home.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kotori.common.sdk.SDKCallbackExt
import com.ximalaya.ting.android.opensdk.model.album.Album

class HomeAlbumPagingSource(private val id: Int): PagingSource<Int, Album>() {
    override fun getRefreshKey(state: PagingState<Int, Album>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        return try {
            val page = params.key ?: 1

            // 请求
            val albumList = SDKCallbackExt.getAlbumList(id, page)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (albumList.isNotEmpty()) page + 1 else null
            LoadResult.Page(albumList, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}