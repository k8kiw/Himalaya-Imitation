package com.kotori.player.viewmodel

import androidx.lifecycle.ViewModel
import com.kotori.player.repository.PlayerRepository
import com.ximalaya.ting.android.opensdk.model.track.Track
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SmallPlayerViewModel : ViewModel() {

    private val playerManager = PlayerRepository.playerManager

    /**
     * 界面上需要显示的信息
     */
    // 当前播放的track
    val currentTrack: StateFlow<Track> = PlayerRepository.currentTrack.asStateFlow()

    // 当前播放器的trackList
    val currentTrackList: StateFlow<List<Track>> = PlayerRepository.currentTrackList.asStateFlow()

    // 当前播放器状态
    val currentPlayState: StateFlow<PlayState> = PlayerRepository.currentPlayState.asStateFlow()


    /**
     * 供界面调用的操作播放器的方法
     */
    fun play(vararg index: Int) {
        when (index.size) {
            0 -> playerManager.play()
            1 -> playerManager.play(index[0])
        }
    }

    fun pause() {
        playerManager.pause()
    }

    val isPlaying : Boolean
        get() = playerManager.isPlaying
}