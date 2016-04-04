package cn.kejin.gitbook.views

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import cn.kejin.gitbook.R

import kotlin.collections.mutableListOf

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/22
 */
class ExRecyclerView: RecyclerView {
    companion object {
        val TAG = "ExRecyclerView"

    }

    private val headerViews: MutableList<View> = mutableListOf()

    private val footerViews: MutableList<View> = mutableListOf()

    var wrapperAdapter: Adapter<ViewHolder>? = null;

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {

        super.setAdapter(AdapterWrapper())
        if (context != null && attrs != null) {
            val attr = context.obtainStyledAttributes(attrs, R.styleable.ExRecyclerView, defStyle, 0)
            /**
             * TODO: realize add header and footer in layout,  and can get header view in code
             */
        }
    }

    init {
        super.setAdapter(AdapterWrapper())
    }

    fun containsHeader(view: View) : Boolean = headerViews.contains(view)

    fun addHeader(view: View) {
        headerViews.add(view)
        setFullSpan(view)

        adapter?.notifyItemInserted(getHeaderSize())
    }

    fun removeHeader(view: View) {
        val index = headerViews.indexOf(view)
        if (index > 0) {
            headerViews.removeAt(index)

            adapter?.notifyItemRemoved(index)
        }
    }

    fun containsFooter(view : View) : Boolean = footerViews.contains(view)
    fun addFooter(view: View) {
        footerViews.add(view)
        setFullSpan(view)

        adapter?.notifyItemInserted(adapter.itemCount)
    }

    fun removeFooter(view: View) {
        val index = footerViews.indexOf(view)
        if (index > 0) {
            footerViews.removeAt(index)

            adapter?.notifyItemRemoved(getHeaderSize() + (wrapperAdapter?.itemCount ?: 0) + index)
        }
    }
    
    fun getHeaderSize() = headerViews.size
    fun getFooterSize() = footerViews.size
    fun getItemCount() = {
        getHeaderSize() + getFooterSize() + getWrapItemCount()
    }
    fun getWrapItemCount() = wrapperAdapter?.itemCount?:0


    override fun setAdapter(wrapAdapter: Adapter<ViewHolder>?) {
        wrapperAdapter?.unregisterAdapterDataObserver(adapterDataObserver)
        wrapperAdapter = wrapAdapter
        wrapperAdapter?.registerAdapterDataObserver(adapterDataObserver)

        super.setAdapter(adapter)
    }

    override fun setLayoutManager(layout: LayoutManager?) {
        super.setLayoutManager(layout)

        if (layout != null && layout is GridLayoutManager) {
            configGridLayoutManager(layout)
        }

        headerViews.forEach { setFullSpan(it) }
        footerViews.forEach { setFullSpan(it) }
    }

    private fun configGridLayoutManager(layoutManager: GridLayoutManager) {
        val oldLookup = layoutManager.spanSizeLookup;
        val spanCount = layoutManager.spanCount;
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (isHeaderOrFooterPos(position)) {
                    return spanCount
                }
                return oldLookup.getSpanSize(position - getHeaderSize())
            }
        }
    }

    /**
     * 设置 LayoutParams, 如果不设置, 在 LinearLayoutManager 时有时会出现不能填满宽度的情况
     */
    private fun setFullSpan(view: View?) {
        if (view == null) {
            return
        }

        if (layoutManager is StaggeredGridLayoutManager) {
            val layoutParams = StaggeredGridLayoutManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.isFullSpan = true
            view.layoutParams = layoutParams
        }
        else {
            val layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            view.layoutParams = layoutParams
        }
    }

    private fun isHeaderOrFooterPos(pos: Int): Boolean {
        return (pos < getHeaderSize() || pos >= getHeaderSize() + (wrapperAdapter?.itemCount ?: 0))
    }

    inner class AdapterWrapper : Adapter<ViewHolder>() {
        override fun getItemViewType(position: Int): Int {
            if (isHeaderOrFooterPos(position)) {
                return -position - 1;
            }

            val pos = position - getHeaderSize()
            return wrapperAdapter?.getItemViewType(pos) ?: 0;
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            if (!isHeaderOrFooterPos(position)) {
                wrapperAdapter?.onBindViewHolder(holder, position - getHeaderSize())
            }
        }

        override fun getItemCount(): Int {
            return getHeaderSize() + getFooterSize() + getWrapItemCount()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
            if (viewType < 0) {
                var pos = -(viewType + 1)
                if (pos >= 0 && pos < getHeaderSize()) {
                    return WrapperViewHolder(headerViews[pos])
                }

                pos -= getHeaderSize() + getWrapItemCount()
                if (pos >= 0 && pos < getFooterSize()) {
                    return WrapperViewHolder(footerViews[pos])
                }
                return null;
            }

            return wrapperAdapter?.onCreateViewHolder(parent, viewType)
        }

        inner class WrapperViewHolder(itemView: View?) : ViewHolder(itemView)
    }

    /**
     * Data observer
     */
    private val adapterDataObserver = object : AdapterDataObserver() {
        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            // TODO: support move
            adapter.notifyItemMoved(getHeaderSize() + fromPosition, getHeaderSize() + toPosition)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeInserted(getHeaderSize() + positionStart, itemCount)
            adapter.notifyItemRangeChanged(positionStart + getHeaderSize() + itemCount,
                    adapter.itemCount - (getHeaderSize() + positionStart + itemCount) - getFooterSize())
        }

        override fun onChanged() {
            adapter.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeRemoved(getHeaderSize() + positionStart, itemCount)
            adapter.notifyItemRangeChanged(getHeaderSize() + positionStart,
                    adapter.itemCount - getHeaderSize() - positionStart - getFooterSize())
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeChanged(getHeaderSize() + positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(getHeaderSize() + positionStart, itemCount, payload)
        }
    }

    /**
     * get first and last visible item position
     */
    private fun getVisiblePos() : Pair<Int, Int> {
        if (layoutManager == null) {
            return Pair(0, 0)
        }

        var first = 0
        var last = 0
        when (layoutManager) {
            is GridLayoutManager -> {
                first = (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
                last = (layoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }

            is LinearLayoutManager -> {
                first = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition();
                last = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }

            is StaggeredGridLayoutManager -> {
                var firstPos : IntArray = IntArray(2, {0});
                (layoutManager as StaggeredGridLayoutManager).findFirstVisibleItemPositions(firstPos)

                var lastPos : IntArray = IntArray(2, {0});
                (layoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(lastPos)

                first = firstPos.min()?:0
                last = lastPos.max()?:0
            }
        }

        return Pair(first, last)
    }

    var isLoadingMore = false
        private set

    var loadMoreListener : OnLoadMoreListener? = null

    fun setOnLoadMoreListener(r : ()->Any) {
        loadMoreListener = object : OnLoadMoreListener {
            override fun onLoadMore() {
                r()
            }
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        val wrapperSize = wrapperAdapter?.itemCount?:0
        if (!isLoadingMore && loadMoreListener != null && wrapperSize > 0) {
            val visPos = getVisiblePos()
            if (visPos.second+1 >= getHeaderSize()+wrapperSize) {
                isLoadingMore = true
                loadMoreListener?.onLoadMore()
            }
        }
    }

    fun endLoadMore() {
        isLoadingMore = false
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}