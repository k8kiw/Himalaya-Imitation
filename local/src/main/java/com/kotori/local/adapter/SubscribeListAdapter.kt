package com.kotori.local.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.alibaba.android.arouter.launcher.ARouter
import com.kotori.common.support.Constants
import com.kotori.common.utils.formatNum
import com.kotori.common.utils.trimAlbumTitle
import com.kotori.local.R
import com.ximalaya.ting.android.opensdk.model.album.Album

class SubscribeListAdapter(
    var list: List<Album>
): RecyclerView.Adapter<SubscribeListAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val cover: ImageView = view.findViewById(R.id.album_cover)
        private val title: TextView = view.findViewById(R.id.album_title)
        private val introduction: TextView = view.findViewById(R.id.album_introduction)
        private val playNum: TextView = view.findViewById(R.id.album_play_num)
        private val subscribeNum: TextView = view.findViewById(R.id.album_subscribe_num)

        fun bindViewHolder(position: Int) {
            val album = list[position]
            album.apply {
                cover.load(coverUrlSmall)
                title.text = albumTitle.trimAlbumTitle()
                introduction.text = albumIntro
                playNum.text = playCount.toString().formatNum()
                subscribeNum.text = subscribeCount.toString().formatNum()
            }
        }
    }

    /**
     * item点击事件
     */
    var onClick: (View) -> Unit = {
        ARouter.getInstance()
            .build(Constants.PATH_ALBUM_DETAIL_PAGE)
            //.withParcelable("album", data)
            .navigation()
    }

    /**
     * 解析xml，创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album, parent, false)

        val viewHolder = ViewHolder(root)
        // 点击跳转事件
        viewHolder.itemView.setOnClickListener {
            ARouter.getInstance()
                .build(Constants.PATH_ALBUM_DETAIL_PAGE)
                .withParcelable("album", list[viewHolder.layoutPosition])
                .navigation()
        }
        return viewHolder
    }

    /**
     * 控制ViewHolder填充内容
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}