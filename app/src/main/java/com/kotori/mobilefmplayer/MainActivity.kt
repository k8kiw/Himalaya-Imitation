package com.kotori.mobilefmplayer

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.kotori.common.base.BaseActivity
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ktx.setupWithNavController
import com.kotori.common.sdk.testSDKGetCategories
import com.kotori.common.support.Constants
import com.kotori.mobilefmplayer.databinding.ActivityMainBinding
import com.ximalaya.ting.android.opensdk.player.XmPlayerManager

@Route(path = Constants.PATH_MAIN)
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var currentNavController: LiveData<NavController>

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun initView(root: View) {
        setupBottomNavigationBar()
    }

    /**
     * activity只是个容器，不要显示topbar
     */
    override fun showTopBar(): Boolean = false

    /**
     * 设置底部导航栏
     */
    private fun setupBottomNavigationBar() {
        // nav列表，与menu中定义的内容与顺序应该一致
        val navGraphIds = listOf(
            R.navigation.navigation_home,
            R.navigation.navigation_local,
            R.navigation.navigation_personal
        )

        // 所有被导航的界面的控制器，不同模块间独立
        val controller = mBinding.bottomNavView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        controller.observe(this) { navController ->
            //setupActionBarWithNavController(navController)
            navController.addOnDestinationChangedListener { _, destination, _ ->
                run {
                    val id = destination.id
                    // 设置进入到播放页面时，隐藏导航栏
                    if (id == R.id.tempPlayerFragment) {
                        mBinding.bottomNavView.visibility = View.GONE
                    } else {
                        mBinding.bottomNavView.visibility = View.VISIBLE
                    }
                }
            }
        }

        currentNavController = controller
    }

    /**
     * 页面返回逻辑
     */
    override fun onSupportNavigateUp(): Boolean {
        return currentNavController.value?.navigateUp() ?: false
    }


    /**
     * ===================== 释放资源 =======================
     */
    var isReleased = false

    fun destroy() {
        if (isReleased) {
            return
        }
        XmPlayerManager.release()
        isReleased = true
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            destroy()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroy()
    }
    /**
     * ===================== 释放资源 =======================
     */


    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }
}