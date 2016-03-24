package cn.kejin.gitbook.views

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/22
 */
class ExRecyclerView(context: Context?, attrs: AttributeSet?, defStyle: Int) : RecyclerView(context, attrs, defStyle) {
    companion object {
        val TAG = "ExRecyclerView"

    }

    val headerViews: MutableList<View> = mutableListOf();
    val footerViews: MutableList<View> = mutableListOf();

    var wrapperAdapter: Adapter<ViewHolder>? = null;

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        super.setAdapter(AdapterWrapper())
    }

    fun addHeader(view: View) {
        if (!headerViews.contains(view)) {
            headerViews.add(view)
            setFullSpan(view)

            adapter?.notifyItemInserted(headerViews.size)
        }
    }

    fun removeHeader(view: View) {
        val index = headerViews.indexOf(view)
        if (index > 0) {
            headerViews.removeAt(index)

            adapter?.notifyItemRemoved(index)
        }
    }

    fun addFooter(view: View) {
        if (!footerViews.contains(view)) {
            footerViews.add(view)
            setFullSpan(view)

            adapter?.notifyItemInserted(adapter.itemCount)
        }
    }

    fun removeFooter(view: View) {
        val index = footerViews.indexOf(view)
        if (index > 0) {
            footerViews.removeAt(index)

            adapter?.notifyItemRemoved(headerViews.size + (wrapperAdapter?.itemCount ?: 0) + index)
        }
    }

    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        wrapperAdapter?.unregisterAdapterDataObserver(adapterDataObserver)
        wrapperAdapter = adapter
        wrapperAdapter?.registerAdapterDataObserver(adapterDataObserver)
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
                return oldLookup.getSpanSize(position - headerViews.size)
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
        return (pos < headerViews.size || pos >= headerViews.size + (wrapperAdapter?.itemCount ?: 0))
    }

    inner class AdapterWrapper : Adapter<ViewHolder>() {
        override fun getItemViewType(position: Int): Int {
            if (isHeaderOrFooterPos(position)) {
                return -position - 1;
            }

            return wrapperAdapter?.getItemViewType(position) ?: super.getItemViewType(position)
        }

        override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
            if (!isHeaderOrFooterPos(position)) {
                wrapperAdapter?.onBindViewHolder(holder, position - headerViews.size)
            }
        }

        override fun getItemCount(): Int {
            return headerViews.size + footerViews.size + (wrapperAdapter?.itemCount ?: 0)
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
            if (viewType < 0) {
                var pos = -(viewType + 1)
                if (pos >= 0 && pos < headerViews.size) {
                    return WrapperViewHolder(headerViews[pos])
                }

                pos -= headerViews.size + (wrapperAdapter?.itemCount ?: 0)
                if (pos >= 0 && pos < footerViews.size) {
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
            adapter.notifyItemMoved(headerViews.size + fromPosition, headerViews.size + toPosition)
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeInserted(headerViews.size + positionStart, itemCount)
            adapter.notifyItemRangeChanged(positionStart + headerViews.size + itemCount,
                    adapter.itemCount - (headerViews.size + positionStart + itemCount) - footerViews.size)
        }

        override fun onChanged() {
            adapter.notifyDataSetChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeRemoved(headerViews.size + positionStart, itemCount)
            adapter.notifyItemRangeChanged(headerViews.size + positionStart,
                    adapter.itemCount - headerViews.size - positionStart - footerViews.size)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            adapter.notifyItemRangeChanged(headerViews.size + positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(headerViews.size + positionStart, itemCount, payload)
        }
    }

    /**
     * get first visible and last visible item position
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

    var loadMoreEnable = false
    var loadMoreListener : OnLoadMoreListener? = null

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        val wrapperSize = wrapperAdapter?.itemCount?:0
        if (!isLoadingMore &&
                loadMoreEnable && wrapperSize > 0) {
            val visPos = getVisiblePos()
            if (visPos.second >= headerViews.size+wrapperSize) {
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