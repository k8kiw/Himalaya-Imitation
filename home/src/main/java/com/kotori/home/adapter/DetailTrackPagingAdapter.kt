package com.kotori.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kotori.common.base.BasePagingAdapter
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
        // TODO：打开播放页面
    }

    override fun bindData(helper: ItemHelper, data: Track?) {
        // 若不为空
        data?.let {
            // helper加载数据
            helper.apply {
                setText(R.id.track_title, data.trackTitle)
                setText(R.id.track_play_num, data.playCount.toString())
                setText(R.id.track_duration_num, data.duration.toString())
            }
        }
    }
}