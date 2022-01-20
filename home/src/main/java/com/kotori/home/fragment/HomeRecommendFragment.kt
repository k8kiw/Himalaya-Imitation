package com.kotori.home.fragment

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.home.R
import com.kotori.home.databinding.FragmentHomeRecommendBinding

class HomeRecommendFragment : BaseDbFragment<FragmentHomeRecommendBinding>(){
    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_home_recommend

    override fun initView(root: View) {
        initListener()
    }

    /**
     * 刷新数据的监听器
     */
    private fun initListener() {
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            // adapter刷新数据
        }

    }


    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}