package com.kotori.home

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ui.showInfoTipsDialog
import com.kotori.common.utils.LogUtil
import com.kotori.home.databinding.FragmentHomeBinding

class HomeFragment : BaseDbFragment<FragmentHomeBinding>() {
    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun showTopBar(): Boolean = true


    override fun initView(root: View) {
        getTopBar()?.setTitle(R.string.title_home)

        LogUtil.d("TTTTTTTTTTTTTT------>", "$parentFragment")
        LogUtil.d("TTTTTTTTTTTTTT------>", "${showTopBar()}")

        mBinding.homeTestButton.setOnClickListener {
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