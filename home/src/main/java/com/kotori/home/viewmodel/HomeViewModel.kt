package com.kotori.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kotori.home.R
import com.kotori.home.repository.HomeRepository
import com.ximalaya.ting.android.opensdk.model.album.Album
import com.ximalaya.ting.android.opensdk.model.track.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Home各个tab下公用的数据存储类，封装repository并缓存数据到内存中
 * 如果需要对数据进行筛选再输出到界面中，在此操作Flow即可
 */
class HomeViewModel(private val repository: HomeRepository) : ViewModel(){

    /**
     * tab 下的 paging data
     */
    val recommendPagingData = repository.getRecommendAlbumPagingData().cachedIn(viewModelScope)

    val crosstalkPagingData = repository.getCrossTalkPagingData().cachedIn(viewModelScope)

    val novelPagingData = repository.getNovelPagingData().cachedIn(viewModelScope)

    val newsPagingData = repository.getNewsPagingData().cachedIn(viewModelScope)

    val musicPagingData = repository.getMusicPagingData().cachedIn(viewModelScope)



    /**
     * --------------------------- 专辑详情页 -------------------------
     */
    private val _currentAlbum = MutableStateFlow(Album())

    fun getTracksByAlbum(album: Album) : Flow<PagingData<Track>> {
        // 该函数是 Detail Activity 进来后必定调用的函数，在此处获取是否被订阅
        _currentAlbum.value = album
        viewModelScope.launch(Dispatchers.IO) {
            _isSubscribed.value = repository.isSubscribed(album)
        }
        // 信息保存完后返回paging的数据
        return repository.getTrackPagingData(album).cachedIn(viewModelScope)
    }

    // 当前界面的专辑是否被订阅
    private val _isSubscribed = MutableStateFlow(false)
    val isSubscribed = _isSubscribed.asStateFlow()

    // 订阅按钮要显示哪个
    val subscribeButtonImageRes = mapOf(
        true to R.drawable.ic_check_24px_rounded,
        false to R.drawable.ic_add_24px_rounded
    )

    fun addSubscribe(album: Album) {
        viewModelScope.launch {
            repository.addSubscription(album)
            // 添加完之后同步修改
            _isSubscribed.value = true
        }
    }

    fun deleteSubscribe(album: Album) {
        viewModelScope.launch {
            repository.deleteSubscription(album)
            // 删除完之后同步修改
            _isSubscribed.value = false
        }
    }
}