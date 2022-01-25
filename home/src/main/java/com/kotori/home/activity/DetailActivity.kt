package com.kotori.home.activity

import android.view.View
import coil.load
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.common.utils.LogUtil
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
        initTopBar()
    }

    private fun initTopBar() {
        mBinding.apply {
            // 加载头图
            detailAlbumCover.load(album?.coverUrlLarge)
            // 设置标题
            collapsingTopbarLayout.title = album?.albumTitle
            // 增加返回键
            detailTopbar.addLeftImageButton(
                Constants.DEFAULT_LEFT_IMAGE,
                R.id.qmui_topbar_item_left_back
            )?.setOnClickListener {
                finish()
            }
            // TODO:增加订阅键，要后期根据状态来决定
            detailTopbar.addRightImageButton(
                R.drawable.ic_add_24px_rounded,
                R.id.topbar_right_about_button
            )?.setOnClickListener {
                "点击了订阅".showToast()
            }
        }

        // 监听滑动状态
        mBinding.collapsingTopbarLayout.apply {
            setScrimUpdateListener {
                LogUtil.d(TAG, "scrim = ${it.animatedValue}")
            }
            addOnOffsetUpdateListener { layout, offset, expandFraction ->
                LogUtil.d(TAG, "offset = $offset, expandFraction = $expandFraction")
                // TODO:top bar图片切换问题
                /*if (expandFraction == 1.0f) {
                    mBinding.detailAlbumCover.visibility = View.INVISIBLE
                } else {
                    mBinding.detailAlbumCover.visibility = View.VISIBLE
                }*/
            }
        }
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