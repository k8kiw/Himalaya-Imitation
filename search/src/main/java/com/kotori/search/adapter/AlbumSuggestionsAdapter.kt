package com.kotori.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotori.common.utils.showToast
import com.kotori.search.R
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
import com.ximalaya.ting.android.opensdk.model.word.QueryResult


class AlbumSuggestionsAdapter constructor(
    private val inflater: LayoutInflater
): SuggestionsAdapter<QueryResult, AlbumSuggestionsAdapter.SuggestionHolder>(inflater) {

    override fun getSingleViewHeight(): Int = 50


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionHolder {
        val rootView = inflater.inflate(R.layout.item_suggestions, parent, false)
        return SuggestionHolder(rootView)
    }

    override fun onBindSuggestionHolder(
        suggestion: QueryResult?,
        holder: SuggestionHolder?,
        position: Int
    ) {
        holder?.apply {
            // 显示联想
            title.text = suggestion?.keyword
            // 点击事件
            itemView.setOnClickListener {
                "点击了：${title.text}".showToast()
                // 将title加入搜索框，如果无法加入那就在外部写listener
            }
        }
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
                suggestions = results?.values as ArrayList<QueryResult>
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