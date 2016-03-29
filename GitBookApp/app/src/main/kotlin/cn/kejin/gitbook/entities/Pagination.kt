package cn.kejin.gitbook.entities

import java.util.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * Pagination Model
 */
open class Pagination<Item> : BaseResp() {
    var list = ArrayList<Item>()
    var page = 0
    var limit = 0
    var total = 0
}
