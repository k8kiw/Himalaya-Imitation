package com.kotori.common.ui

import android.annotation.SuppressLint
import android.text.TextUtils
import android.widget.TextView

/**
 * xml设置跑马灯无效，使用代码动态设置
 */
fun TextView.setMarqueeEnable() {
    ellipsize = TextUtils.TruncateAt.MARQUEE
    isFocusableInTouchMode = true
    isSelected = true
    isSingleLine = true
    isFocusable = true
}