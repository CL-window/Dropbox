package cn.kejin.gitbook.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * https://www.gitbook.com/topics
 */
class WWWTopicsPage : WWW() {
    var topics : MutableList<WWWTopic> = mutableListOf()

    override fun toString(): String {
        var str = "TopicPage (${topics.size}:Topics){\n"
        topics.forEach { str += "$it\n" }
        str += "}\n"

        return str
    }
}
