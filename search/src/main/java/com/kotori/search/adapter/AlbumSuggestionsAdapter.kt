package com.kotori.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotori.common.sdk.ParcelableQueryResult
import com.kotori.common.utils.showToast
import com.kotori.search.R
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter


class AlbumSuggestionsAdapter constructor(
    private val inflater: LayoutInflater
): SuggestionsAdapter<ParcelableQueryResult, AlbumSuggestionsAdapter.SuggestionHolder>(inflater) {

    override fun getSingleViewHeight(): Int = 50


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        val rootView = inflater.inflate(R.layout.item_suggestions, parent, false)
        return SuggestionHolder(rootView)
    }

    override fun onBindSuggestionHolder(
        suggestion: ParcelableQueryResult?,
        holder: SuggestionHolder?,
        position: Int
    ) {
        holder?.apply {
            // 显示联想
            title.text = suggestion?.keyword
            // 点击事件
            itemView.setOnClickListener {
                onItemClick(position, title.text.toString())
            }
        }
    }

    /**
     * 联想项点击事件，入参为 position 和 text
     */
    var onItemClick: (Int, String) -> Unit = { position, text ->
        "点击了第${position}项，标题为：${text}".showToast()
    }

    /**
     * 过滤器
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val term = constraint.toString()
                if (term.isEmpty()) {
                    suggestions = suggestions_clone
                } else {
                    suggestions = ArrayList()
                    for (item in suggestions_clone) {
                        if (item.keyword.lowercase().contains(term.lowercase())) {
                            suggestions.add(item)
                        }
                    }
                }
                results.values = suggestions
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                suggestions = results?.values as ArrayList<ParcelableQueryResult>
                notifyDataSetChanged()
            }

        }
    }

    inner class SuggestionHolder constructor(
        rootView: View
    ): RecyclerView.ViewHolder(rootView){
        val title: TextView = rootView.findViewById(R.id.search_suggestion_title)
    }
}