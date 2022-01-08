package com.kotori.mobilefmplayer

import android.os.Bundle
import com.kotori.common.sdk.testSDKGetCategories
import com.qmuiteam.qmui.arch.QMUIActivity
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout2

class MainActivity : QMUIActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testSDKGetCategories()
    }
}