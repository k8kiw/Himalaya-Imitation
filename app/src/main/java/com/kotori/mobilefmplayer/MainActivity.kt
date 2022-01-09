package com.kotori.mobilefmplayer

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kotori.common.sdk.testSDKGetCategories
import com.kotori.mobilefmplayer.databinding.ActivityMainBinding
import com.qmuiteam.qmui.arch.QMUIActivity
import com.qmuiteam.qmui.arch.QMUIFragmentActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

class MainActivity : QMUIActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding = DataBindingUtil.inflate<ActivityMainBinding>(
            layoutInflater,
            R.layout.activity_main,
            null,
            false
        )
        setContentView(mBinding.root)

        actionBar?.hide()

        mBinding.topBar.setTitle("Title")

        //testSDKGetCategories()
    }
}