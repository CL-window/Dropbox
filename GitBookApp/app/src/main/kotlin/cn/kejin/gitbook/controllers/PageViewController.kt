package cn.kejin.gitbook.controllers

import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import cn.kejin.gitbook.R
import cn.kejin.gitbook.views.ExRecyclerView

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/24
 */
class PageViewController(val refreshLayout: SwipeRefreshLayout,
                         val exListView : ExRecyclerView,
                         pageCallback : PageController.PageCallback)
{
    init {
        refreshLayout.setOnRefreshListener { pageCtrl.refresh() }
        exListView.loadMoreListener = object : ExRecyclerView.OnLoadMoreListener {
            override fun onLoadMore() {
                pageCtrl.loadMore()
            }
        }
    }

    private val pageCtrl: PageController
        get() = PageController(callback=callback)

    private val callback = object : PageController.PageCallback {
        override fun onLoading(page: Int) {
            if (page == 0 && !refreshLayout.isRefreshing) {
                refreshLayout.post({refreshLayout.isRefreshing = true })
            }

            pageCallback.onLoading(page)
        }
    }

    fun pageIndex() = pageCtrl.exploredPageIndex

    fun refresh() = pageCtrl.refresh()

    fun finish(result : PageController.Result) {
        if (pageCtrl.exploredPageIndex == 0) {
            refreshLayout.isRefreshing = false

            when (result) {
                PageController.Result.SUCCESS -> {
                    if (!exListView.containsFooter(footer))
                        exListView.addFooter(footer)
                    showLoading()
                }

                PageController.Result.NO_MORE -> {
                    if (!exListView.containsFooter(footer))
                        exListView.addFooter(footer)
                    showNoMore()
                }
            }
        }
        else {
            when (result) {
                PageController.Result.SUCCESS -> {
                    showLoading()
                    exListView.endLoadMore()
                }
                PageController.Result.NO_MORE -> showNoMore()
                PageController.Result.FAILED -> showReload()
            }
        }

        pageCtrl.finish(result)
    }

    /**
     * Footer control
     */
    private val footer = View.inflate(exListView.context, R.layout.layout_list_footer, null)

    private val loading by lazy { footer.findViewById(R.id.loading) }
    private val reload by lazy { footer.findViewById(R.id.reload) }
    private val noMore by lazy { footer.findViewById(R.id.noMore) }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
        reload.visibility = View.GONE
        noMore.visibility = View.GONE
    }

    private fun showReload() {
        loading.visibility = View.GONE
        reload.visibility = View.VISIBLE
        noMore.visibility = View.GONE

        reload.setOnClickListener({pageCtrl.reload()})
    }

    private fun showNoMore() {
        loading.visibility = View.GONE
        reload.visibility = View.GONE
        noMore.visibility = View.VISIBLE
    }
}