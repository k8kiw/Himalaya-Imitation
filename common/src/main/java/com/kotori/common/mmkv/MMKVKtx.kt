package com.kotori.common.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * mmkv扩展，使其支持存取列表
 */
inline fun <reified T: Parcelable> MMKV.encodeParcelableList(
    key: String,
    list: List<T>
) {
    // 先存大小，方便循环
    this.encode("${key}_size", list.size)
    // 按编号存储数据
    for (i in list.indices) {
        this.encode("${key}_item_$i", list[i])
    }
}

inline fun <reified T: Parcelable> MMKV.decodeParcelableList(
    key: String
): List<T> {
    // 返回数组
    val result = mutableListOf<T>()
    // 取大小
    val listSize = this.decodeInt("${key}_size")
    for (i in 0 until listSize) {
        val item = this.decodeParcelable("${key}_item_$i", T::class.java)
        if (item != null) {
            result.add(item)
        }
    }
    return result
}


class MMKVParcelableListProperty<V : Parcelable>(
    private val list: List<V>,
    private val clazz: Class<V>
) : ReadWriteProperty<MMKVOwner, V?> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V? {
        return thisRef.kv.decodeParcelable(property.name, clazz)
    }

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V?) {
        thisRef.kv.encode(property.name, value)
    }
}

