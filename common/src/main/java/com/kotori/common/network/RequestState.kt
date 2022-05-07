package com.kotori.common.network

/**
 * 通用网络请求状态密封类
 */
sealed class RequestState {

    // data class Success<T>(val data: T): RequestState()

    // 不好使用泛型，因为list有类型擦除，除非基于Retrofit的Flow请求写法
    object Success: RequestState()

    object Loading: RequestState()

    object Empty: RequestState()

    data class Error(val errorMessage: String): RequestState()

}
