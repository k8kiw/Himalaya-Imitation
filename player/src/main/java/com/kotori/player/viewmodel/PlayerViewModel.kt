package com.kotori.player.viewmodel

import androidx.lifecycle.ViewModel
import com.kotori.player.repository.PlayerRepository
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import kotlinx.coroutines.flow.*

class PlayerViewModel : ViewModel() {

    // 播放器实例，它可以直接拿到当前列表
    private val playerManager = PlayerRepository.playerManager

    // 当前播放的track
    val currentTrack: StateFlow<Track> = PlayerRepository.currentTrack.asStateFlow()

    // 当前播放器的trackList
    val currentTrackList: StateFlow<List<Track>> = PlayerRepository.currentTrackList.asStateFlow()

    /**
     *  详情页跳转需要设置列表
     */
    fun setCurrentTrackList(list: List<Track>, currentIndex: Int) {
        PlayerRepository.setCurrentTrackList(list, currentIndex)
    }


    // 当前播放器播放模式
    val currentPlayMode: StateFlow<XmPlayListControl.PlayMode> = PlayerRepository.currentPlayMode.asStateFlow()

    // 播放器页面需要进行切换操作
    fun changePlayMode() {
        PlayerRepository.changePlayMode()
    }


    // 当前播放器状态
    val currentPlayState: StateFlow<PlayState> = PlayerRepository.currentPlayState.asStateFlow()


    /**
     * =====================  封装一些播放器控制方法供界面使用  =====================
     */
    fun play(vararg index: Int) {
        when (index.size) {
            0 -> playerManager.play()
            1 -> playerManager.play(index[0])
        }
    }

    fun playPre() {
        playerManager.playPre()
    }

    fun playNext() {
        playerManager.playNext()
    }

    val isPlaying : Boolean
        get() = playerManager.isPlaying

    fun pause() {
        playerManager.pause()
    }

    fun seekTo(position : Int) {
        playerManager.seekTo(position)
        if (!playerManager.isPlaying) {
            playerManager.play()
        }
    }
}