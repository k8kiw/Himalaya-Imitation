package com.kotori.search.ui

import android.view.LayoutInflater
import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.entity.ProgressBean
import com.kotori.common.utils.showToast
import com.kotori.search.R
import com.kotori.search.databinding.FragmentSearchSuggestBinding
import com.kotori.search.viewmodel.SearchViewModel
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import org.koin.androidx.viewmodel.ext.android.sharedViewModel



class SearchSuggestFragment : BaseDbFragment<FragmentSearchSuggestBinding>() {

    private val mViewModel: SearchViewModel by sharedViewModel()

    override fun onLazyInit() {

    }

    override fun getLayoutId(): Int = R.layout.fragment_search_suggest

    override fun showTopBar(): Boolean = false

    override fun initView(root: View) {

        initHotWords()
        initSearchHistory()
    }

    private fun initHotWords() {
        // 获取热词
        val list = ArrayList<String>()
        for (i in 1..20) {
            val s = "词$i"
            list.add(s)
        }
        addHotWordsToFloatLayout(list)
    }

    /**
     * FloatLayout是个ViewGroup，没有专门的方法一键添加
     * 这里每个item添加QMUIRoundButton进去
     * @param list 需要添加进布局的内容项
     */
    private fun addHotWordsToFloatLayout(list: List<String>) {
        // wrap_content 的布局参数
        /*val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )*/
        // 遍历list，把每一项都做成Button添加进去
        list.forEach {
            // 通过xml来加载button，不然不走AttributeSet的构造，会丢失样式
            val floatLayoutItem = LayoutInflater.from(activity).inflate(
                R.layout.round_button_in_float_layout,
                null
            ) as QMUIRoundButton
            // 设置文字，样式已经在xml中设置完了
            floatLayoutItem.text = it
            /*floatLayoutItem.apply {
                val itemPadding = QMUIDisplayHelper.dp2px(activity, 4)
                setPadding(itemPadding)
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f)
                text = it
            }*/
            
            // 设置点击监听，点击后启动搜索
            floatLayoutItem.setOnClickListener {
                val word = floatLayoutItem.text.toString()
                "点击了热词：${word}".showToast()
                mViewModel.setCurrentSearchKeyword(word)
            }
            mBinding.searchHotWordsFloatLayout.addView(floatLayoutItem)
        }
    }


    private fun initSearchHistory() {
        // 获取历史记录
        val list = ArrayList<String>()
        for (i in 1..20) {
            val s = "记录$i"
            list.add(s)
        }
        addHistoryToFloat(list)
    }

    private fun addHistoryToFloat(list: List<String>) {
        // 遍历list，把每一项都做成Button添加进去
        list.forEach {
            // 通过xml来加载button，不然不走AttributeSet的构造，会丢失样式
            val floatLayoutItem = LayoutInflater.from(activity).inflate(
                R.layout.round_button_in_float_layout,
                null
            ) as QMUIRoundButton
            // 设置文字
            floatLayoutItem.text = it

            // 设置点击监听，点击后启动搜索
            floatLayoutItem.setOnClickListener {
                val word = floatLayoutItem.text.toString()
                "点击了热词：${word}".showToast()
                mViewModel.setCurrentSearchKeyword(word)
            }
            // 长按删除，历史记录里才需要
            floatLayoutItem.setOnLongClickListener {
                // 删除
                mBinding.searchHistoryFloatLayout.removeView(floatLayoutItem)
                // 屏蔽单击事件
                true
            }
            mBinding.searchHistoryFloatLayout.addView(floatLayoutItem)
        }
    }




    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }

}