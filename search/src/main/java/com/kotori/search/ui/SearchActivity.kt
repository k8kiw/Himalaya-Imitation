package com.kotori.search.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.kotori.common.base.BaseActivity
import com.kotori.common.database.SearchHistory
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ktx.launchAndRepeatWithLifecycle
import com.kotori.common.network.RequestState
import com.kotori.common.sdk.ParcelableQueryResult
import com.kotori.common.support.Constants
import com.kotori.common.ui.showFailTipsDialog
import com.kotori.common.utils.LogUtil
import com.kotori.common.utils.showToast
import com.kotori.search.R
import com.kotori.search.adapter.AlbumSuggestionsAdapter
import com.kotori.search.databinding.ActivitySearchBinding
import com.kotori.search.ktx.doSearch
import com.kotori.search.ktx.setNavIcon
import com.kotori.search.viewmodel.SearchViewModel
import com.mancj.materialsearchbar.MaterialSearchBar
import com.qmuiteam.qmui.util.QMUIKeyboardHelper
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

@Route(path = Constants.PATH_SEARCH_PAGE)
class SearchActivity : BaseActivity<ActivitySearchBinding>(){

    private val mViewModel: SearchViewModel by viewModel()

    private lateinit var navController: NavController

    //private lateinit var adapter: AlbumSuggestionsAdapter
    private val isSearchResultNotVisible
        get() = navController.currentDestination?.id != R.id.searchResultFragment

    override fun getLayoutId(): Int = R.layout.activity_search

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {

        val navHost = supportFragmentManager.findFragmentById(R.id.search_page_container)
        navHost?.apply {
            navController = findNavController()
        }
        initSearchBar()
        loadData()
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

            // 设置联想内容adapter
            val layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val adapter = AlbumSuggestionsAdapter(layoutInflater)
            setCustomSuggestionAdapter(adapter)

            // 点击事件
            adapter.onItemClick = { _, text ->
                doSearch(text)
            }

            lastSuggestions = emptyList<ParcelableQueryResult>()


            // 设置监听器
            setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {
                    if (!enabled) {
                        // 结束搜索改回icon
                        setNavIcon(R.drawable.ic_home_used_in_search_bar)
                        // 切回去
                        navController.navigateUp()
                    }
                }

                override fun onSearchConfirmed(text: CharSequence?) {
                    if (text.isNullOrBlank()) {
                        return
                    }
                    // 确认搜索，展示搜索结果列表，替换fragment
                    mViewModel.setCurrentSearchKeyword(text.toString())
                    // 记录搜索
                    mViewModel.addHistory(text.toString())


                    //focusable = View.NOT_FOCUSABLE
                    QMUIKeyboardHelper.hideKeyboard(this@apply)
                    searchEditText?.clearFocus()

                    // 确认当前是否处于搜索页面，否则无需再navigate
                    if (isSearchResultNotVisible) {
                        navController.navigate(R.id.action_searchSuggestFragment_to_searchResultFragment)
                    }
                }

                override fun onButtonClicked(buttonCode: Int) {
                    // 按钮的点击事件
                    when(buttonCode) {
                        MaterialSearchBar.BUTTON_NAVIGATION -> finish()
                        MaterialSearchBar.BUTTON_BACK -> {
                            mBinding.searchBar.closeSearch()
                            // 清空联想词，避免重复一个词点击无效
                            mViewModel.setCurrentSearchKeyword("")
                            mViewModel.setCurrentSearchSuggest("")
                            // 执行收起动画
                            updateLastSuggestions(emptyList<ParcelableQueryResult>())
                            // 由于列表为空时，放动画不会清空联想列表，得手动
                            adapter.suggestions = emptyList()
                        }
                    }
                }

            })

            addTextChangeListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    s?.apply {
                        // 空的时候退出搜索会被调用一次，需要屏蔽
                        if (s.isBlank()) {
                            return
                        }
                        // 实时获取联想内容并显示
                        "获取搜索联想：$s".showToast()
                        // 设置给ViewModel，让它更新联想
                        mViewModel.setCurrentSearchSuggest(s.toString())
                    }
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
        }
    }

    private fun loadData() {
        launchAndRepeatWithLifecycle {
            mViewModel.currentSearchKeyword.collect {
                if (it.isNotBlank()) {
                    // 搜索词变化时显示在上面
                    mBinding.searchBar.doSearch(it)
                } else {
                    LogUtil.d("Test", "联想列表置空")
                    // mBinding.searchBar.updateLastSuggestions(emptyList<ParcelableQueryResult>())
                }
            }
        }

        launchAndRepeatWithLifecycle {
            mViewModel.searchSuggestList.collect {
                if (isSearchResultNotVisible) {
                    mBinding.searchBar.apply {
                        if (it[0].keyword.isBlank()) {
                            // 更新内容
                            updateLastSuggestions(emptyList<ParcelableQueryResult>())
                        } else {
                            updateLastSuggestions(it)
                        }
                    }
                }
            }
            /*mViewModel.searchSuggestLoadState.collect {
                when(it) {
                    is RequestState.Success -> {
                        val result = mViewModel.searchSuggestList.value ?: emptyList()
                        if (isSearchResultNotVisible) {
                            mBinding.searchBar.apply {
                                if (result[0].keyword.isBlank()) {
                                    // 更新内容
                                    updateLastSuggestions(emptyList<ParcelableQueryResult>())
                                } else {
                                    updateLastSuggestions(result)
                                }
                            }
                        }
                    }
                    is RequestState.Error -> {
                        showFailTipsDialog("加载错误，请稍后再试")
                        it.errorMessage.showToast()
                    }
                    is RequestState.Loading -> {

                    }
                    is RequestState.Empty -> {

                    }
                }
            }*/
        }
    }

    /**
     * 测试用成成模拟数据
     */
    private fun genSuggestions(testList: List<String>): MutableList<ParcelableQueryResult> {
        val suggestions: MutableList<ParcelableQueryResult> = ArrayList()
        for (s in testList) {
            val suggestion = ParcelableQueryResult()
            suggestion.keyword = s
            suggestions.add(suggestion)
        }
        return suggestions
    }



    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }
}