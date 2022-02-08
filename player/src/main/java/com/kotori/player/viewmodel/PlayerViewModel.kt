package com.kotori.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotori.common.base.BaseApplication
import com.kotori.common.sdk.SDKCallbackExt
import com.kotori.common.utils.LogUtil
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val TAG = "PlayerViewModel"

class PlayerViewModel : ViewModel() {

    // 播放器实例，它可以直接拿到当前列表
    private val playerManager: XmPlayerManager = XmPlayerManager.getInstance(BaseApplication.context)

    init {
        // 对播放器设置回调工作
        addPlayerListener()
    }

    /**
     * =======================  当前播放的track  ===========================
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

    /**
     * =======================  播放状态的观测  =======================
     */
    private val _currentPlayState: MutableStateFlow<PlayState> = MutableStateFlow(PlayState.Pause)

    val currentPlayState: StateFlow<PlayState> = _currentPlayState

    private fun addPlayerListener() {
        // 播放器回调
        playerManager.addPlayerStatusListener(object : IXmPlayerStatusListener {
            override fun onPlayStart() {
                LogUtil.d(TAG, "onPlayStart")
            }

            override fun onPlayPause() {
                LogUtil.d(TAG, "onPlayPause")
                _currentPlayState.value = PlayState.Pause
            }

            override fun onPlayStop() {
                LogUtil.d(TAG, "onPlayStop")
                _currentPlayState.value = PlayState.Pause
            }

            override fun onSoundPlayComplete() {
                LogUtil.d(TAG, "onSoundPlayComplete")
            }

            override fun onSoundPrepared() {
                LogUtil.d(TAG, "onSoundPrepared")
            }

            override fun onSoundSwitch(lastModel: PlayableModel?, curModel: PlayableModel?) {
                LogUtil.d(TAG, "onSoundSwitch")
            }

            override fun onBufferingStart() {
                LogUtil.d(TAG, "onBufferingStart")
            }

            override fun onBufferingStop() {
                LogUtil.d(TAG, "onBufferingStop")
            }

            override fun onBufferProgress(percent: Int) {
                LogUtil.d(TAG, "onBufferProgress --> percent:$percent")
                _currentPlayState.value = PlayState.Loading(percent)
            }

            override fun onPlayProgress(currPos: Int, duration: Int) {
                LogUtil.d(TAG, "onPlayProgress --> currentPosition:$currPos, duration:$duration")
                _currentPlayState.value = PlayState.Playing(currPos, duration)
            }

            override fun onError(exception: XmPlayerException?): Boolean {
                LogUtil.d(TAG, "onError --> info:${exception?.message}")
                return false
            }

        })


        // 广告状态监听
        playerManager.addAdsStatusListener(object: IXmAdsStatusListener {
            override fun onStartGetAdsInfo() {
                LogUtil.d(TAG, "onStartGetAdsInfo")
            }

            override fun onGetAdsInfo(ads: AdvertisList?) {
                LogUtil.d(TAG, "onGetAdsInfo")
            }

            override fun onAdsStartBuffering() {
                LogUtil.d(TAG, "onAdsStartBuffering")
            }

            override fun onAdsStopBuffering() {
                LogUtil.d(TAG, "onAdsStopBuffering")
            }

            override fun onStartPlayAds(ad: Advertis?, position: Int) {
                LogUtil.d(TAG, "onStartPlayAds --> currentAd:$ad, index:$position")
            }

            override fun onCompletePlayAds() {
                LogUtil.d(TAG, "onCompletePlayAds")
            }

            override fun onError(what: Int, extra: Int) {
                LogUtil.d(TAG, "onError --> type:$what, code:$extra")
            }

        })
    }


    /**
     * =====================  封装一些播放器控制方法供view使用  =====================
     */
    fun play() {
        playerManager.play()
    }

    val isPlaying : Boolean
        get() = playerManager.isPlaying

    fun pause() {
        playerManager.pause()
    }
}