package com.kotori.common.ui

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.kotori.common.R
import com.kotori.common.base.BaseApplication
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder


/**
 * BottomSheet扩展函数，提取成工具函数
 */
fun Activity.showBottomSheetList(
    gravityCenter: Boolean,
    addCancelBtn: Boolean,
    withIcon: Boolean,
    allowDragDismiss: Boolean,
    markIndex: Int,
    title: CharSequence,
    items: List<String>,
    clickCallback: (QMUIBottomSheet, View, Int, String) -> Unit
) {
    val builder = BottomListSheetBuilder(this)
        .setSkinManager(QMUISkinManager.defaultInstance(BaseApplication.context))
        .setGravityCenter(gravityCenter)
        .setTitle(title)
        .setAddCancelBtn(addCancelBtn)
        .setAllowDrag(allowDragDismiss)
        .setNeedRightMark(true)
        .setCheckedIndex(markIndex)
        .setOnSheetItemClickListener(clickCallback)

    for (item in items) {
        if (withIcon) {
            /*builder.addItem(
                ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_lab),
                "Item $i"
            )*/
        } else {
            builder.addItem(item)
        }
    }
    builder.build().show()
}

/*
// grid sheet
fun showSimpleBottomSheetGrid() {
    val TAG_SHARE_WECHAT_FRIEND = 0
    val TAG_SHARE_WECHAT_MOMENT = 1
    val TAG_SHARE_WEIBO = 2
    val TAG_SHARE_CHAT = 3
    val TAG_SHARE_LOCAL = 4
    val builder = BottomGridSheetBuilder(getActivity())
    builder.addItem(
        R.mipmap.icon_more_operation_share_friend,
        "分享到微信",
        TAG_SHARE_WECHAT_FRIEND,
        BottomGridSheetBuilder.FIRST_LINE
    )
        .addItem(
            R.mipmap.icon_more_operation_share_moment,
            "分享到朋友圈",
            TAG_SHARE_WECHAT_MOMENT,
            BottomGridSheetBuilder.FIRST_LINE
        )
        .addItem(
            R.mipmap.icon_more_operation_share_weibo,
            "分享到微博",
            TAG_SHARE_WEIBO,
            BottomGridSheetBuilder.FIRST_LINE
        )
        .addItem(
            R.mipmap.icon_more_operation_share_chat,
            "分享到私信",
            TAG_SHARE_CHAT,
            BottomGridSheetBuilder.FIRST_LINE
        )
        .addItem(
            R.mipmap.icon_more_operation_save,
            "保存到本地",
            TAG_SHARE_LOCAL,
            BottomGridSheetBuilder.SECOND_LINE
        )
        .setAddCancelBtn(true)
        .setSkinManager(QMUISkinManager.defaultInstance(getContext()))
        .setOnSheetItemClickListener { dialog, itemView ->
            dialog.dismiss()
            val tag = itemView.tag as Int
            when (tag) {
                TAG_SHARE_WECHAT_FRIEND -> Toast.makeText(
                    getActivity(),
                    "分享到微信",
                    Toast.LENGTH_SHORT
                )
                    .show()
                TAG_SHARE_WECHAT_MOMENT -> Toast.makeText(
                    getActivity(),
                    "分享到朋友圈",
                    Toast.LENGTH_SHORT
                )
                    .show()
                TAG_SHARE_WEIBO -> Toast.makeText(getActivity(), "分享到微博", Toast.LENGTH_SHORT).show()
                TAG_SHARE_CHAT -> Toast.makeText(getActivity(), "分享到私信", Toast.LENGTH_SHORT).show()
                TAG_SHARE_LOCAL -> Toast.makeText(getActivity(), "保存到本地", Toast.LENGTH_SHORT).show()
            }
        }.build().show()
}*/
