package com.kotori.common.sdk

import com.ximalaya.ting.android.opensdk.datatrasfer.CommonRequest
import com.ximalaya.ting.android.opensdk.datatrasfer.IDataCallBack
import com.ximalaya.ting.android.opensdk.model.category.CategoryList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 封装SDK的回调
 *
 */
suspend fun SDKGetAllCategories(specificParams: Map<String, String>) : CategoryList? {
    return suspendCoroutine { continuation ->
        // 调用sdk
        CommonRequest.getCategories(specificParams, object : IDataCallBack<CategoryList> {
            override fun onSuccess(p0: CategoryList?) {
                continuation.resume(p0)
            }

            override fun onError(p0: Int, p1: String?) {
                continuation.resumeWithException(Throwable(p1))
            }

        })
    }
}