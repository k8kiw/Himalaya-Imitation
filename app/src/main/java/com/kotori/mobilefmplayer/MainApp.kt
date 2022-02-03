package com.kotori.mobilefmplayer

import androidx.multidex.BuildConfig
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseApplication
import com.kotori.home.di.moduleHome
import com.kotori.player.di.modulePlayer
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApp : BaseApplication() {

    private val modules = arrayListOf(
        moduleHome,
        modulePlayer
    )

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initARouter()

        // 播放器初始化
        XmPlayerManager.getInstance(this).init()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApp)
            // 拿到全部模块，初始化
            modules(modules)
        }
    }

    private fun initARouter() {
        /*if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }*/
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }
}