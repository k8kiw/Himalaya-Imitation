package com.kotori.home.fragment

import kotlinx.coroutines.flow.collect

class HomeMusicFragment: BaseHomeFragment() {
    override suspend fun loadAlbumList() {
        mViewModel.musicPagingData.collect {
            pagingAdapter.submitData(it)
        }
    }
}