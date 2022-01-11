package com.kotori.common.base

import android.view.View
import com.kotori.common.entity.ProgressBean
import com.qmuiteam.qmui.widget.QMUITopBarLayout

/**
 * Activity/Fragment的公共方法，提取出来用于添加公共的扩展函数
 * 界面拆分为内容层与TopBar，封装好各种TopBar相关的方法
 * 界面只需要关心内容层，TopBar只需要设置是否显示
 */
interface IBaseView {

    //----------------------- 实际界面类才能写的方法 -------------------------
    /**
     * 获取布局，封装内容层，即R.layout中的内容
     */
    fun getLayoutId(): Int

    /**
     * 布局初始化
     * @param root View
     */
    fun initView(root: View)


    //----------------------- 基类可预写的方法 ----------------------------------
    /**
     * 提供一个方法供子类获取TopBar
     */
    fun getTopBar(): QMUITopBarLayout?

    /**
     * 是否需要TopBar
     */
    fun showTopBar(): Boolean

    /**
     * 获取内容层
     */
    fun getContentView(): View

    /**
     * 显示加载框
     * @param msg String?
     */
    fun showLoading(msg:String?)

    /**
     * 隐藏加载框
     */
    fun hideLoading()

    /**
     * 显示进度弹窗
     * @param progress ProgressUI
     */
    fun showProgress(progress: ProgressBean)

    /**
     * 隐藏进度弹窗
     */
    fun hideProgress()

    /**
     * @return 是否设置状态栏LightMode true 深色图标 false 白色背景
     */
    fun isStatusBarLightMode():Boolean

    /**
     * @return 是否要进行对状态栏的处理
     */
    fun isNeedChangeStatusBarMode():Boolean

    /**
     * true -> 内容层将充满整个屏幕，直接延伸至状态栏
     *
     * false ->内容层将有一个向上的TopBar高度的间距
     */
    fun translucentFull(): Boolean
}