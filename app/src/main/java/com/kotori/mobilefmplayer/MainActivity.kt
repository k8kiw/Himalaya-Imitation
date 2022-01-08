package com.kotori.mobilefmplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotori.common.sdk.testSDKGetCategories

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testSDKGetCategories()
    }
}