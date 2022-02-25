package com.kotori.home.fragment

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.home.R
import com.kotori.home.databinding.FragmentBlankBinding

class BlankFragment : BaseDbFragment<FragmentBlankBinding>() {
    override fun onLazyInit() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_blank

    override fun initView(root: View) {
        // 通过参数设置按钮文字
        // takeIf 会检查对象是否满足某条件
        arguments?.takeIf { it.containsKey(Constants.ARG_PAGER_ADAPTER) }?.apply {
            mBinding.fragmentTestButton.text = getString(Constants.ARG_PAGER_ADAPTER)
        }
    }

    override fun showProgress(progress: ProgressBean) {
    }

    override fun hideProgress() {
    }
}