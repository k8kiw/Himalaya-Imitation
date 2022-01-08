package com.kotori.mobilefmplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotori.common.sdk.testSDKGetCategories
import com.qmuiteam.qmui.arch.QMUIActivity
import com.qmuiteam.qmui.widget.QMUIWindowInsetLayout2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionBar?.hide()

        testSDKGetCategories()
    }
}