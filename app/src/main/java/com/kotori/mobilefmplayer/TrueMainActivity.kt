package com.kotori.mobilefmplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.mobilefmplayer.databinding.ActivityTrueMainBinding

class TrueMainActivity : BaseActivity<ActivityTrueMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_true_main

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {
        initBottomBar()
    }

    private fun initBottomBar() {

    }

    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}