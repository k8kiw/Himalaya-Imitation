package com.kotori.player

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.common.ui.addLeftCloseImageBtn
import com.kotori.common.utils.showToast
import com.kotori.player.databinding.FragmentTempPlayerBinding

class TempPlayerFragment : BaseDbFragment<FragmentTempPlayerBinding>() {


    override fun onLazyInit() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_temp_player

    override fun initView(root: View) {
        this.addLeftCloseImageBtn()
    }

    override fun showTopBar(): Boolean = true


    override fun showProgress(progress: ProgressBean) {
    }

    override fun hideProgress() {
    }

}