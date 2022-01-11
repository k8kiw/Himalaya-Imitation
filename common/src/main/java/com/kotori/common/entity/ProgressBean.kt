package com.kotori.common.entity

/**
 * 进度条所使用的实体类
 * @property msg String?
 * @property percent Int
 * @property max Int
 * @property outSideCancel Boolean
 * @property keyBackCancel Boolean  返回键是否能关闭
 * @constructor
 */
data class ProgressBean(
    val msg: String? = "",
    val percent: Int = 0,
    val max: Int = 100,
    val outSideCancel: Boolean = false,
    val keyBackCancel: Boolean = false
) {

}