package com.kotori.home.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.coroutines.flow.Flow

/**
 * 数据仓库，封装各种来源的数据，提供出一致的接口
 * 对分页数据作flow的封装
 */
object HomeRepository {

    // 分页加载的配置
    private val config = PagingConfig(
        pageSize = 20,
        maxSize = 150
    )


    /**
     * 获取推荐列表中一页的数据，用flow封装
     */
    fun getRecommendAlbumPagingData() : Flow<PagingData<Album>> {
        return Pager(
            config = config,
            pagingSourceFactory = { RecommendAlbumPagingSource() }
        ).flow
    }
}