package cn.kejin.gitbook.entities

import org.jsoup.Jsoup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * https://www.gitbook.com/topics
 */
class WWWTopicsPage : WWW() {
    companion object {

        /**
         * parse all topics page
         */
        fun parse(body: String): WWWTopicsPage {
            val page = WWWTopicsPage()

            val doc = Jsoup.parse(body)
            val topicsList = doc.getElementsByClass("topics-byname");
            topicsList.forEach {
                it.children().forEach {
                    topic ->
                    val t = WWWTopic();
                    if (topic.className() == "entry letter") {
                        t.letter = topic.text()
                    }
                    else {
                        val link = topic.getElementsByTag("a");
                        t.url = link.attr("href");
                        t.name = link.text();

                        t.num = topic.getElementsByTag("span").text();
                    }

                    page.topics.add(t)
                }
            }

            return page
        }
    }

    var topics : MutableList<WWWTopic> = mutableListOf()

    override fun toString(): String {
        var str = "TopicPage (${topics.size}:Topics){\n"
        topics.forEach { str += "$it\n" }
        str += "}\n"

        return str
    }
}
