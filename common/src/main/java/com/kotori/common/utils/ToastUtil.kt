package com.kotori.common.utils

import android.widget.Toast
import com.kotori.common.base.BaseApplication

// 扩展函数显示toast
fun String.showToast() {
    Toast.makeText(
        BaseApplication.context,
        this,
        Toast.LENGTH_SHORT
    ).show()
}