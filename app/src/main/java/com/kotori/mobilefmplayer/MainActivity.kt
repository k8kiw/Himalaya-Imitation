package com.kotori.mobilefmplayer

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.sdk.testSDKGetCategories
import com.kotori.common.support.Constants
import com.kotori.common.ui.showEditDialog
import com.kotori.common.ui.showInfoTipsDialog
import com.kotori.common.ui.showMsgTipsDialog
import com.kotori.mobilefmplayer.databinding.ActivityMainBinding
import com.qmuiteam.qmui.arch.QMUIActivity

@Route(path = Constants.PATH_MAIN)
class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        testSDKGetCategories()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

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