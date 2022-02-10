package com.kotori.common.support

import com.ximalaya.ting.android.opensdk.model.track.Track

/**
 * paging列表数据通过ARouter太难弄，直接设置个公共变量来取算了
 */
object PublicData {

    // 保存最新播放列表
    var trackList: List<Track> = ArrayList()
}