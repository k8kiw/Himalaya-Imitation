package com.kotori.common.ui

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.kotori.common.R
import com.kotori.common.base.BaseApplication
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet.BottomListSheetBuilder
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheetListItemModel


/**
 * BottomSheet扩展函数，提取成工具函数
 * @param gravityCenter item项的文字是否处于中央
 * @param addCancelBtn 是否需要取消键
 * @param allowDragDismiss 是否可以下滑收起菜单栏
 * @param markIndex 当前正在播放的曲目 /*忽略：用主题色与左侧icon标记(不用右侧check)*/
 * @param title 菜单的标题
 * @param items 需要显示的list列表
 * @param clickCallback 单击的回调事件
 */
fun Activity.showBottomSheetList(
    gravityCenter: Boolean,
    addCancelBtn: Boolean,
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
        /*if (items[markIndex] == item) {
            *//*builder.addItem(
                ContextCompat.getDrawable(this, R.mipmap.icon_tabbar_lab),
                "Item $i"
            )*//*
            // 构建自己的被标记item项
            val markedItem = QMUIBottomSheetListItemModel(item, item)
            // 带icon和文字主题色
            markedItem.image(R.drawable.ic_equalizer_24px_rounded)
            markedItem.skinTextColorAttr(R.color.qmui_config_color_blue)
            // 插入菜单列表
            builder.addItem(markedItem)
        } else {

        }*/
        builder.addItem(item)
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
