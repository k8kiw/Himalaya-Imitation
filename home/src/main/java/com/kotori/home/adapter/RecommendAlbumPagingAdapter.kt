package com.kotori.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.kotori.common.base.BasePagingAdapter
import com.kotori.common.utils.showToast
import com.kotori.home.R
import com.ximalaya.ting.android.opensdk.model.album.Album

class RecommendAlbumPagingAdapter : BasePagingAdapter<Album>(differCallback) {

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Album>() {
            override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
                return oldItem.equals(newItem)
            }

        }
    }

    /**
     * 返回item的布局文件
     */
    override fun getItemLayout(position: Int): Int = R.layout.item_album

    override fun onItemClick(data: Album?) {
        "打开 ${data?.albumTitle} 详情".showToast()
    }

    override fun bindData(helper: ItemHelper, data: Album?) {
        // 显示数据，对data先行判断是否为空
        data?.let {
            // 使用helper完成数据的显示
            helper.apply {
                // 加载封面
                loadImage(R.id.album_cover, it.coverUrlSmall)
                // 显示文字
                setText(R.id.album_title, it.albumTitle)
                setText(R.id.album_introduction, it.albumIntro)
                // 显示数据
                setText(R.id.album_views_num, it.playCount.toString())
                setText(R.id.album_subscribe_num, it.subscribeCount.toString())
            }
        }
    }

}