package com.kotori.player

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
import com.kotori.common.ui.addDefaultCloseButton
import com.kotori.common.ui.addRightFunctionButton
import com.kotori.common.ui.enableMarquee
import com.kotori.common.ui.showFailTipsDialog
import com.kotori.common.utils.formatPlayProgress
import com.kotori.common.utils.formatTrackDuration
import com.kotori.common.utils.showToast
import com.kotori.common.utils.trimAlbumTitle
import com.kotori.player.databinding.ActivityPlayerBinding
import com.kotori.player.viewmodel.PlayState
import com.kotori.player.viewmodel.PlayerViewModel
import com.qmuiteam.qmui.widget.QMUISlider
import com.ximalaya.ting.android.opensdk.model.track.Track
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = Constants.PATH_PLAYER_PAGE)
class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    private val mViewModel : PlayerViewModel by viewModel()

    @JvmField
    @Autowired(name = "track")
    var currentTrackFromDetail: Track? = null


    override fun getLayoutId(): Int = R.layout.activity_player



    override fun initView(root: View) {
        initData()
        initTopBar()
        initPlayer()
        initListener()
    }

    /**
     * 接收路由跳转传入的数据
     */
    private fun initData() {
        ARouter.getInstance().inject(this)

        // 传给 view model，之后就不用自己的数据了
        currentTrackFromDetail?.let {
            mViewModel.setCurrentTrack(it)
        }

        // 启动协程，监听当前的Track，刷新界面
        lifecycleScope.launch {
            // 一定要在界面的生命周期内更新，flow在后台依然能收到
            // start 时才执行，stop 后自动取消
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mViewModel.currentTrack.collect { currentTrack ->
                    // 显示Toast
                    """专辑名：${currentTrack.album?.albumTitle}
                        |位置：${currentTrack.orderNum}
                    """.trimMargin().showToast()
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
     * 处理播放器状态的变化，主要是更新界面
     */
    private fun initPlayer() {

        val pauseImageId = R.drawable.ic_pause_circle_outline_24px_rounded
        val playImageId = R.drawable.ic_play_circle_outline_24px_rounded

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 监听播放状态
                mViewModel.currentPlayState.collect {
                    when(it) {
                        is PlayState.Error -> showFailTipsDialog("加载出错")
                        is PlayState.Pause -> {
                            mBinding.playerPlayButton.setImageResource(playImageId)
                        }
                        is PlayState.Loading -> {

                        }
                        is PlayState.Playing -> {
                            mBinding.apply {
                                // 正在播放时更换暂停图标
                                playerPlayButton.setImageResource(pauseImageId)
                                // 更新播放进度
                                playerCurrentPosition.text = it.position.toString().formatPlayProgress()
                                // 更新声音长度
                                playerDuration.text = it.duration.toString().formatPlayProgress()
                                // 更新进度条
                                playerProgressBar.tickCount = it.duration
                                //val percent = (it.position * 1.0f / it.duration).toInt()
                                playerProgressBar.currentProgress = it.position
                            }
                        }
                    }
                }
            }
        }

    }

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
        }
    }


    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }

}