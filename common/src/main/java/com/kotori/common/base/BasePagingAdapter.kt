package com.kotori.common.base

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.kotori.common.R


/**
 * PagingAdapter的基类提取
 * 当只有一类数据时，只需编写点击事件和具体控件的数据显示逻辑
 * @param T 一个item的数据类型
 */
abstract class BasePagingAdapter<T : Any>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        private const val TAG = "BasePagingAdapter"
    }

    /**
     * 设置item如何显示数据
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) ?: return
        (holder as BasePagingAdapter<*>.BaseViewHolder).bindNormalData(item)
    }

    /**
     * 为item设置点击事件
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val holder = BaseViewHolder(parent, viewType)
        //Item的点击事件
        holder.itemView.setOnClickListener {
            onItemClick(getItem(holder.layoutPosition))
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return getItemLayout(position)
    }

    /**
     * 子类获取layout，创建view holder时需要用来找控件
     */
    protected abstract fun getItemLayout(position: Int): Int

    /**
     * itemView的点击事件，子类实现
     * 在createViewHolder中设置点击监听器时
     * 该方法会在item的根布局上间接调用
     */
    protected abstract fun onItemClick(data: T?)

    /**
     * 子类绑定数据，会在ViewHolder中的绑定数据函数中间接调用
     * 每一个item的数据显示最终都会调用到这里
     */
    protected abstract fun bindData(helper: ItemHelper, data: T?)


    /**
     * 使用一个helper类(也即委托，他来干活的)一并完成ViewHolder，这样子类就无需自行实现
     */
    inner class BaseViewHolder(parent: ViewGroup, layout: Int) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(layout, parent, false)
    ) {
        private val helper: ItemHelper = ItemHelper(this)

        fun bindNormalData(item: Any?) {
            bindData(helper, item as T)
        }
    }


    /**
     * ItemView的辅助类，帮助view holder实现view的缓存以及数据显示
     */
    class ItemHelper(holder: BasePagingAdapter<*>.BaseViewHolder) {
        private val itemView = holder.itemView
        private val viewCache = SparseArray<View>()

        private fun findViewById(viewId: Int): View {
            var view = viewCache.get(viewId)
            if (view == null) {
                view = itemView.findViewById(viewId)
                if (view == null) {
                    throw NullPointerException("$viewId can not find this item！")
                }
                viewCache.put(viewId, view)
            }
            return view
        }

        /**
         * TextView设置内容
         */
        fun setText(viewId: Int, text: CharSequence?): ItemHelper {
            (findViewById(viewId) as TextView).text = text
            return this
        }

        /**
         * Coil加载图片
         */
        fun loadImage(viewId: Int, url: String) {
            val imageView: ImageView = findViewById(viewId) as ImageView
            imageView.load(url) {
                placeholder(R.mipmap.img_placeholder)
            }

        }
    }


}