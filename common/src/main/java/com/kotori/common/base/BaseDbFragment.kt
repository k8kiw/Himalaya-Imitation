package com.kotori.common.base

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * 含有DataBinding的fragment基类
 */
abstract class BaseDbFragment<T : ViewDataBinding> : BaseFragment() {

    private lateinit var mBinding: T

    override fun createContentView(): View {
        // 初始化
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            getLayoutId(),
            null,
            false
        )

        return mBinding.root
    }


}