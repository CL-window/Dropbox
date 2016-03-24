package cn.kejin.gitbook.controllers

import android.support.v4.widget.SwipeRefreshLayout

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/24
 */
class ExPageController(cb: PageCallback?) : PageController(cb)
{
    constructor(swipeRefreshLayout: SwipeRefreshLayout) : this(null)


}