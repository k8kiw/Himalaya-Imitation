package com.kotori.mobilefmplayer

import com.kotori.common.base.BaseApplication
import com.kotori.home.di.moduleHome
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApp : BaseApplication() {

    private val modules = arrayListOf(
        moduleHome
    )

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApp)
            // 拿到全部模块，初始化
            modules(modules)
        }
    }
}