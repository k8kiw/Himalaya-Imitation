package com.kotori.home.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.kotori.common.sdk.SDKCallbackExt
import com.ximalaya.ting.android.opensdk.model.album.Album

class RecommendAlbumPagingSource : PagingSource<Int, Album>(){

    override fun getRefreshKey(state: PagingState<Int, Album>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Album> {
        // 获取加载页，默认为1
        val page = params.key ?: 1
        // 根据页数计算出实际的index
        val startIndex = (page - 1) * params.loadSize
        val endIndex = page * params.loadSize

        return try {
            // 根据index加载数据
            val albums = SDKCallbackExt.getRecommendAlbumList(startIndex, endIndex)
            // 填入前一页的 key 和后一页的 key
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (albums.isNotEmpty()) page + 1 else null
            // 返回分页结果，创建一个page
            LoadResult.Page(albums, prevKey, nextKey)
        } catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}