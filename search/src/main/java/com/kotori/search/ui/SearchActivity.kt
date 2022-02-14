package com.kotori.search.ui

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.search.R
import com.kotori.search.databinding.ActivitySearchBinding

@Route(path = Constants.PATH_SEARCH_PAGE)
class SearchActivity : BaseActivity<ActivitySearchBinding>(){

    override fun getLayoutId(): Int = R.layout.activity_search

    override fun initView(root: View) {
        // 调整TopBar，
        // 要么不要，要么就把搜索框加到TopBar里
    }




    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }
}