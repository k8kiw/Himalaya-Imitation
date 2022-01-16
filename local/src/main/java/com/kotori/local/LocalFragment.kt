package com.kotori.local

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.local.databinding.FragmentLocalBinding

class LocalFragment : BaseDbFragment<FragmentLocalBinding>() {
    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_local

    override fun showTopBar(): Boolean = true


    override fun initView(root: View) {
        getTopBar()?.setTitle(R.string.title_local)
        
    }

    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}