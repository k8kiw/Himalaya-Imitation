package com.kotori.common.ui

import androidx.annotation.StringRes
import com.kotori.common.R
import com.kotori.common.base.BaseFragment
import com.kotori.common.support.Constants.DEFAULT_LEFT_IMAGE
import com.qmuiteam.qmui.widget.QMUITopBarLayout


/**
 * 封装TopBar的基类
 */

fun QMUITopBarLayout.setTitleWithBackBtn(title: String?="", fragment: BaseFragment) {
    setTitle(title)
    addLeftCloseImageBtn(fragment)
}

fun QMUITopBarLayout.setTitleWithBackBtn(@StringRes resId: Int, fragment: BaseFragment) {
    setTitleWithBackBtn(context.getString(resId), fragment)
}

fun BaseFragment.setTitleWitchBackBtn(@StringRes resId: Int){
    setTitleWitchBackBtn(context?.getString(resId))
}

fun BaseFragment.setTitleWitchBackBtn(title: String?=""){
    getTopBar()?.setTitleWithBackBtn(title,this)
}

/**
 * 默认值为返回键
 */
fun BaseFragment.addLeftCloseImageBtn( drawableResId: Int = DEFAULT_LEFT_IMAGE){
    getTopBar()?.addLeftCloseImageBtn(this,drawableResId)
}

fun QMUITopBarLayout.addLeftCloseImageBtn(fragment: BaseFragment, drawableResId: Int = DEFAULT_LEFT_IMAGE) {
    when (drawableResId) {
        DEFAULT_LEFT_IMAGE -> addLeftBackImageButton()
        else -> addLeftImageButton(drawableResId, R.id.qmui_topbar_item_left_back)
    }.setOnClickListener {
        fragment.finish()
    }
}