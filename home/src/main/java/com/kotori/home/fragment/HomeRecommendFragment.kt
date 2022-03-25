package com.kotori.home.fragment


import androidx.lifecycle.lifecycleScope
import com.kotori.common.ktx.launchAndRepeatWithViewLifecycle
import com.kotori.common.sdk.SDKCallbackExt
import com.kotori.common.utils.LogUtil
import kotlinx.coroutines.flow.collect


class HomeRecommendFragment : BaseHomeFragment(){

    override suspend fun loadAlbumList() {
        mViewModel.recommendPagingData.collect {
            pagingAdapter.submitData(it)
        }
        /*lifecycleScope.launchWhenStarted {
            val allCategoryList = SDKCallbackExt.getAllCategoryList()
            LogUtil.d(TAG, "category size -> ${allCategoryList.size}")
            allCategoryList.forEach {
                LogUtil.d(TAG, "id -> ${it.id}, category name -> ${it.categoryName}")
            }
        }*/
    }

}