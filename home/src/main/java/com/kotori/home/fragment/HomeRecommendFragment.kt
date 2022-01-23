package com.kotori.home.fragment

import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ui.showFailTipsDialog
import com.kotori.common.utils.showToast
import com.kotori.home.R
import com.kotori.home.adapter.RecommendAlbumPagingAdapter
import com.kotori.home.databinding.FragmentHomeRecommendBinding
import com.kotori.home.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeRecommendFragment : BaseDbFragment<FragmentHomeRecommendBinding>(){

    private val mViewModel : HomeViewModel by viewModel()

    private val recommendAlbumPagingAdapter = RecommendAlbumPagingAdapter()

    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_home_recommend

    override fun initView(root: View) {
        initRecyclerView()
        initListener()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launchWhenCreated {
            // 订阅flow
            mViewModel.getRecommendAlbums().collect {
                recommendAlbumPagingAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView() {
        // 设置recycler view 适配器
        // 还可一并设置footer
        mBinding.homeRecommendList.adapter = recommendAlbumPagingAdapter
        // 更改颜色
        mBinding.swipeRefreshLayout.setColorSchemeResources(R.color.qmui_config_color_blue)
    }

    /**
     * 初始化各种监听器
     */
    private fun initListener() {
        // 刷新监听
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            recommendAlbumPagingAdapter.refresh()
        }
        // 刚开始加载时，主动弹出来刷新
        lifecycleScope.launchWhenCreated {
            recommendAlbumPagingAdapter.loadStateFlow.collectLatest {
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


    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}