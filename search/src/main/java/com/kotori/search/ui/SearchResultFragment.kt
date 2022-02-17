package com.kotori.search.ui

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ktx.launchAndRepeatWithViewLifecycle
import com.kotori.search.R
import com.kotori.search.databinding.FragmentSearchResultBinding
import com.kotori.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SearchResultFragment : BaseDbFragment<FragmentSearchResultBinding>() {

    private val mViewModel: SearchViewModel by sharedViewModel()


    override fun onLazyInit() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_search_result

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {

        loadData()
    }

    private fun loadData() {
        launchAndRepeatWithViewLifecycle {
            mViewModel.currentSearchKeyword.collect {
                //TODO：keyword变化后需要重新执行搜索
                mBinding.searchTestText.text = it
            }
        }
    }


    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }
}