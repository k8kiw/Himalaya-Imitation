package com.kotori.common.ui

import android.content.Context
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog


/**
 * QMUI 消息弹窗的扩展函数
 * 消息弹窗是指，展示一些信息在屏幕中间一段时间后自动消失
 * 参考Demo
 */

/**
 * 全局保存的一个loading弹窗，避免重复创建
 */
private var loadingDialog: QMUITipDialog? = null

/**
 * 在activity/fragment中直接调用即可
 */
fun Context.showLoadingDialog(msg: CharSequence?) {
    if(null != loadingDialog){
        hideLoadingDialog()
    }
    loadingDialog = createQMUIDialog(msg, QMUITipDialog.Builder.ICON_TYPE_LOADING)
}

fun hideLoadingDialog() {
    loadingDialog?.dismiss()
    loadingDialog = null
}


/**
 * 单独文字
 */
fun Context.showMsgTipsDialog(msg: CharSequence?, delay: Long = 1000, callback: (() -> Any?)? = null) {
    showTipsDelayedDismiss(msg, QMUITipDialog.Builder.ICON_TYPE_NOTHING, delay, callback)
}

/**
 * 带一个信息icon + 文字
 */
fun Context.showInfoTipsDialog(msg: CharSequence?, delay: Long = 1000, callback: (() -> Any?)? = null) {
    showTipsDelayedDismiss(msg, QMUITipDialog.Builder.ICON_TYPE_INFO, delay, callback)
}

/**
 * 发送成功、发送失败
 */
fun Context.showSuccessTipsDialog(msg: CharSequence?, delay: Long = 1000, callback: (() -> Any?)? = null) {
    showTipsDelayedDismiss(msg, QMUITipDialog.Builder.ICON_TYPE_SUCCESS, delay, callback)
}

fun Context.showFailTipsDialog(msg: CharSequence?, delay: Long = 1000, callback: (() -> Any?)? = null) {
    showTipsDelayedDismiss(msg, QMUITipDialog.Builder.ICON_TYPE_FAIL, delay, callback)
}

/**
 * 经过一段时间后消失的弹窗
 */
fun Context.showTipsDelayedDismiss(
    msg: CharSequence?,
    type: Int,
    delay: Long = 1000,
    callback: (() -> Any?)? = null
) {
    val dialog = createQMUIDialog(msg, type)
    android.os.Handler().postDelayed({
        dialog.dismiss()
        callback?.invoke()
    }, delay)
}

/**
 * 封装Dialog的构建器
 */
fun Context.createQMUIDialog(msg: CharSequence?, type: Int): QMUITipDialog {
    return QMUITipDialog.Builder(this)
        .setIconType(type)
        .setTipWord(msg)
        .create().apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
}