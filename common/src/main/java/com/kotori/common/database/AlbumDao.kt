package com.kotori.common.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM album")
    suspend fun getAlbums(): List<MyAlbum>

    @Query("SELECT * FROM album")
    fun getAlbumsWithLiveData(): LiveData<List<MyAlbum>>

    @Query("SELECT * FROM album")
    fun getAlbumsWithFlow(): Flow<List<MyAlbum>>

    @Query("SELECT * FROM album WHERE id IN (:albumIds)")
    fun getAlbumsWithFlowByIds(albumIds: List<Long>): Flow<List<MyAlbum>>

    /**
     * 仅供dao事务使用的查询方法，业务方要使用flow返回的接口
     */
    @Query("SELECT * FROM album WHERE id = :albumId")
    suspend fun getAlbumsById(albumId: Long): List<MyAlbum>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(vararg myAlbums: MyAlbum)

    @Update
    suspend fun updateAlbums(vararg myAlbums: MyAlbum)

    @Delete
    suspend fun deleteAlbums(vararg myAlbums: MyAlbum)

    /**
     * 以事务形式，查询某专辑是否被订阅
     * @param myAlbum 只考察其id
     */
    @Transaction
    suspend fun isSubscribed(myAlbum: MyAlbum): Boolean {
        // 得到查询列表
        val queryList = getAlbumsById(myAlbum.id)
        return queryList.isNotEmpty()
    }
}