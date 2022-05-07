package com.kotori.common.base

import com.kotori.common.network.RequestState
import com.kotori.common.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

/**
 * 使用Flow封装了基础的网络请求
 */
open class BaseRepository {

    val TAG: String = this.javaClass.simpleName

    /**
     * 通用请求方法
     * @param stateFlow: 网络请求状态
     * @param block: 具体的api请求方法
     */
    suspend fun <T: Any> executeReq(
        stateFlow: MutableStateFlow<RequestState>,
        block: suspend () -> T
    ) {
        // 借助flow的生命周期，完成进行网络请求
        flow {
            // 网络请求
            val result = block()
            // 发射数据
            emit(result)
        }
            .flowOn(Dispatchers.IO)
            .onStart {
                LogUtil.d(TAG, "Request Loading")
                val requestState = RequestState.Loading
                stateFlow.value = requestState
            }
            .onEmpty {
                LogUtil.d(TAG, "Request Result is Empty")
                val requestState = RequestState.Empty
                stateFlow.value = requestState
            }
            .catch { exception ->
                exception.printStackTrace()
                val errorMessage = exception.message ?: "Unknown Error!"
                val requestState = RequestState.Error(errorMessage)
                stateFlow.value = requestState
            }
            .collect {
                LogUtil.d(TAG, "Request Success")
                val requestState = RequestState.Success
                stateFlow.value = requestState
            }
    }
}