package com.kotori.player.ui

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.utils.showToast
import com.kotori.player.R
import com.kotori.player.databinding.FragmentPlayerBinding

class PlayerFragment : BaseDbFragment<FragmentPlayerBinding>() {

    override fun onLazyInit() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_player

    override fun showTopBar(): Boolean  = false

    override fun initView(root: View) {
        // 加载当前播放状态
        root.setOnClickListener {
            "打开播放器".showToast()
        }
    }

    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }

}