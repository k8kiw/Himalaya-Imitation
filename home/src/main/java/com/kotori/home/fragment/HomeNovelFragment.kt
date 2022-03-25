package com.kotori.home.fragment

import kotlinx.coroutines.flow.collect

class HomeNovelFragment : BaseHomeFragment() {
    override suspend fun loadAlbumList() {
        mViewModel.novelPagingData.collect {
            pagingAdapter.submitData(it)
        }
    }
}