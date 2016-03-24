package cn.kejin.gitbook.controllers

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/24
 */
open class PageController
{
    constructor():this(null)
    constructor(cb: PageCallback?) {
        callback = cb;
    }

    enum class Result {
        SUCCESS,
        FAILED,
        NO_MORE
    }

    var callback : PageCallback? = null;

    var isRequesting = false;
        private set(value) {
            field = value
        }

    var exploredPageIndex = 0
        private set(value) {
            if (isRequesting) {
                return;
            }
            isRequesting = true
            field = value
        }


    fun refresh() : Boolean {
        if (isRequesting) {
            return false;
        }

        exploredPageIndex = 0
        callback?.onRefresh(exploredPageIndex)
        return true;
    }

    fun loadMore() : Boolean {
        if (isRequesting) {
            return false;
        }
        exploredPageIndex += 1
        callback?.onLoadMore(exploredPageIndex)
        return true;
    }

    fun reload() : Boolean {
        if (isRequesting) {
            return false
        }
        exploredPageIndex = exploredPageIndex
        if (exploredPageIndex == 0) {
            callback?.onRefresh(exploredPageIndex)
        }
        else {
            callback?.onLoadMore(exploredPageIndex)
        }
        return true;
    }

    fun finish(res: Result) {
        when (res) {
            Result.FAILED,
            Result.NO_MORE -> {
                if (exploredPageIndex > 0) {
                    exploredPageIndex -= 1
                }
            }

            else -> {
                //
            }
        }

        isRequesting = false
    }

    interface PageCallback {
        fun onRefresh(page : Int)
        fun onLoadMore(page : Int)
    }
}