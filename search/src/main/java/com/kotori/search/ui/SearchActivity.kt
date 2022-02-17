package com.kotori.search.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.alibaba.android.arouter.facade.annotation.Route
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.common.utils.showToast
import com.kotori.search.R
import com.kotori.search.adapter.AlbumSuggestionsAdapter
import com.kotori.search.databinding.ActivitySearchBinding
import com.mancj.materialsearchbar.MaterialSearchBar
import com.ximalaya.ting.android.opensdk.model.word.QueryResult

@Route(path = Constants.PATH_SEARCH_PAGE)
class SearchActivity : BaseActivity<ActivitySearchBinding>(){

    override fun getLayoutId(): Int = R.layout.activity_search

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {

        initSearchBar()
    }

    private fun initSearchBar() {
        mBinding.searchBar.apply {
            // 设置搜索框样式
            setPlaceHolder("搜索你感兴趣的内容吧")
            setHint("输入搜索内容")
            setMaxSuggestionCount(5)
            setSpeechMode(false)
            setRoundedSearchBarEnabled(true)
            // 调整card view 向后的阴影程度
            setCardViewElevation(5)

            // 用左边的菜单键代替退出
            setNavButtonEnabled(true)
            // 更改NavIcon
            setNavIcon(R.drawable.ic_home_used_in_search_bar)

            // 设置联想内容适配器
            val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val adapter = AlbumSuggestionsAdapter(layoutInflater)
            //TODO：假数据模拟联想
            val testList1 = listOf("111", "222", "444", "666")
            val testList2 = listOf("777", "888", "999", "000")
            val suggestions1: MutableList<QueryResult> = genSuggestions(testList1)
            val suggestions2: MutableList<QueryResult> = genSuggestions(testList2)
            // 设置联想数据
            adapter.suggestions = suggestions1
            this.setCustomSuggestionAdapter(adapter)

            // 设置监听器
            setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {
                    if (!enabled) {
                        // 结束搜索改回icon
                        setNavIcon(R.drawable.ic_home_used_in_search_bar)
                    }
                }

                override fun onSearchConfirmed(text: CharSequence?) {
                    // 确认搜索，展示搜索结果列表，替换fragment
                    "搜索内容：$text".showToast()
                }

                override fun onButtonClicked(buttonCode: Int) {
                    // 按钮的点击事件
                    when(buttonCode) {
                        MaterialSearchBar.BUTTON_NAVIGATION -> finish()
                        MaterialSearchBar.BUTTON_BACK -> mBinding.searchBar.closeSearch()
                    }
                }

            })

            addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // 空的时候退出搜索会被调用一次，需要屏蔽
                    if (s?.isBlank() == true) {
                        return
                    }
                    // 实时获取联想内容并显示
                    "获取搜索联想：$s".showToast()
                    adapter.suggestions = suggestions2
                    adapter.notifyDataSetChanged()
                    // adapter过滤信息
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }
    }

    private fun genSuggestions(testList: List<String>): MutableList<QueryResult> {
        val suggestions: MutableList<QueryResult> = ArrayList()
        for (s in testList) {
            val suggestion = QueryResult()
            suggestion.keyword = s
            suggestions.add(suggestion)
        }
        return suggestions
    }

    private fun MaterialSearchBar.setNavIcon(navIconRes: Int) {
        val navIconResId = this.javaClass.getDeclaredField("navIconResId")
        navIconResId.isAccessible = true
        navIconResId.set(this, navIconRes)

        val navIcon = this.javaClass.getDeclaredField("navIcon")
        navIcon.isAccessible = true
        val imageView = navIcon.get(this) as ImageView
        imageView.setImageResource(navIconRes)
    }

    private fun showSearchSuggestFragment() {
        mBinding.searchPageContainer
    }

    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }
}