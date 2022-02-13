package com.kotori.player.ui

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ui.showBottomSheetList
import com.kotori.common.utils.showToast
import com.kotori.player.R
import com.kotori.player.databinding.FragmentPlayerBinding
import com.kotori.player.viewmodel.PlayState
import com.kotori.player.viewmodel.SmallPlayerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : BaseDbFragment<FragmentPlayerBinding>() {

    private val mViewModel : SmallPlayerViewModel by viewModel()

    override fun onLazyInit() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_player

    override fun showTopBar(): Boolean  = false

    override fun initView(root: View) {
        // 加载当前播放状态
        root.setOnClickListener {
            "打开播放器".showToast()
        }

        initListener()
        initPlayer()
        loadData()
    }

    private fun initPlayer() {
        // 需要切换的图片资源
        val pauseImageId = R.drawable.ic_pause_circle_outline_24px_rounded
        val playImageId = R.drawable.ic_play_circle_outline_24px_rounded

        // 根据不同状态显示
        launchAndRepeatWithViewLifecycle {

            mViewModel.currentPlayState.collect {
                when(it) {
                    is PlayState.Pause -> {
                        mBinding.playerSmallPlay.setImageResource(playImageId)
                    }
                    is PlayState.Playing -> {
                        mBinding.playerSmallPlay.setImageResource(pauseImageId)
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private fun initListener() {
        mBinding.apply {
            playerSmallPlay.setOnClickListener {
                mViewModel.apply {
                    if (isPlaying) {
                        pause()
                    } else {
                        play()
                    }
                }
            }

            playerSmallPlaylist.setOnClickListener {
                // 拿到列表
                val titleList = mViewModel.currentTrackList.value.map { it.trackTitle }
                // 弹出QMUI列表
                this@PlayerFragment.activity?.showBottomSheetList(
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

    private fun loadData() {
        launchAndRepeatWithViewLifecycle {
            mViewModel.currentTrack.collect {
                if (it.coverUrlSmall != null && it.trackTitle != null) {
                    // 显示
                    mBinding.playerSmallCover.load(it.coverUrlSmall)
                    mBinding.playerSmallTitle.text = it.trackTitle
                }
            }
        }
    }

    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }


    private inline fun launchAndRepeatWithViewLifecycle(
        minActiveState: Lifecycle.State = Lifecycle.State.RESUMED,
        crossinline block: suspend CoroutineScope.() -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
                block()
            }
        }
    }
}