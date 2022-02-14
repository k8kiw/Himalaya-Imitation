package com.kotori.common.support

import com.kotori.common.R


/**
 * 保存全局使用的常量
 * 各个模块跳转、通信，所需要用到的路径信息
 */
object Constants {

    // 页面跳转路径
    const val PATH_MAIN = "/app/MainActivity"
    const val PATH_HOME_PAGE = "/home/HomeFragment"
    const val PATH_ALBUM_DETAIL_PAGE = "/home/DetailActivity"
    const val PATH_PLAYER_PAGE = "/player/PlayerActivity"
    const val PATH_SEARCH_PAGE = "/search/SearchActivity"

    // 页面带参数跳转的参数名
    const val KEY_TRACK = "track"
    const val KEY_TRACK_LIST = "trackList"

    // ARouter 传递自定义对象所要用的
    const val SERVICE_ALBUM = "/service/AlbumServiceImpl"
    const val SERVICE_TRACK = "/service/TrackServiceImpl"


    // 默认的返回键
    val DEFAULT_LEFT_IMAGE: Int = R.drawable.ic_arrow_back_24px_rounded

    // view pager 传递参数时的键值
    const val ARG_PAGER_ADAPTER = "view pager arguments"
}