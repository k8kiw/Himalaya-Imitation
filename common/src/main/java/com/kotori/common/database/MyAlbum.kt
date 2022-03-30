package com.kotori.common.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ximalaya.ting.android.opensdk.model.album.Album

@Entity(tableName = "album")
data class MyAlbum(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val albumTitle: String,
    @ColumnInfo(name = "cover_url") val coverUrlSmall: String,
    @ColumnInfo(name = "intro") val albumIntro: String,
    @ColumnInfo(name = "play_count") val playCount: Long,
    @ColumnInfo(name = "subscribe_count") val subscribeCount: Long,
) {
    // 通过 Album 创建
    constructor(album: Album) : this(
        id = album.id,
        albumTitle = album.albumTitle,
        coverUrlSmall = album.coverUrlSmall,
        albumIntro = album.albumIntro,
        playCount = album.playCount,
        subscribeCount = album.subscribeCount
    )

    // 转换为官方 Album
    fun toAlbum(): Album {
        return Album().also {
            it.id = id
            it.albumTitle = albumTitle
            it.coverUrlSmall = coverUrlSmall
            it.albumIntro = albumIntro
            it.playCount = playCount
            it.subscribeCount = subscribeCount
        }
    }
}
