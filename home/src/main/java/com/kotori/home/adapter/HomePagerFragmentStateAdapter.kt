package com.kotori.home.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kotori.common.support.Constants
import com.kotori.home.fragment.BlankFragment
import com.kotori.home.fragment.HomeRecommendFragment

/**
 * 首页view pager2的适配器
 * 传入view pager所在的界面，以及需要的页面数目
 */
class HomePagerFragmentStateAdapter constructor(
    fragment : Fragment,
    private val pageList : List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = pageList.size

    override fun createFragment(position: Int): Fragment {
        // 创建fragment
        val fragment = when (pageList[position]) {
            "推荐" -> HomeRecommendFragment()
            else -> BlankFragment()
        }

        // 向传递参数
        fragment.arguments = Bundle().apply {
            putString(Constants.ARG_PAGER_ADAPTER, pageList[position])
        }

        return fragment
    }

}