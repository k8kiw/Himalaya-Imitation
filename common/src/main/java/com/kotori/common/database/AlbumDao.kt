package com.kotori.common.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    @Query("SELECT * FROM album")
    fun getAlbums(): Flow<List<MyAlbum>>

    @Query("SELECT * FROM album WHERE id IN (:albumIds)")
    fun loadAlbumsByIds(albumIds: List<Long>): Flow<List<MyAlbum>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAlbums(vararg myAlbums: MyAlbum)

    @Update
    fun updateAlbums(vararg myAlbums: MyAlbum)

    @Delete
    fun deleteAlbums(vararg myAlbums: MyAlbum)
}