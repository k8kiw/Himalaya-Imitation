package com.kotori.player.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kotori.common.base.BaseApplication
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlayerViewModel : ViewModel() {

    // 播放器实例
    val playerManager = XmPlayerManager.getInstance(BaseApplication.context)

    // 当前播放的track
    private val _currentTrack = MutableStateFlow(Track())

    val currentTrack : StateFlow<Track> = _currentTrack

    fun setCurrentTrack(track: Track) {
        _currentTrack.value = track
    }


    // 获取列表
    fun getTrackList() {

    }
}