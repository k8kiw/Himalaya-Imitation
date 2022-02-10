package com.kotori.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BasePagingAdapter
import com.kotori.common.support.Constants
import com.kotori.common.support.Constants.KEY_TRACK_LIST
import com.kotori.common.support.Constants.KEY_TRACK
import com.kotori.common.utils.formatTrackDuration
import com.kotori.common.utils.formatNum
import com.kotori.common.utils.trimAlbumTitle
import com.kotori.home.R
import com.ximalaya.ting.android.opensdk.model.track.Track

class DetailTrackPagingAdapter : BasePagingAdapter<Track>(differCallback){

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Track>() {
            override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem.dataId == newItem.dataId
            }

            override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
                return oldItem == newItem
            }


        }
    }

    override fun getItemLayout(position: Int): Int = R.layout.item_track

    override fun onItemClick(data: Track?) {
        ARouter.getInstance().build(Constants.PATH_PLAYER_PAGE)
            .withParcelable(KEY_TRACK, data)
            .navigation()
    }

    override fun bindData(helper: ItemHelper, data: Track?) {
        // 若不为空
        data?.let {
            // helper加载数据
            helper.apply {
                setText(R.id.list_order, (data.orderNum + 1).toString())
                setText(R.id.track_title, data.trackTitle.trimAlbumTitle())
                setText(R.id.track_play_num, data.playCount.toString().formatNum())
                setText(R.id.track_duration_num, data.duration.toString().formatTrackDuration())
            }
        }
    }
}