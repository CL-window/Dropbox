package cn.kejin.gitbook.controllers

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/24
 */

/**
 * 控制页面的 index,
 */
class PageController(val callback: PageCallback)
{
    enum class Result {
        SUCCESS,
        FAILED,
        NO_MORE
    }

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

    /**
     * Refresh Page
     */
    fun refresh() : Boolean {
        if (isRequesting) {
            return false;
        }

        exploredPageIndex = 0
        callback.onLoading(exploredPageIndex)
        return true;
    }

    /**
     * Load One More Page
     */
    fun loadMore() : Boolean {
        if (isRequesting) {
            return false;
        }
        exploredPageIndex += 1
        callback.onLoading(exploredPageIndex)
        return true;
    }

    /**
     * Reload Current Page
     */
    fun reload() : Boolean {
        if (isRequesting) {
            return false
        }
        exploredPageIndex = exploredPageIndex
        callback.onLoading(exploredPageIndex)
        return true;
    }

    /**
     * Finish current request
     */
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
        fun onLoading(page : Int)
    }
}