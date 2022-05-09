package com.kotori.local.ui

import android.os.Bundle
import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ktx.launchAndRepeatWithViewLifecycle
import com.kotori.common.utils.LogUtil
import com.kotori.local.R
import com.kotori.local.adapter.SubscribeListAdapter
import com.kotori.local.databinding.FragmentLocalBinding
import com.kotori.local.viewmodel.LocalViewModel
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class LocalFragment : BaseDbFragment<FragmentLocalBinding>() {

    private val mViewModel: LocalViewModel by viewModel()

    private val adapter = SubscribeListAdapter(emptyList())


    override fun onLazyInit() {
    }

    override fun getLayoutId(): Int = R.layout.fragment_local

    override fun showTopBar(): Boolean = true

    /*override fun onResume() {
        super.onResume()
        LogUtil.d(TAG, "refresh data")
        // mViewModel.refreshData()
        LogUtil.d(TAG, "is null? ${mViewModel.albumList.value.size}")
    }*/

    override fun initView(root: View) {
        getTopBar()?.setTitle(R.string.title_local)
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        mBinding.subscribeList.adapter = adapter
    }

    private fun loadData() {
        launchAndRepeatWithViewLifecycle {
            LogUtil.d(TAG, "repeat register")
            mViewModel.albumList.collect {
                LogUtil.d(TAG, "subscribe items num:${it.size}")
                adapter.list = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initListener() {

    }

    override fun showProgress(progress: ProgressBean) {
    }

    override fun hideProgress() {
    }

}