package com.kotori.search.ui

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ktx.launchAndRepeatWithViewLifecycle
import com.kotori.common.utils.showToast
import com.kotori.search.R
import com.kotori.search.adapter.SearchResultPagingAdapter
import com.kotori.search.databinding.FragmentSearchResultBinding
import com.kotori.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchResultFragment : BaseDbFragment<FragmentSearchResultBinding>() {

    private val mViewModel: SearchViewModel by sharedViewModel()

    private val searchResultPagingAdapter = SearchResultPagingAdapter()


    override fun onLazyInit() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_search_result

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {

        initRecyclerView()
        initListener()
        loadData()
    }

    private fun initRecyclerView() {
        mBinding.searchResultList.adapter = searchResultPagingAdapter
    }

    private fun initListener() {
        // 加载、错误提示
        // 切换列表排序方式
    }

    private fun loadData() {
        launchAndRepeatWithViewLifecycle {
            mViewModel.currentSearchKeyword.collect {
                //TODO：keyword变化后需要重新执行搜索
                // 清空列表，重新加载
                mViewModel.searchResultList.collectLatest {
                    searchResultPagingAdapter.submitData(it)
                }
            }
        }

        /*lifecycleScope.launchWhenCreated {
            // 订阅列表数据源
            mViewModel.getSearchResult().collect {
                searchResultPagingAdapter.submitData(it)
            }
        }*/
    }


    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }
}