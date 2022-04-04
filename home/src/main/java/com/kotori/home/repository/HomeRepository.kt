package com.kotori.home.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.kotori.common.database.AppDatabase
import com.kotori.common.database.MyAlbum
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * 数据仓库，封装各种来源的数据，提供出一致的接口
 * 对分页数据作flow的封装
 */
class HomeRepository(database: AppDatabase) {

    private val albumDao = database.albumDao()

    companion object {
        private const val PAGE_SIZE = 50
        // 分页加载的配置
        private val config = PagingConfig(
            pageSize = PAGE_SIZE,
        )

        /**
         * 分类
         */
        private const val NEWS_ID = 1
        private const val MUSIC_ID = 2
        private const val NOVEL_ID = 3
        private const val CROSSTALK_ID = 12
    }


    /**
     * 获取推荐列表中一页的数据，用flow封装
     */
    fun getRecommendAlbumPagingData() : Flow<PagingData<Album>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = 150
            ),
            pagingSourceFactory = { RecommendAlbumPagingSource() }
        ).flow
    }

    fun getCrossTalkPagingData() : Flow<PagingData<Album>> {
        return Pager(config) {
            HomeAlbumPagingSource(CROSSTALK_ID)
        }.flow
    }

    fun getNewsPagingData() : Flow<PagingData<Album>> {
        return Pager(config) {
            HomeAlbumPagingSource(NEWS_ID)
        }.flow
    }

    fun getMusicPagingData() : Flow<PagingData<Album>> {
        return Pager(config) {
            HomeAlbumPagingSource(MUSIC_ID)
        }.flow
    }

    fun getNovelPagingData() : Flow<PagingData<Album>> {
        return Pager(config) {
            HomeAlbumPagingSource(NOVEL_ID)
        }.flow
    }

    /**
     * 封装好自己数据库的MyAlbum类，界面处只需传入官方Album
     */
    suspend fun isSubscribed(album: Album): Boolean {
        val myAlbum = MyAlbum(album)
        return albumDao.isSubscribed(myAlbum)
    }

    suspend fun addSubscription(album: Album) {
        withContext(Dispatchers.IO) {
            val myAlbum = MyAlbum(album)
            albumDao.insertAlbums(myAlbum)
        }
    }

    suspend fun deleteSubscription(album: Album) {
        withContext(Dispatchers.IO) {
            val myAlbum = MyAlbum(album)
            albumDao.deleteAlbums(myAlbum)
        }
    }


    /**
     * 根据专辑获取其声音内容
     */
    fun getTrackPagingData(album: Album) : Flow<PagingData<Track>> {
        return Pager(config) {
            DetailTrackPagingSource(album)
        }.flow
    }
}