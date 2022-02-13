package com.kotori.player.ui

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.common.support.PublicRepository
import com.kotori.common.ui.*
import com.kotori.common.utils.formatPlayProgress
import com.kotori.common.utils.formatTrackDuration
import com.kotori.common.utils.showToast
import com.kotori.common.utils.trimAlbumTitle
import com.kotori.player.R
import com.kotori.player.databinding.ActivityPlayerBinding
import com.kotori.player.viewmodel.PlayState
import com.kotori.player.viewmodel.PlayerViewModel
import com.qmuiteam.qmui.widget.QMUISlider
import com.ximalaya.ting.android.opensdk.model.track.Track
import com.ximalaya.ting.android.opensdk.player.service.XmPlayListControl.PlayMode
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = Constants.PATH_PLAYER_PAGE)
class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    private val mViewModel : PlayerViewModel by viewModel()

    @JvmField
    @Autowired(name = Constants.KEY_TRACK)
    var currentTrackFromDetail: Track? = null

    @JvmField
    @Autowired(name = Constants.KEY_TRACK_LIST)
    var currentTrackListFromDetail: List<Track> = ArrayList()


    override fun getLayoutId(): Int = R.layout.activity_player


    override fun initView(root: View) {
        initData()
        initTopBar()
        initPlayer()
        initListener()
        loadData()
    }

    /**
     * 接收路由跳转传入的数据
     */
    private fun initData() {
        ARouter.getInstance().inject(this)

        // 从主页跳转过来时，没有数据就不需要设置了
        currentTrackFromDetail?.apply {
            mViewModel.setCurrentTrackList(
                PublicRepository.trackList,
                this.orderNum
            )
        }
    }

    /**
     * 从View Model加载需要的数据到界面上
     */
    private fun loadData() {
        // 启动协程
        lifecycleScope.launch {
            // 一定要在界面的生命周期内更新，flow在后台依然能收到
            // start 时才执行，stop 后自动取消
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 监听当前的Track，刷新界面
                mViewModel.currentTrack.collect { currentTrack ->
                    // 显示Toast
                    "位置：${currentTrack.orderNum}".trimMargin().showToast()
                    // 加载到界面上
                    mBinding.apply {
                        // 加载头图和名称
                        playerAlbumCover.load(currentTrack.coverUrlLarge)
                        playerTrackTitle.text = currentTrack.trackTitle
                        playerTrackTitle.enableMarquee()
                        // 设置声音长度
                        playerDuration.text = currentTrack.duration.toString().formatTrackDuration()
                        // 加载标题栏的专辑名
                        getTopBar()?.setTitle(currentTrack.album?.albumTitle?.trimAlbumTitle())
                    }

                }
            }
        }

        // 监听播放列表
        // mViewModel.currentTrackList.collect {  }
    }

    /**
     * 设置TopBar样式，数据部分都在view model中，其他地方别管
     */
    private fun initTopBar() {
        getTopBar()?.apply {
            // 返回键
            addDefaultCloseButton().setOnClickListener { finish() }
            // 功能键
            addRightFunctionButton(R.drawable.ic_more_horiz_24px_rounded).setOnClickListener {
                // 弹出更多菜单：分享、下载
            }
            // 设置背景颜色
            setBackgroundColor(ContextCompat.getColor(
                this@PlayerActivity,
                R.color.qmui_config_color_background
            ))
            // 去除分割线
            setBottomDividerAlpha(0)
        }
    }

    /**
     * 处理播放器状态的变化，同步播放器进度、更新成该有的界面
     */
    private fun initPlayer() {

        // 需要切换的图片资源
        val pauseImageId = R.drawable.ic_pause_circle_outline_24px_rounded
        val playImageId = R.drawable.ic_play_circle_outline_24px_rounded
        // 三种播放模式的图片资源
        val playModeImageId = mapOf(
            PlayMode.PLAY_MODEL_LIST_LOOP to R.drawable.ic_repeat_24px_rounded,
            PlayMode.PLAY_MODEL_SINGLE_LOOP to R.drawable.ic_repeat_one_24px_rounded,
            PlayMode.PLAY_MODEL_RANDOM to R.drawable.ic_shuffle_24px_rounded
        )

        // 监听播放状态
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.currentPlayState.collect {
                    when(it) {
                        is PlayState.Error -> showFailTipsDialog("加载出错")
                        is PlayState.Pause -> {
                            mBinding.playerPlayButton.setImageResource(playImageId)
                        }
                        is PlayState.Loading -> {
                            // 缓冲进度分成不同颜色在进度条上显示
                        }
                        is PlayState.Playing -> {
                            mBinding.apply {
                                // 正在播放时更换暂停图标
                                playerPlayButton.setImageResource(pauseImageId)
                                // 更新播放进度
                                playerCurrentPosition.text = it.position.toString().formatPlayProgress()
                                // 更新声音长度
                                playerDuration.text = it.duration.toString().formatPlayProgress()
                                // 更新进度条，设置最大值与当前值
                                playerProgressBar.tickCount = it.duration
                                //val percent = (it.position * 1.0f / it.duration).toInt()
                                playerProgressBar.currentProgress = it.position
                            }
                        }
                    }
                }
            }
        }

        // 监听播放模式
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.currentPlayMode.collect {
                    // 拿到图片
                    val image = playModeImageId[it]
                    // 加载图片
                    if (image != null) {
                        mBinding.playerPlayModeButton.setImageResource(image)
                    }
                }
            }
        }
    }

    /**
     * 初始化页面上控件的点击、触摸等监听器
     */
    private fun initListener() {
        mBinding.apply {
            // 播放/暂停按钮的点击逻辑
            playerPlayButton.setOnClickListener {
                mViewModel.apply {
                    if (isPlaying) {
                        pause()
                    } else {
                        play()
                    }
                }
            }

            // 进度条的拖动逻辑
            playerProgressBar.setCallback(object : QMUISlider.Callback {
                override fun onProgressChange(
                    slider: QMUISlider?,
                    progress: Int,
                    tickCount: Int,
                    fromUser: Boolean
                ) {

                }

                override fun onTouchDown(
                    slider: QMUISlider?,
                    progress: Int,
                    tickCount: Int,
                    hitThumb: Boolean
                ) {

                }

                override fun onTouchUp(slider: QMUISlider?, progress: Int, tickCount: Int) {
                    mViewModel.seekTo(progress)
                }

                override fun onStartMoving(slider: QMUISlider?, progress: Int, tickCount: Int) {

                }

                override fun onStopMoving(slider: QMUISlider?, progress: Int, tickCount: Int) {

                }

                override fun onLongTouch(slider: QMUISlider?, progress: Int, tickCount: Int) {

                }

            })

            // 上一首
            playerPreviousButton.setOnClickListener {
                mViewModel.playPre()
            }

            // 下一首
            playerNextButton.setOnClickListener {
                mViewModel.playNext()
            }

            // 切换播放模式
            playerPlayModeButton.setOnClickListener {
                mViewModel.changePlayMode()
            }

            // 弹出播放列表
            playerPlayListButton.setOnClickListener {
                // 拿到列表
                val titleList = mViewModel.currentTrackList.value.map { it.trackTitle }
                // QMUI列表
                this@PlayerActivity.showBottomSheetList(
                    gravityCenter = false,
                    addCancelBtn = false,
                    allowDragDismiss = true,
                    title = "播放列表",
                    items = titleList,
                    markIndex = mViewModel.currentTrack.value.orderNum
                ) { dialog, _, position, tag ->
                    dialog.dismiss()
                    tag.showToast()
                    // 点击后切换track
                    mViewModel.play(position)
                }
            }
        }
    }


    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }

}