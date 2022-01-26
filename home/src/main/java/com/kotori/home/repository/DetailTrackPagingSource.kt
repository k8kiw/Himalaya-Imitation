package com.kotori.home.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kotori.common.sdk.SDKCallbackExt
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track

class DetailTrackPagingSource (private val album : Album): PagingSource<Int, Track>() {
    override fun getRefreshKey(state: PagingState<Int, Track>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Track> {
        return try {
            val page = params.key ?: 1

            // 请求
            val trackList = SDKCallbackExt.getTrackByAlbum(album, page)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (trackList.isNotEmpty()) page + 1 else null
            LoadResult.Page(trackList, prevKey, nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}