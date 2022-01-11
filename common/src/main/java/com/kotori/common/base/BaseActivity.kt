package com.kotori.common.base

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.kotori.common.ui.*
import com.qmuiteam.qmui.arch.QMUIActivity
import com.qmuiteam.qmui.widget.QMUITopBarLayout


/**
 * activity基类，
 */
abstract class BaseActivity<T : ViewDataBinding> : QMUIActivity(), IBaseView {

    /**
     * 打印日志要使用到的TAG
     */
    protected val TAG: String = this.javaClass.simpleName

    /**
     * db的初始化操作都是共通的，放到基类里
     */
    lateinit var mBinding: T

    /**
     * 内容层实例
     */
    private val mContent: View by lazy {
        createContentView()
    }

    /**
     * TopBar实例
     */
    private val mTopBar: QMUITopBarLayout? by lazy {
        createTopBar(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 更新状态栏
        updateStatusBarMode(isStatusBarLightMode())
        // 调用界面扩展函数，创建布局
        createView(this, translucentFull()).let {
            // 创建根据要求动态创建好布局后，进行设置
            setContentView(it)
            initView(it)
        }
    }

    /**
     * 创建内容层
     */
    internal open fun createContentView(): View {
        // 初始化
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            getLayoutId(),
            null,
            false
        )

        return mBinding.root
    }

    //------------------------以下为公共方法--------------------------------

    /**
     * 提供一个方法供子类获取TopBar
     */
    override fun getTopBar(): QMUITopBarLayout? = mTopBar

    /**
     * 是否需要TopBar(默认为根Fragment才需要)
     * 子类重写此方法进行修改
     */
    override fun showTopBar(): Boolean = true

    /**
     * 提供一个方法子类获取内容层
     * @return View
     */
    override fun getContentView(): View = mContent

    /**
     * @return 是否设置状态栏LightMode true 深色图标 false 白色背景
     * @remark 根据自己APP的配色，给定一个全局的默认模式。
     *         建议用TopBar的背景颜色做判断。或者在自己的BaseFragment里提供一个全局默认的模式。
     */
    override fun isStatusBarLightMode(): Boolean = true


    /**
     * @return 是否要进行对状态栏的处理
     */
    override fun isNeedChangeStatusBarMode(): Boolean = true

    /**
     * true -> 内容层将充满整个屏幕，直接延伸至状态栏
     *
     * false ->内容层将有一个向上的TopBar高度的间距
     */
    override fun translucentFull(): Boolean = false

    /**
     * 显示加载框
     * @param msg String? 提示语
     * @remark 这了提供了默认的加载效果，如果需要更改，重写此方法以及[hideLoading]
     */
    override fun showLoading(msg: String?) {
        showLoadingDialog(msg)
    }

    /**
     * 隐藏加载框
     */
    override fun hideLoading() {
        hideLoadingDialog()
    }
}