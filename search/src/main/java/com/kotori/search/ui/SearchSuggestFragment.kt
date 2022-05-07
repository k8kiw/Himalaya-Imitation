package com.kotori.search.ui

import android.view.LayoutInflater
import android.view.View
import com.kotori.common.base.BaseDbFragment
import com.kotori.common.database.SearchHistory
import com.kotori.common.entity.ProgressBean
import com.kotori.common.ktx.launchAndRepeatWithViewLifecycle
import com.kotori.common.network.RequestState
import com.kotori.common.ui.showFailTipsDialog
import com.kotori.common.utils.showToast
import com.kotori.search.R
import com.kotori.search.databinding.FragmentSearchSuggestBinding
import com.kotori.search.viewmodel.SearchViewModel
import com.qmuiteam.qmui.widget.QMUIFloatLayout
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import kotlinx.coroutines.flow.collect
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
        launchAndRepeatWithViewLifecycle {
            /*mViewModel.hotWordList.collect {
                // 将热词显示在界面上
                addHotWordsToFloatLayout(it)
                //"获取热词".showToast()
            }*/
            mViewModel.hotWordLoadState.collect {
                when(it) {
                    is RequestState.Success -> {
                        // 由于list的类型擦除，加载成功那就用hotWordList
                        val list = mViewModel.hotWordList.value
                        addHotWordsToFloatLayout(list)
                    }
                    is RequestState.Error -> {
                        showFailTipsDialog("加载错误，请稍后再试")
                        it.errorMessage.showToast()
                    }
                    is RequestState.Loading -> {

                    }
                    is RequestState.Empty -> {

                    }
                }
            }
        }
    }

    private fun initSearchHistory() {
        /*// 获取历史记录
        val list = ArrayList<String>()
        for (i in 1..20) {
            val s = "记录$i"
            list.add(s)
        }
        addHistoryToFloat(list)*/

        /*launchAndRepeatWithViewLifecycle {
            mViewModel.searchHistoryList.collect {
                // 将历史记录显示在界面上
                mBinding.searchHistoryFloatLayout.setHistoryToFloat(it)
            }
        }*/
        mViewModel.searchHistoryList.observe(this) {
            mBinding.searchHistoryFloatLayout.setHistoryToFloat(it)
        }
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
            if(it.isBlank()) {
                return@forEach
            }
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

    // 为QMUIFloatLayout添加一个item数组
    private val historyItem: MutableList<QMUIRoundButton> = mutableListOf()
    private var QMUIFloatLayout.itemList: MutableList<QMUIRoundButton>
        set(value) {
            // 清空view
            removeList(historyItem)
            historyItem.clear()
            // 添加view
            historyItem.addAll(value)
            addList(value)
        }
        get()  = historyItem

    private fun QMUIFloatLayout.setHistoryToFloat(list: List<SearchHistory>) {
        // 将历史做成itemList
        itemList = genHistoryItemList(list)
    }

    private fun QMUIFloatLayout.addList(list: List<QMUIRoundButton>) {
        list.forEach {
            addView(it)
        }
    }

    private fun QMUIFloatLayout.removeList(list: List<QMUIRoundButton>) {
        list.forEach {
            removeView(it)
        }
    }


    private fun QMUIFloatLayout.genHistoryItemList(list: List<SearchHistory>): MutableList<QMUIRoundButton> {
        val result = mutableListOf<QMUIRoundButton>()
        // 遍历list，把每一项都做成Button
        list.forEach { searchHistory ->
            // 通过xml来加载button，不然不走AttributeSet的构造，会丢失样式
            val floatLayoutItem = LayoutInflater.from(activity).inflate(
                R.layout.round_button_in_float_layout,
                null
            ) as QMUIRoundButton
            // 设置文字
            floatLayoutItem.text = searchHistory.keyword
            // 设置tag为搜索，免得无法删除
            floatLayoutItem.tag = searchHistory.id

            // 设置点击监听，点击后启动搜索
            floatLayoutItem.setOnClickListener {
                val word = floatLayoutItem.text.toString()
                "点击了搜索记录：${word}".showToast()
                mViewModel.setCurrentSearchKeyword(word)
            }
            // 长按删除，历史记录里才需要
            floatLayoutItem.setOnLongClickListener {
                // 删除
                // removeView(floatLayoutItem)
                // 删除记录
                mViewModel.deleteHistory(searchHistory)
                // 屏蔽单击事件
                return@setOnLongClickListener true
            }
            result.add(floatLayoutItem)
        }
        return result
    }




    override fun showProgress(progress: ProgressBean) {

    }

    override fun hideProgress() {

    }

}