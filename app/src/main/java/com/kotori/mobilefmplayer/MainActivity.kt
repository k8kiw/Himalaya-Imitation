package com.kotori.mobilefmplayer

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kotori.common.sdk.testSDKGetCategories
import com.kotori.mobilefmplayer.databinding.ActivityMainBinding
import com.qmuiteam.qmui.arch.QMUIActivity

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

        mBinding.topBar.setTitle("Title")

        //testSDKGetCategories()
    }
}