package com.kotori.search.adapter

import androidx.recyclerview.widget.DiffUtil
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.base.BasePagingAdapter
import com.kotori.common.support.Constants
import com.kotori.common.utils.formatNum
import com.kotori.common.utils.trimAlbumTitle
import com.kotori.search.R
import com.ximalaya.ting.android.opensdk.model.album.Album

class SearchResultPagingAdapter : BasePagingAdapter<Album>(differCallback) {

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                // return oldItem == newItem
                // bean 未实现equals，没办法快速比较
                return oldItem.albumTitle == newItem.albumTitle &&
                        oldItem.albumIntro == newItem.albumIntro &&
                        oldItem.playCount == newItem.playCount &&
                        oldItem.subscribeCount == newItem.subscribeCount
            }

        }
    }

    override fun getItemLayout(position: Int): Int = R.layout.item_album

    override fun onItemClick(data: Album?) {
        // 打开详情页面
        ARouter.getInstance().build(Constants.PATH_ALBUM_DETAIL_PAGE)
            .withParcelable("album", data)
            .navigation()
    }

    override fun bindData(helper: ItemHelper, data: Album?) {
        // 控制数据显示
        data?.let {
            helper.apply {
                // 加载封面
                loadImage(R.id.album_cover, it.coverUrlSmall)
                // 显示文字
                setText(R.id.album_title, it.albumTitle.trimAlbumTitle())
                setText(R.id.album_introduction, it.albumIntro)
                // 显示数据
                setText(R.id.album_play_num, it.playCount.toString().formatNum())
                setText(R.id.album_subscribe_num, it.subscribeCount.toString().formatNum())
            }
        }
    }

}