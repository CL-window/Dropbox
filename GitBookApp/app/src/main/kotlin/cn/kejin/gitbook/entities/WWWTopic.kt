package cn.kejin.gitbook.entities

import org.jsoup.nodes.Element


/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

class WWWTopic : WWW() {
    companion object {
        /**
         * parse a topic html
         */
        fun parse(e: Element): WWWTopic {
            var topic = WWWTopic();

            val a = e.getElementsByTag("a");
            if (a.isNotEmpty()) {
                topic.url = a[0].attr("href")
                topic.name = a[0].getElementsByClass("name").text()
                topic.num = a[0].getElementsByClass("count").text()
            }

            return topic;
        }
    }

    var url = "" // topic url

    var name = ""

    var num = "0"
        set(value) {
            field = getNumString(value)
        }

    var letter = ""


    override fun toString(): String {
        return "Topic ($name, $num, $letter, $url)"
    }
}
