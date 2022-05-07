package com.kotori.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kotori.common.database.SearchHistory
import com.kotori.common.network.RequestState
import com.kotori.common.sdk.ParcelableQueryResult
import com.kotori.common.sdk.SDKCallbackExt
import com.kotori.common.utils.LogUtil
import com.kotori.common.utils.showToast
import com.kotori.search.repository.SearchRepository
import com.kotori.search.repository.SearchResultPagingSource
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class SearchViewModel(private val repository: SearchRepository): ViewModel() {

    /*init {
        getHotWords()
        //getHistories()
    }*/

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
        "搜索新词：$keyword".showToast()
        Pager(PagingConfig(pageSize = 20)) {
            SearchResultPagingSource(keyword)
        }.flow
            .onStart { emit(PagingData.empty()) }
            .cachedIn(viewModelScope)
    }

    /**
     * 需要实时联想的词
     */
    //TODO: 重复点击同一个词不会被设置，退出搜索模式后应该将它设为空
    private val _currentSearchSuggest = MutableStateFlow("")

    fun setCurrentSearchSuggest(suggest: String) {
        _currentSearchSuggest.value = suggest
    }

    val searchSuggestLoadState = MutableStateFlow<RequestState>(RequestState.Empty)

    val searchSuggestList = _currentSearchSuggest.flatMapLatest { suggest ->
        LogUtil.d("Test", "请求${suggest}的联想")
        flow {
            if (suggest.isBlank()) {
                val parcelableQueryResult = ParcelableQueryResult()
                parcelableQueryResult.keyword = ""
                emit(listOf(parcelableQueryResult))
            } else {
                //TODO: 使用通用请求
                /*repository.executeReq(searchSuggestLoadState) {
                    LogUtil.d("Test", "执行请求")
                    SDKCallbackExt.getSuggestWord(suggest)
                        .also { emit(it) }
                }*/
                val result = SDKCallbackExt.getSuggestWord(suggest)
                emit(result)
            }
        }
    }//.asLiveData()


    // 执行搜索的接口
    fun getSearchResult() : Flow<PagingData<Album>> {
        // 被实际调用时再执行搜索
        return repository.getSearchResultPagingData(_currentSearchKeyword.value)
            .cachedIn(viewModelScope)
    }

    /**
     * 热搜词
     */
    private val _hotWordList = MutableStateFlow(listOf(""))

    val hotWordList = _hotWordList.asStateFlow()

    /**
     * 热搜词的请求状态
     * 由于进入到该界面就需要请求了，所以在初始化完成后自动调用
     */
    val hotWordLoadState = MutableStateFlow<RequestState>(RequestState.Empty)
        .also { getHotWords() }

    private fun getHotWords() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.executeReq(hotWordLoadState) {
                SDKCallbackExt.getHotWords()
                    .map { it.searchword }
                    .also { _hotWordList.value = it }
            }
            /*val result = SDKCallbackExt.getHotWords()
            _hotWordList.value = result.map { it.searchword }*/
        }
    }


    /**
     * 搜索历史纪录
     */
    private val _searchHistoryList = MutableStateFlow(emptyList<SearchHistory>())

    val searchHistoryList = repository.getAllHistories().flatMapLatest { historyList ->
        flow {
            emit(historyList.take(10))
        }
        // 注意，三个参数的stateIn对标asLiveData，但是好像不起作用
    }.asLiveData()
        //.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    //val searchHistoryList = _searchHistoryList.asStateFlow()

    private fun getHistories() {
        viewModelScope.launch {
            // collect数据库来的数据，取前十
            repository.getAllHistories().collect {
                // 转换为 StateFlow 的 UIState
                _searchHistoryList.value = it.take(10)
            }
        }
    }

    /**
     * 添加历史记录，把bean类和时间封装好，再暴露出去
     */
    fun addHistory(keyword: String) {
        viewModelScope.launch {
            repository.insertHistory(SearchHistory(
                keyword = keyword,
                time = Date()
            ))
        }
    }

    fun deleteHistory(history: SearchHistory) {
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }
}