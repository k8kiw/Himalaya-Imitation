package com.kotori.ranking

import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.ranking.databinding.FragmentRankingBinding

class RankingFragment: BaseDbFragment<FragmentRankingBinding>() {
    override fun onLazyInit() {
        TODO("Not yet implemented")
    }

    override fun getLayoutId(): Int = R.layout.fragment_ranking

    override fun showTopBar(): Boolean = true


    override fun initView(root: View) {
        getTopBar()?.setTitle(R.string.title_rank)

    }

    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}