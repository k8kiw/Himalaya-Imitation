package com.kotori.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.support.Constants
import com.kotori.common.utils.showToast
import com.kotori.player.databinding.ActivityPlayerContainerBinding

@Route(path = Constants.PATH_PLAYER_PAGE)
class PlayerContainerActivity : BaseActivity<ActivityPlayerContainerBinding>() {

    @JvmField
    @Autowired(name = "test")
    var test: String? = null

    override fun getLayoutId(): Int = R.layout.activity_player_container

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {
        ARouter.getInstance().inject(this)

        "${supportFragmentManager.fragments[0]}".showToast()
    }

    override fun showProgress(progress: ProgressBean) {
        TODO("Not yet implemented")
    }

    override fun hideProgress() {
        TODO("Not yet implemented")
    }

}