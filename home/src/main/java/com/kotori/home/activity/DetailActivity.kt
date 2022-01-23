package com.kotori.home.activity

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.common.utils.showToast
import com.kotori.home.R
import com.kotori.home.databinding.ActivityDetailBinding
import com.kotori.home.viewmodel.HomeViewModel
import com.ximalaya.ting.android.opensdk.model.album.Album
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = Constants.PATH_ALBUM_DETAIL_PAGE)
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val mViewModel : HomeViewModel by viewModel()

    @JvmField
    @Autowired(name = "album")
    var album : Album? = null

    override fun getLayoutId(): Int = R.layout.activity_detail

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {

        loadData()
    }

    private fun loadData() {
        // 获取注入的专辑信息
        ARouter.getInstance().inject(this)

        // 取得数据
        "${album?.albumIntro}".showToast()
    }


    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }

}