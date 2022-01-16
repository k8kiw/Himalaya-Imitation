package com.kotori.player

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.player.databinding.FragmentTempPlayerBinding

class TempPlayerFragment : BaseDbFragment<FragmentTempPlayerBinding>() {
    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_temp_player

    override fun initView(root: View) {

    }

    override fun showTopBar(): Boolean = false

    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}