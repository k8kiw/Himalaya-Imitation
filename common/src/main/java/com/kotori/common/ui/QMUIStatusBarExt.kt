package com.kotori.common.ui


import android.app.Activity
import androidx.fragment.app.Fragment
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * 更新状态栏模式
 *
 * @param isLight true 设置状态栏黑色字体图标，
 *
 * 支持 4.4 以上版本 MIUI 和 Flyme，以及 6.0 以上版本的其他 Android
 */
fun Activity.updateStatusBarMode(isLight: Boolean) {
    if (isLight) {
        QMUIStatusBarHelper.setStatusBarLightMode(this)
    } else {
        QMUIStatusBarHelper.setStatusBarDarkMode(this)
    }
}

fun Fragment.updateStatusBarMode(isLight: Boolean) {
    requireActivity().updateStatusBarMode(isLight)
}