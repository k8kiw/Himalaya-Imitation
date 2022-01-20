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
import com.kotori.home.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

@Route(path = Constants.PATH_HOME_PAGE)
class HomeFragment : BaseDbFragment<FragmentHomeBinding>() {
    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_home

    override fun showTopBar(): Boolean = true


    override fun initView(root: View) {
        getTopBar()?.setTitle(R.string.title_home)
        getTopBar()?.addRightImageButton(
            R.drawable.ic_search_24px_rounded,
            R.id.topbar_right_about_button
        )?.setOnClickListener { view ->
            "搜索被点击".showToast()
        }

        LogUtil.d("TTTTTTTTTTTTTT------>", "$parentFragment")
        LogUtil.d("TTTTTTTTTTTTTT------>", "${showTopBar()}")

        mBinding.homeTestButton.setOnClickListener {

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

        }
    }


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