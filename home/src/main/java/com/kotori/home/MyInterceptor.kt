package com.kotori.home

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.kotori.common.support.Constants
import com.ximalaya.ting.android.opensdk.model.track.Track


//@Interceptor(priority = 1, name = "player")
class MyInterceptor : IInterceptor {

    override fun init(context: Context?) {

    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        // 拦截页面跳转，在路由时传入列表，确保数据是最新的
        when(postcard?.path) {
            // 播放页面时，获取最新的列表，加入
            Constants.PATH_PLAYER_PAGE -> {
                postcard.withObject(Constants.KEY_TRACK_LIST, list)
            }
        }
        callback?.onContinue(postcard)
    }

    companion object {
        // 保存列表数据，让activity设置进来
        var list: List<Track> = ArrayList()
    }
}