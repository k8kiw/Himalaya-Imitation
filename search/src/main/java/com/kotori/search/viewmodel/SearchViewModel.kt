package com.kotori.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kotori.common.sdk.SDKCallbackExt
import com.kotori.search.repository.SearchRepository
import com.kotori.search.repository.SearchResultPagingSource
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {

    /**
     * 搜索框传进来的搜索关键字
     */
    private val _currentSearchKeyword = MutableStateFlow("")

    val currentSearchKeyword = _currentSearchKeyword.asStateFlow()

    fun setCurrentSearchKeyword(keyword: String) {
        _currentSearchKeyword.value = keyword
    }

    // 当搜索词变化时，发送新的Flow，替换掉原paging data
    val searchResultList = _currentSearchKeyword.flatMapLatest { keyword ->
        Pager(PagingConfig(pageSize = 20)) {
            SearchResultPagingSource(keyword)
        }.flow
            .onStart { emit(PagingData.empty()) }
            .cachedIn(viewModelScope)
    }

    // 执行搜索的接口
    fun getSearchResult() : Flow<PagingData<Album>> {
        // 被实际调用时再执行搜索
        return SearchRepository.getSearchResultPagingData(_currentSearchKeyword.value)
            .cachedIn(viewModelScope)
    }

    /**
     * 热搜词
     */
    private val _hotWordList = MutableStateFlow(listOf(""))

    val hotWordList = _hotWordList.asStateFlow()

    init {
        getHotWords()
    }

    private fun getHotWords() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = SDKCallbackExt.getHotWords()
            _hotWordList.value = result.map { it.searchword }
        }
    }
}