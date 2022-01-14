package com.kotori.home

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ui.showInfoTipsDialog
import com.kotori.home.databinding.FragmentHomeBinding

class HomeFragment : BaseDbFragment<FragmentHomeBinding>() {
    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_home


    override fun initView(root: View) {
        getTopBar()?.setTitle("首页")


        mBinding.testButton.setOnClickListener {
            showInfoTipsDialog("点击了按钮")

        }
    }


    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}