package com.kotori.player.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotori.common.base.BaseApplication
import com.kotori.common.sdk.SDKCallbackExt
import com.kotori.common.utils.LogUtil
import com.kotori.common.utils.showToast
import com.ximalaya.ting.android.opensdk.model.PlayableModel
import com.ximalaya.ting.android.opensdk.model.advertis.Advertis
import com.ximalaya.ting.android.opensdk.model.advertis.AdvertisList
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import com.ximalaya.ting.android.opensdk.player.advertis.IXmAdsStatusListener
import com.ximalaya.ting.android.opensdk.player.service.IXmPlayerStatusListener
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_LIST_LOOP
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_SINGLE_LOOP
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode.PLAY_MODEL_RANDOM
import com.ximalaya.ting.android.opensdk.player.service.XmPlayerException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

const val TAG = "PlayerViewModel"

class PlayerViewModel : ViewModel() {

    // 播放器实例，它可以直接拿到当前列表
    private val playerManager: XmPlayerManager = XmPlayerManager.getInstance(BaseApplication.context)

    init {
        // 对播放器设置回调工作
        addPlayerListener()
        // 默认播放模式为列表循环
        setPlayMode(PLAY_MODEL_LIST_LOOP)
    }

    companion object {
        // 播放模式的对应名称
        val playModeName = mapOf(
            PLAY_MODEL_LIST_LOOP to "列表循环",
            PLAY_MODEL_SINGLE_LOOP to "单曲循环",
            PLAY_MODEL_RANDOM to "随机播放"
        )
        // 播放模式切换顺序
        // 列表循环(默认) -> 单曲循环 -> 随机播放 -> 列表循环
        private val changeRule = mapOf(
            PLAY_MODEL_LIST_LOOP to PLAY_MODEL_SINGLE_LOOP,
            PLAY_MODEL_SINGLE_LOOP to PLAY_MODEL_RANDOM,
            PLAY_MODEL_RANDOM to PLAY_MODEL_LIST_LOOP
        )
    }

    /**
     * ======================  当前播放的track  ===========================
     */
    private val _currentTrack = MutableStateFlow(Track())

    val currentTrack : StateFlow<Track> = _currentTrack.asStateFlow()

    /**
     * 设置当前的track，自己或者view层都会调用
     */
    /*fun setCurrentTrack(track: Track) {
        _currentTrack.value = track
        // 设置列表数据
        viewModelScope.launch {
            // 拿序号
            val order = _currentTrack.value.orderNum
            // 先暂停，list没变的情况下不会自己切
            if (playerManager.isPlaying) {
                playerManager.resetPlayList()
            }
            // 设置播放器
            playerManager.playList(_currentTrackList.value, order)
            // 播放测试，这里的index指的是list里的index
            // 播放器其实并不会自己切页
            playerManager.play(order)
        }
    }*/

    /**
     * ======================  当前播放器的trackList  =========================
     */
    private val _currentTrackList: MutableStateFlow<List<Track>> = MutableStateFlow(ArrayList())

    val currentTrackList: StateFlow<List<Track>> = _currentTrackList.asStateFlow()

    /**
     *  供外部传入已加载完的数据，每次打开Player只会使用第一次
     *  @param list 专辑内容页面已经加载完的列表
     *  @param currentIndex 用户所点击的项目(点完后跳转进的播放器)
     */
    fun setCurrentTrackList(list: List<Track>, currentIndex: Int) {
        // 设进Flow保存数据
        _currentTrackList.value = list
        _currentTrack.value = list[currentIndex]
        // 自动播放当前歌曲
        // 先暂停，list没变的情况下不会自己切
        if (playerManager.isPlaying) {
            playerManager.resetPlayList()
        }
        // 设置播放器
        playerManager.playList(_currentTrackList.value, currentIndex)
        // 播放测试，这里的index指的是list里的index
        // 播放器其实并不会自己切页
        playerManager.play(currentIndex)
    }

    /**
     * =======================  当前播放器播放模式  ========================
     * 注意：SDK 没有回调可以使用，需要自己回显界面
     */
    // 对view展示的播放模式
    // TODO:val _currentPlayMode = MutableStateFlow()

    /**
     * 按照一定的顺序，进行播放模式的切换
     */
    fun changePlayMode() {
        // 拿到当前的模式
        val currentMode = playerManager.playMode
        // 切换到下一个
        val nextMode = changeRule[currentMode]

        setPlayMode(nextMode)
    }


    /**
     * ========================  播放器状态的观测  ========================
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

            // 播放器切歌后，最先被调用的回调，在此更新track
            override fun onSoundSwitch(lastModel: PlayableModel?, curModel: PlayableModel?) {
                LogUtil.d(TAG, "onSoundSwitch")
                // Track 是 PlayableModel 的子类，判断类型做强转即可
                if (curModel is Track) {
                    LogUtil.d(TAG, "onSoundSwitch ---> ${curModel.trackTitle}")
                    // 更新track
                    _currentTrack.value = curModel
                }
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

    private fun setPlayMode(mode : XmPlayListControl.PlayMode?) {
        playerManager.playMode = mode
        //TODO:设置当前playmode的state flow

        "当前播放模式：${playModeName[playerManager.playMode]}".showToast()
    }
}