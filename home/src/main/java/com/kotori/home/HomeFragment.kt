package com.kotori.home

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.sdk.SDKCallbackExt
import com.kotori.common.support.Constants
import com.kotori.common.ui.showInfoTipsDialog
import com.kotori.common.utils.LogUtil
import com.kotori.common.utils.showToast
import com.kotori.home.adapter.HomePagerFragmentStateAdapter
import com.kotori.home.databinding.FragmentHomeBinding
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.tab.QMUITabIndicator
import com.qmuiteam.qmui.widget.tab.QMUITabSegment
import kotlinx.coroutines.launch

@Route(path = Constants.PATH_HOME_PAGE)
class HomeFragment : BaseDbFragment<FragmentHomeBinding>() {
    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun showTopBar(): Boolean = true


    override fun initView(root: View) {

        initTopBar()
        initTabAndPager()

        /*mBinding.homeTestButton.setOnClickListener {

            // 请求网络数据，记得捕获没网异常
            lifecycleScope.launch {
                val categories = SDKCallbackExt.getAllCategories(null)
                categories?.let { list ->
                    LogUtil.d(TAG, list.size.toString())

                    list.forEach {
                        LogUtil.d(TAG, it.toString())
                    }
                }
            }

        }*/
    }

    private fun initTopBar() {
        getTopBar()?.setTitle(R.string.title_home)
        getTopBar()?.addRightImageButton(
            R.drawable.ic_search_24px_rounded,
            R.id.topbar_right_about_button
        )?.setOnClickListener {
            "搜索被点击".showToast()
        }
    }

    /**
     * 初始化tab layout以及view pager
     * view pager 控制fragment的滑动，tab只要设置即可
     */
    private fun initTabAndPager() {
        val tabs = listOf("推荐", "小说", "相声", "玄幻", "时事")

        // 先初始化view pager, 后面设置tab时一并绑定
        mBinding.homeViewPager.apply {
            adapter = HomePagerFragmentStateAdapter(this@HomeFragment, tabs)
            // TODO:底部bottom切换后fragment不会保存，得切换才会重新刷新
            isSaveEnabled = false
        }

        // 设置tab
        mBinding.homeTabSegment.apply {
            tabs.forEach {
                // 设置tab文字和样式
                val tab = tabBuilder()
                    .setTextSize(
                        QMUIDisplayHelper.sp2px(context, 15),
                        QMUIDisplayHelper.sp2px(context, 18)
                    )
                    .setText(it)
                    .setColorAttr(R.attr.qmui_config_color_gray_1, R.attr.qmui_config_color_blue)
                    .build(context)
                addTab(tab)
            }
            // 添加完tab更新数据
            notifyDataChanged()

            // 设置样式
            // 设置指示器，在tab内容下方，且适应内容长度
            setIndicator(QMUITabIndicator(
                QMUIDisplayHelper.dp2px(context, 2),
                false,
                true
            ))

            // 设置模式：宽度固定，内容均分
            mode = QMUITabSegment.MODE_FIXED

            // 绑定view pager
            // TabLayout没有绑定ViewPager2的方法，所以才需要使用TabMediator做中介
            // TabSegment2已经自带
            setupWithViewPager(mBinding.homeViewPager)
        }

    }


    /**
     * 跳转播放页就是将专辑传过去给它播放
     */
    private fun startPlayerPage() {
        ARouter.getInstance().build(Constants.PATH_PLAYER_PAGE)
            .withString("test", "路由跳转")
            .navigation()
    }


    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}