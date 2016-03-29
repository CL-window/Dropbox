package cn.kejin.gitbook.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * A Book branch
 */
class ABranch : BaseResp() {
    var name = ""
    var urls = URLs()

    inner class URLs {
        var website = ""
        var epub = ""
        var pdf = ""
        var mobi = ""
    }

    var current = false
}