package com.kotori.personal

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ui.addLeftCloseImageBtn
import com.kotori.common.utils.showToast
import com.kotori.common.widget.SettingView
import com.kotori.personal.databinding.FragmentPersonalBinding

class PersonalFragment: BaseDbFragment<FragmentPersonalBinding>() {
    override fun onLazyInit() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_personal

    override fun showTopBar(): Boolean = false


    override fun initView(root: View) {
        getTopBar()?.setTitle(R.string.title_personal)
        initListener()
    }

    private fun initListener() {
        mBinding.settingItemSubscribe.setOnSettingItemClick(object : SettingView.OnSettingItemClick {
            override fun click(v: View?) {
                "点击订阅".showToast()
            }
        })
    }

    override fun showProgress(progress: ProgressBean) {
    }

    override fun hideProgress() {
    }

}