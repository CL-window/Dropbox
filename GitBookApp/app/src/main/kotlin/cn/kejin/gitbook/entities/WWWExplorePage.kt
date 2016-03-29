package cn.kejin.gitbook.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * Parse explore html
 * https://www.gitbook.com/explore
 */
class WWWExplorePage : WWW() {
    var books : MutableList<WWWBook> = mutableListOf()
    var topics : MutableList<WWWTopic> = mutableListOf()

    override fun toString(): String {
        var str = "ExplorePage (${books.size}:Books, ${topics.size}:Topics){\n"
        books.forEach { str += "$it\n" }
        str += "\n";
        topics.forEach { str += "$it\n" }
        str += "}\n"
        return str
    }
}