package cn.kejin.gitbook.entities

import cn.kejin.gitbook.networks.Models

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * WWW book
 */
class WWWBook : WWW() {
    var title = "" // title
    var details = ""; // details url
    var star_num = "0" // number of star
        set(value) {
            field = value.trim() //getNumString(value)
        }

    var pub_time = "" // publish time
    var summary = ""

    var author : WWWAuthor = WWWAuthor()

    override fun toString(): String {
        return "Book ($title, $details, $star_num, $pub_time, $summary, $author)"
    }
}