package com.kotori.personal

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ui.addLeftCloseImageBtn
import com.kotori.personal.databinding.FragmentPersonalBinding

class PersonalFragment: BaseDbFragment<FragmentPersonalBinding>() {
    override fun onLazyInit() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal

    override fun showTopBar(): Boolean = true


    override fun initView(root: View) {
        getTopBar()?.setTitle(R.string.title_personal)

        // 测试返回键
        //addLeftCloseImageBtn()
    }

    override fun showProgress(progress: ProgressBean) {
    }

    override fun hideProgress() {
    }

}