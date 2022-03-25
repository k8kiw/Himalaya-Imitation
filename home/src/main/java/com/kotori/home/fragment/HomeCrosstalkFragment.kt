package com.kotori.home.fragment

import kotlinx.coroutines.flow.collect

class HomeCrosstalkFragment: BaseHomeFragment() {
    override suspend fun loadAlbumList() {
        mViewModel.crosstalkPagingData.collect {
            pagingAdapter.submitData(it)
        }
    }
}