package com.kotori.common.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kotori.common.ui.*
import com.qmuiteam.qmui.arch.QMUIFragment
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import com.qmuiteam.qmui.widget.QMUITopBarLayout

abstract class BaseFragment : QMUIFragment(), IBaseView, LifecycleObserver {

    protected val TAG: String = this.javaClass.simpleName

    /**
     * 持有的属性
     */
    lateinit var mActivity: AppCompatActivity


    /**
     * 是否为根Fragment： getParentFragment() == null
     * 可作为一些默认情况的判断依据
     */
    private var isIndexFragment = false

    /**
     * 是否第一次加载
     */
    private var mIsFirstLayInit = true


    /**
     * ------------------ 内容层 ---------------------
     */
    private val mContent: View by lazy {
        createContentView()
    }

    /**
     * 提供一个方法供子类获取内容层
     */
    override fun getContentView(): View = mContent


    internal open fun createContentView(): View = layoutInflater.inflate(getLayoutId(), null)

    /**
     * ------------------- TopBar ------------------------
     */
    private val mTopBar: QMUITopBarLayout? by lazy {
        createTopBar(mActivity)
    }

    /**
     * 是否需要TopBar(默认为根Fragment才需要)
     * 子类重写此方法进行修改
     */
    override fun showTopBar(): Boolean = isIndexFragment

    /**
     * 提供一个方法供子类获取TopBar
     */
    override fun getTopBar(): QMUITopBarLayout? = mTopBar


    //------------------------ Fragment 重载------------------------------
    override fun onCreateView(): View {
        return createView(mActivity, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
        isIndexFragment = null == parentFragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //lazyViewLifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onViewCreated(rootView: View) {
        mIsFirstLayInit = true
        initView(rootView)
    }


    //------------------------ BaseView 重载------------------------------
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
        context?.showLoadingDialog(msg)
    }

    /**
     * 隐藏加载框
     */
    override fun hideLoading() {
        hideLoadingDialog()
    }

    /**
     * @return 是否要进行对状态栏的处理
     * @remark 默认当为根fragment时才进行处理
     */
    override fun isNeedChangeStatusBarMode(): Boolean = isIndexFragment

    /**
     * @return 是否设置状态栏LightMode true 深色图标 false 白色背景
     * @remark 根据自己APP的配色，给定一个全局的默认模式。
     *         建议用TopBar的背景颜色做判断。或者在自己的BaseFragment里提供一个全局默认的模式。
     */
    override fun isStatusBarLightMode(): Boolean = true


    //------------------------ lifecycle相关 ------------------------------
    /**
     * 当为 BaseFragmentActivity 的 DefaultFirstFragment 时，[isIndexFragment] = True , 但是并不会走 onEnterAnimationEnd()
     * 所以现在全部以界面可见时为懒加载时机
     */
    abstract fun onLazyInit()

    /**
     * 界面可见时
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onLazyResume() {
        if (isNeedChangeStatusBarMode()) {
            updateStatusBarMode(isStatusBarLightMode())
        }
        checkLazyInit()
    }

    /**
     * 检查是否需要延迟初始化
     */
    private fun checkLazyInit() {
        if (mIsFirstLayInit) {
            mIsFirstLayInit = false
            view?.post {
                onLazyInit()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onLazyPause() {
        QMUIKeyboardHelper.hideKeyboard(view)
    }

    /**
     * 向外提供的关闭方法
     */
    open fun finish() {
        // 这里一定要用这个方法
        onBackPressed()
        // 不能用这个
        // popBackStack()
    }
}