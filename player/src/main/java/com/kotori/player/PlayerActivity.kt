package com.kotori.player

import android.view.View
import androidx.core.content.ContextCompat
import coil.load
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.common.support.Constants.DEFAULT_LEFT_IMAGE
import com.kotori.common.ui.addDefaultCloseButton
import com.kotori.common.ui.addRightFunctionButton
import com.kotori.common.ui.setMarqueeEnable
import com.kotori.common.utils.showToast
import com.kotori.common.utils.trimAlbumTitle
import com.kotori.player.databinding.ActivityPlayerBinding
import com.ximalaya.ting.android.opensdk.model.track.Track

@Route(path = Constants.PATH_PLAYER_PAGE)
class PlayerActivity : BaseActivity<ActivityPlayerBinding>() {

    @JvmField
    @Autowired(name = "track")
    var currentTrack: Track? = null


    override fun getLayoutId(): Int = R.layout.activity_player



    override fun initView(root: View) {
        initData()
        initTopBar()
    }

    /**
     * 接收路由跳转传入的数据
     */
    private fun initData() {
        ARouter.getInstance().inject(this)

        """专辑名：${currentTrack?.album?.albumTitle}
            |位置：${currentTrack?.orderNum}
        """.trimMargin().showToast()

        // 加载信息
        mBinding.apply {
            // 封面
            playerAlbumCover.load(currentTrack?.coverUrlLarge)
            // 标题
            playerTrackTitle.text = currentTrack?.trackTitle
            playerTrackTitle.setMarqueeEnable()
        }
    }

    private fun initTopBar() {
        getTopBar()?.apply {
            // 设置界面信息
            setTitle(currentTrack?.album?.albumTitle?.trimAlbumTitle())
            // 返回键
            addDefaultCloseButton().setOnClickListener { finish() }
            // 功能键
            addRightFunctionButton(R.drawable.ic_more_horiz_24px_rounded).setOnClickListener {

            }
            // 设置背景颜色
            setBackgroundColor(ContextCompat.getColor(
                this@PlayerActivity,
                R.color.qmui_config_color_background
            ))
            // 去除分割线
            setBottomDividerAlpha(0)
        }
    }


    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }

}