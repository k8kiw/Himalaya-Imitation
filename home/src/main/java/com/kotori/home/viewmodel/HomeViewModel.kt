package com.kotori.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kotori.home.repository.HomeRepository
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import kotlinx.coroutines.flow.Flow

/**
 * Home各个tab下公用的数据存储类，封装repository并缓存数据到内存中
 * 如果需要对数据进行筛选再输出到界面中，在此操作Flow即可
 */
class HomeViewModel : ViewModel(){

    /**
     * tab 下的 paging data
     */
    val recommendPagingData = HomeRepository.getRecommendAlbumPagingData().cachedIn(viewModelScope)

    val crosstalkPagingData = HomeRepository.getCrossTalkPagingData().cachedIn(viewModelScope)

    val novelPagingData = HomeRepository.getNovelPagingData().cachedIn(viewModelScope)

    val newsPagingData = HomeRepository.getNewsPagingData().cachedIn(viewModelScope)

    val musicPagingData = HomeRepository.getMusicPagingData().cachedIn(viewModelScope)



    /**
     * --------------------------- 专辑详情页 -------------------------
     */
    fun getTracksByAlbum(album: Album) : Flow<PagingData<Track>> {
        return HomeRepository.getTrackPagingData(album).cachedIn(viewModelScope)
    }
}