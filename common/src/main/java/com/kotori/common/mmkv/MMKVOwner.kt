package com.kotori.common.mmkv

import com.tencent.mmkv.MMKV

/**
 * 保存一个全局的mmkv默认实例，如需替换，则重写：
 *
 * ```kotlin
 * object DataRepository : MMKVOwner {
 *
 *   override val kv: MMKV = MMKV.mmkvWithID("MyID")
 * }
 * ```
 *
 */
val kv: MMKV = MMKV.defaultMMKV()

interface MMKVOwner {
    val kv: MMKV
        get() = com.kotori.common.mmkv.kv
}
