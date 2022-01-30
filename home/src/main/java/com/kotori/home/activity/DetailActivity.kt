package com.kotori.home.activity

import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import coil.load
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.common.ui.showFailTipsDialog
import com.kotori.common.utils.LogUtil
import com.kotori.common.utils.showToast
import com.kotori.common.utils.trimAlbumTitle
import com.kotori.home.R
import com.kotori.home.adapter.DetailTrackPagingAdapter
import com.kotori.home.databinding.ActivityDetailBinding
import com.kotori.home.viewmodel.HomeViewModel
import com.ximalaya.ting.android.opensdk.model.album.Album
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = Constants.PATH_ALBUM_DETAIL_PAGE)
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    private val mViewModel : HomeViewModel by viewModel()

    private val detailTrackPagingAdapter = DetailTrackPagingAdapter()

    @JvmField
    @Autowired(name = "album")
    var album : Album? = null

    override fun getLayoutId(): Int = R.layout.activity_detail

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {

        loadData()
        initTopBar()
        initRecyclerView()
        initListener()
    }

    private fun initListener() {
        // 刷新
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            detailTrackPagingAdapter.refresh()
        }

        // 监听加载状态
        lifecycleScope.launchWhenCreated {
            detailTrackPagingAdapter.loadStateFlow.collectLatest {
                // 监听加载状态
                when(it.refresh) {
                    is LoadState.Loading -> mBinding.swipeRefreshLayout.isRefreshing = true
                    is LoadState.NotLoading -> mBinding.swipeRefreshLayout.isRefreshing = false
                    is LoadState.Error -> {
                        // 隐藏刷新，不然一直在转回不去
                        mBinding.swipeRefreshLayout.isRefreshing = false
                        // 网络提示框
                        showFailTipsDialog("加载错误，请稍后再试")
                        // refresh 是字段无法被自动强转，只能自己转
                        val state = it.refresh as LoadState.Error
                        "${state.error.message}".showToast()
                    }
                }

            }
        }
    }

    private fun initRecyclerView() {
        mBinding.detailTrackList.adapter = detailTrackPagingAdapter
        mBinding.swipeRefreshLayout.setColorSchemeResources(R.color.qmui_config_color_blue)

        // 添加divider
        mBinding.detailTrackList.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )
    }

    private fun initTopBar() {
        mBinding.apply {
            // 加载头图
            detailAlbumCover.load(album?.coverUrlLarge)
            // 设置标题
            collapsingTopbarLayout.title = album?.albumTitle?.trimAlbumTitle()
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
            setContentScrimColor(ContextCompat.getColor(
                this@DetailActivity,
                R.color.qmui_config_color_blue
            ))
            setStatusBarScrimColor(ContextCompat.getColor(
                this@DetailActivity,
                R.color.qmui_config_color_blue
            ))
            setScrimUpdateListener {
                // scrim 到达阈值，切换颜色
                LogUtil.d(TAG, "scrim = ${it.animatedValue}")
            }
            addOnOffsetUpdateListener { layout, offset, expandFraction ->
                LogUtil.d(TAG, "offset = $offset, expandFraction = $expandFraction")

            }
        }
    }

    private fun loadData() {
        // 获取注入的专辑信息
        ARouter.getInstance().inject(this)

        // 取得数据
        // "${album?.albumIntro}".showToast()

        // 监听
        lifecycleScope.launchWhenCreated {
            album?.let { albumNotNull ->
                mViewModel.getTracksByAlbum(albumNotNull).collect {
                    detailTrackPagingAdapter.submitData(it)
                }
            }
        }
    }


    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }

}