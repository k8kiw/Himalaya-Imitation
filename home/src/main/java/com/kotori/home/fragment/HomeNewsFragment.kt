package com.kotori.home.fragment

import kotlinx.coroutines.flow.collect

class HomeNewsFragment: BaseHomeFragment() {
    override suspend fun loadAlbumList() {
        mViewModel.newsPagingData.collect {
            pagingAdapter.submitData(it)
        }
    }
}