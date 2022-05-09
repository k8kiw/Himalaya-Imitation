package com.kotori.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.kotori.common.database.AppDatabase
import com.kotori.common.utils.LogUtil
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalRepository(database: AppDatabase) {
    private val albumDao = database.albumDao()

    /**
     * 获取所有已订阅专辑
     */
    fun getAllSubscribedAlbums(): Flow<List<Album>> {
        // Flow<List<MyAlbum>> --> Flow<List<Album>>
        return albumDao.getAlbumsWithFlow().map { subscribedAlbumList ->
            // List<MyAlbum> --> List<Album>
            subscribedAlbumList.map { it.toAlbum() }
        }
    }



    fun getAlbumsWithLiveData(): LiveData<List<Album>> {
        // LiveData<List<MyAlbum>> --> LiveData<List<Album>>
        return albumDao.getAlbumsWithLiveData().map { subscribedAlbumList ->
            LogUtil.d("LocalFragment", "dataset changed: ${subscribedAlbumList.size}")
            // List<MyAlbum> --> List<Album>
            subscribedAlbumList.map { it.toAlbum() }
        }
    }

    suspend fun getAlbumsWithCoroutines(): List<Album> {
        return albumDao.getAlbums().map { it.toAlbum() }
    }
}