package cn.kejin.gitbook.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * WWW Author
 */
class WWWAuthor : WWW() {
    var avatar = "" // avatar url
    var name = ""
    var url = ""
    var join_time = ""
        set(value) {
            field = value.removePrefix("Joined On ")
        }

    override fun toString(): String {
        return "[$name, $join_time, $url, $avatar]"
    }
}