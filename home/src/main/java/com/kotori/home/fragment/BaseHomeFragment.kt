package com.kotori.home.fragment

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ktx.launchAndRepeatWithViewLifecycle
import com.kotori.common.ui.showFailTipsDialog
import com.kotori.common.utils.showToast
import com.kotori.home.R
import com.kotori.home.adapter.RecommendAlbumPagingAdapter
import com.kotori.home.databinding.FragmentHomeRecommendBinding
import com.kotori.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseHomeFragment: BaseDbFragment<FragmentHomeRecommendBinding>(){

    val mViewModel : HomeViewModel by sharedViewModel()

    val pagingAdapter = RecommendAlbumPagingAdapter()

    override fun onLazyInit() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_home_recommend

    override fun initView(root: View) {
        initRecyclerView()
        initListener()
        loadData()
    }

    private fun initRecyclerView() {
        // 设置recycler view 适配器
        // 还可一并设置footer
        mBinding.homeRecommendList.adapter = pagingAdapter
        // 更改颜色
        mBinding.swipeRefreshLayout.setColorSchemeResources(R.color.qmui_config_color_blue)
    }

    /**
     * 初始化各种监听器
     */
    private fun initListener() {
        // 刷新监听
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            pagingAdapter.refresh()
        }
        // 刚开始加载时，主动弹出来刷新
        lifecycleScope.launchWhenCreated {
            pagingAdapter.loadStateFlow.collectLatest {
                // 监听加载状态
                when(it.refresh) {
                    is LoadState.Loading -> mBinding.swipeRefreshLayout.isRefreshing = true
                    is LoadState.NotLoading -> mBinding.swipeRefreshLayout.isRefreshing = false
                    is LoadState.Error -> {
                        // 隐藏刷新，不然一直在转回不去
                        mBinding.swipeRefreshLayout.isRefreshing = false
                        // 网络提示框
                        showFailTipsDialog("加载错误，请稍后再试")
                        // refresh 是字段无法被自动强转，只能自己转
                        val state = it.refresh as LoadState.Error
                        "${state.error.message}".showToast()
                    }
                }

            }
        }
    }

    private fun loadData() {
        launchAndRepeatWithViewLifecycle {
            loadAlbumList()
        }
    }

    /**
     * 供子类重写，直接写 flow 的 collect 逻辑就好
     */
    abstract suspend fun loadAlbumList()


    override fun showProgress(progress: ProgressBean) {
    }

    override fun hideProgress() {
    }

}