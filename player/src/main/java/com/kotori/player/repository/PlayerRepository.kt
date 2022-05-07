package com.kotori.player.repository

import com.kotori.common.base.BaseApplication
import com.kotori.common.mmkv.*
import com.kotori.common.utils.LogUtil
import com.kotori.common.utils.showToast
import com.kotori.player.viewmodel.PlayState
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
import kotlinx.coroutines.flow.MutableStateFlow


/**
 * home上的fragment与activity需要共享数据，需要抽取公共的播放信息到这里
 * 比如播放状态，很多StateFlow需要移到这里来
 */
object PlayerRepository: MMKVOwner{
    private const val TAG = "PlayerTAG"

    // mmkv持久化，在首页显示上一次的数据
    var lastTrackIndex by mmkvInt()
    private val lastTrackList = kv.decodeParcelableList<Track>("lastTrackList")

    // 播放器实例
    val playerManager: XmPlayerManager = XmPlayerManager.getInstance(BaseApplication.context)
        .also {
            // 如果被杀死还需要初始化
            if (!it.isConnected) {
                it.init()
            }
            // 初始化播放器得在它创建完成后
            it.replacePlayList(lastTrackList, lastTrackIndex)
            // 对播放器设置回调工作
            it.addPlayerStateListener()
            // 默认播放模式为列表循环
            it.playMode = PLAY_MODEL_LIST_LOOP
            // 打个toast看看信息
            /*"""播放器的列表size：${it.playList.size}
                |播放器的当前位置：${it.currentIndex}
            """.trimMargin().showToast()*/
        }

    // 播放模式的对应名称
    private val playModeName = mapOf(
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

    /**
     * ======================  当前播放的track  ===========================
     */
    val currentTrack = if (lastTrackList.isEmpty()) {
        MutableStateFlow(Track())
    } else {
        MutableStateFlow(lastTrackList[lastTrackIndex])
    }

    /**
     * ======================  当前播放器的trackList  =========================
     */
    val currentTrackList = MutableStateFlow(lastTrackList)


    /**
     *  供外部传入已加载完的数据，每次打开Player只会使用第一次
     *  @param list 专辑内容页面已经加载完的列表
     *  @param currentIndex 用户所点击的项目(点完后跳转进的播放器)
     */
    fun setCurrentTrackList(list: List<Track>, currentIndex: Int) {
        // 如果点进来的数据完全相同，则啥都不干，不然会被暂停一次
        if (list == currentTrackList.value && currentIndex == currentTrack.value.orderNum) {
            return
        }
        // 设进Flow保存数据
        currentTrackList.value = list
        currentTrack.value = list[currentIndex]
        // 持久化
        if (lastTrackList != list || lastTrackIndex != currentIndex) {
            kv.encodeParcelableList("lastTrackList", list)
            lastTrackIndex = currentIndex
        }

        playerManager.replacePlayList(list, currentIndex)
    }

    private fun XmPlayerManager.replacePlayList(list: List<Track>, index: Int) {
        // 自动播放当前歌曲
        // 先暂停，list没变的情况下不会自己切
        if (this.isPlaying) {
            this.resetPlayList()
        }
        // 设置播放器
        this.playList(list, index)
        // 播放测试，这里的index指的是list里的index
        // 播放器其实并不会自己切页
        this.play(index)
    }


    /**
     * =======================  当前播放器播放模式  ========================
     */
    // 对view展示的播放模式，SDK 没有回调可以使用，需要自己回显界面
    val currentPlayMode = MutableStateFlow(PLAY_MODEL_LIST_LOOP)

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

    // 设置播放模式时的公共操作
    private fun setPlayMode(mode : XmPlayListControl.PlayMode?) {
        mode?.let {
            playerManager.playMode = it
            //设置当前play mode的state flow
            currentPlayMode.value = it
        }
        "当前播放模式：${playModeName[playerManager.playMode]}".showToast()
    }


    /**
     * ========================  播放器状态的观测  ========================
     */
    val currentPlayState: MutableStateFlow<PlayState> = MutableStateFlow(PlayState.Pause)

    private fun XmPlayerManager.addPlayerStateListener() {
        // 播放器回调
        addPlayerStatusListener(object : IXmPlayerStatusListener {
            override fun onPlayStart() {
                LogUtil.d(TAG, "onPlayStart")
            }

            override fun onPlayPause() {
                LogUtil.d(TAG, "onPlayPause")
                currentPlayState.value = PlayState.Pause
            }

            override fun onPlayStop() {
                LogUtil.d(TAG, "onPlayStop")
                currentPlayState.value = PlayState.Pause
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
                    currentTrack.value = curModel
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
                currentPlayState.value = PlayState.Loading(percent)
            }

            override fun onPlayProgress(currPos: Int, duration: Int) {
                LogUtil.d(TAG, "onPlayProgress --> currentPosition:$currPos, duration:$duration")
                currentPlayState.value = PlayState.Playing(currPos, duration)
            }

            override fun onError(exception: XmPlayerException?): Boolean {
                LogUtil.d(TAG, "onError --> info:${exception?.message}")
                return false
            }

        })


        // 广告状态监听
        addAdsStatusListener(object: IXmAdsStatusListener {
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

}