package com.kotori.search.ktx

import android.widget.ImageView
import com.mancj.materialsearchbar.MaterialSearchBar
import com.qmuiteam.qmui.util.QMUIKeyboardHelper

/**
 * 搜索框扩展函数，扩展其自定义项
 */

/**
 * 修改菜单键的icon
 */
fun MaterialSearchBar.setNavIcon(navIconRes: Int) {
    val navIconResId = this.javaClass.getDeclaredField("navIconResId")
    navIconResId.isAccessible = true
    navIconResId.set(this, navIconRes)

    val navIcon = this.javaClass.getDeclaredField("navIcon")
    navIcon.isAccessible = true
    val imageView = navIcon.get(this) as ImageView
    imageView.setImageResource(navIconRes)
}

/**
 * 设置搜索框的内容，并执行搜索
 */
fun MaterialSearchBar.doSearch(keyword: String) {
    openSearch()
    text = keyword

    /*// 拿到当前的listener执行搜索
    val listenerField = this.javaClass.getDeclaredField("onSearchActionListener")
    listenerField.isAccessible = true
    val listener = listenerField.get(this) as MaterialSearchBar.OnSearchActionListener
    // 执行搜索
    listener.onSearchConfirmed(keyword)*/

    // 弹起键盘让用户修改再搜
    QMUIKeyboardHelper.showKeyboard(searchEditText, false)
    // 光标放最后
    searchEditText.setSelection(keyword.length)
}
