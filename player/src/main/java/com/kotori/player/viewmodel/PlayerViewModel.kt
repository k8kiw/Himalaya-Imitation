package com.kotori.player.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotori.common.base.BaseApplication
import com.kotori.common.sdk.SDKCallbackExt
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {

    // 播放器实例，它可以直接拿到当前列表
    val playerManager = XmPlayerManager.getInstance(BaseApplication.context)

    /**
     * 当前播放的track
     */
    private val _currentTrack = MutableStateFlow(Track())

    val currentTrack : StateFlow<Track> = _currentTrack

    /**
     * 设置当前的track，自己或者view层都会调用
     * 设置完就需要获取对应列表，设置到播放器里
     */
    fun setCurrentTrack(track: Track) {
        _currentTrack.value = track
        // 设置列表数据
        viewModelScope.launch {
            // TODO：拿列表，得自己拿真的列表
            val trackList = _currentTrack.value.album?.albumId?.let {
                SDKCallbackExt.getCommonTrackListByAlbumId(it)
            }
            // 拿序号
            val order = _currentTrack.value.orderNum
            // 先暂停，list没变的情况下不会自己切
            if (playerManager.isPlaying) {
                playerManager.resetPlayList()
            }
            // 设置播放器
            playerManager.playList(trackList, 0)
            // 播放测试，这里的index指的是list里的index
            // TODO:播放器其实并不会自己切页
            playerManager.play(order)
        }
    }



}