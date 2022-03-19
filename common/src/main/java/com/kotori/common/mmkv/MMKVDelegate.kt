package com.kotori.common.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * MMKV的扩展，封装属性委托
 * 使用时只需用 by 属性委托，对该变量直接存取就可持久化缓存(key是属性名)
 * 使用方式：
 * ```kotlin
 *  // name 就是存取的key
 *  var name by mmkvString()
 * ```
 */
fun MMKVOwner.mmkvInt(default: Int = 0) =
    MMKVProperty(MMKV::decodeInt, MMKV::encode, default)

fun MMKVOwner.mmkvLong(default: Long = 0L) =
    MMKVProperty(MMKV::decodeLong, MMKV::encode, default)

fun MMKVOwner.mmkvBool(default: Boolean = false) =
    MMKVProperty(MMKV::decodeBool, MMKV::encode, default)

fun MMKVOwner.mmkvFloat(default: Float = 0f) =
    MMKVProperty(MMKV::decodeFloat, MMKV::encode, default)

fun MMKVOwner.mmkvDouble(default: Double = 0.0) =
    MMKVProperty(MMKV::decodeDouble, MMKV::encode, default)

fun MMKVOwner.mmkvString() =
    MMKVNullableProperty(MMKV::decodeString, MMKV::encode)

fun MMKVOwner.mmkvString(default: String) =
    MMKVNullablePropertyWithDefault(MMKV::decodeString, MMKV::encode, default)

fun MMKVOwner.mmkvStringSet(): ReadWriteProperty<MMKVOwner, Set<String>?> =
    MMKVNullableProperty(MMKV::decodeStringSet, MMKV::encode)

fun MMKVOwner.mmkvStringSet(default: Set<String>) =
    MMKVNullablePropertyWithDefault(MMKV::decodeStringSet, MMKV::encode, default)

fun MMKVOwner.mmkvBytes() =
    MMKVNullableProperty(MMKV::decodeBytes, MMKV::encode)

fun MMKVOwner.mmkvBytes(default: ByteArray) =
    MMKVNullablePropertyWithDefault(MMKV::decodeBytes, MMKV::encode, default)

inline fun <reified T : Parcelable> MMKVOwner.mmkvParcelable() =
    MMKVParcelableProperty(T::class.java)

inline fun <reified T : Parcelable> MMKVOwner.mmkvParcelable(default: T) =
    MMKVParcelablePropertyWithDefault(T::class.java, default)


/**
 * 复用的属性委托类，将属性的get set委托给mmkv的encode decode
 */
class MMKVProperty<V>(
    private val decode: MMKV.(String, V) -> V,
    private val encode: MMKV.(String, V) -> Boolean,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        thisRef.kv.decode(property.name, defaultValue)

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.kv.encode(property.name, value)
    }
}

class MMKVNullableProperty<V>(
    private val decode: MMKV.(String, V?) -> V?,
    private val encode: MMKV.(String, V?) -> Boolean
) : ReadWriteProperty<MMKVOwner, V?> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V? =
        thisRef.kv.decode(property.name, null)

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V?) {
        thisRef.kv.encode(property.name, value)
    }
}

class MMKVNullablePropertyWithDefault<V>(
    private val decode: MMKV.(String, V?) -> V?,
    private val encode: MMKV.(String, V?) -> Boolean,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        thisRef.kv.decode(property.name, null) ?: defaultValue

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.kv.encode(property.name, value)
    }
}

class MMKVParcelableProperty<V : Parcelable>(
    private val clazz: Class<V>
) : ReadWriteProperty<MMKVOwner, V?> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V? =
        thisRef.kv.decodeParcelable(property.name, clazz)

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V?) {
        thisRef.kv.encode(property.name, value)
    }
}

class MMKVParcelablePropertyWithDefault<V : Parcelable>(
    private val clazz: Class<V>,
    private val defaultValue: V
) : ReadWriteProperty<MMKVOwner, V> {
    override fun getValue(thisRef: MMKVOwner, property: KProperty<*>): V =
        thisRef.kv.decodeParcelable(property.name, clazz) ?: defaultValue

    override fun setValue(thisRef: MMKVOwner, property: KProperty<*>, value: V) {
        thisRef.kv.encode(property.name, value)
    }
}