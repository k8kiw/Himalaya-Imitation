package com.kotori.common.sdk

import com.kotori.common.utils.LogUtil
import com.kotori.common.utils.showToast
import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.category.CategoryList


fun testSDKGetCategories() {

    // 多函数接口且多参数，只能匿名内部类
    /*CommonRequest.getCategories(map, object :IDataCallBack<CategoryList> {
        override fun onSuccess(categoryList: CategoryList?) {

            // 线程
            LogUtil.d(TAG, "当前线程为：${Thread.currentThread().name}")

            val categories = categoryList?.categories
            categories?.let { list ->
                LogUtil.d(TAG, list.size.toString())

                list.forEach {
                    LogUtil.d(TAG, it.toString())
                }
            }
        }

        override fun onError(code: Int, msg: String?) {
            LogUtil.e(TAG, "error code is $code, error msg is $msg")
        }

    })*/
}
