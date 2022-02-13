package com.kotori.mobilefmplayer

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.support.Constants


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 在界面绘制完成后再跳转
        val content: View = findViewById(R.id.splash_screen)
        content.post {
            Thread.sleep(1000)
            ARouter.getInstance().build(Constants.PATH_MAIN).navigation()
        }
    }

}