package com.kotori.common.ui

import androidx.fragment.app.Fragment
import com.kotori.common.base.BaseFragment
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog



fun Fragment.showMsgTipsDialog(msg: CharSequence?, delay: Long = 2000, callback: (() -> Any?)? = null) {
    context?.showTipsDelayedDismiss(msg, QMUITipDialog.Builder.ICON_TYPE_NOTHING, delay, callback)
}

fun Fragment.showInfoTipsDialog(msg: CharSequence?, delay: Long = 2000, callback: (() -> Any?)? = null) {
    context?.showTipsDelayedDismiss(msg, QMUITipDialog.Builder.ICON_TYPE_INFO, delay, callback)
}

fun BaseFragment.showSuccessTipsExitDialog(msg: CharSequence?) {
    context?.showSuccessTipsDialog(msg) {
        finish()
    }
}

fun Fragment.showSuccessTipsDialog(msg: CharSequence?, delay: Long = 2000, callback: (() -> Any?)? = null) {
    context?.showTipsDelayedDismiss(msg, QMUITipDialog.Builder.ICON_TYPE_SUCCESS, delay, callback)
}

fun Fragment.showFailTipsDialog(msg: CharSequence?, delay: Long = 2000, callback: (() -> Any?)? = null) {
    context?.showTipsDelayedDismiss(msg, QMUITipDialog.Builder.ICON_TYPE_FAIL, delay, callback)
}