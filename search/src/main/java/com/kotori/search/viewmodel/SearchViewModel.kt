package com.kotori.search.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchViewModel: ViewModel() {

    private val _currentSearchKeyword = MutableStateFlow("")

    val currentSearchKeyword = _currentSearchKeyword.asStateFlow()

    fun setCurrentSearchKeyword(keyword: String) {
        _currentSearchKeyword.value = keyword
    }
}