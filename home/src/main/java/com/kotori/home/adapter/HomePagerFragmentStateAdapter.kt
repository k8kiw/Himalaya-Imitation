package com.kotori.home.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kotori.common.support.Constants
import com.kotori.home.R
import com.kotori.home.fragment.*

/**
 * 首页view pager2的适配器
 * 传入view pager所在的界面，以及需要的页面数目
 */
class HomePagerFragmentStateAdapter constructor(
    private val fragment : Fragment,
    private val pageList : List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = pageList.size

    override fun createFragment(position: Int): Fragment {
        // 创建fragment
        val newFragment = when (pageList[position]) {
            fragment.getString(R.string.home_recommend_tab) -> HomeRecommendFragment()
            fragment.getString(R.string.home_crosstalk_tab) -> HomeCrosstalkFragment()
            fragment.getString(R.string.home_novel_tab) -> HomeNovelFragment()
            fragment.getString(R.string.home_news_tab) -> HomeNewsFragment()
            fragment.getString(R.string.home_music_tab) -> HomeMusicFragment()
            else -> BlankFragment()
        }

        // 向传递参数
        newFragment.arguments = Bundle().apply {
            putString(Constants.ARG_PAGER_ADAPTER, pageList[position])
        }

        return newFragment
    }

}