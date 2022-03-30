package com.kotori.common.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kotori.common.base.BaseApplication


@Database(
    entities = [MyAlbum::class, SearchHistory::class],
    version = 1
)
@TypeConverters(value = [DateConverter::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun albumDao(): AlbumDao

    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        private const val DATABASE_NAME = "PlayerDatabase"

        // 暴露出的数据库单例
        val instance: AppDatabase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(
                BaseApplication.context,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

}