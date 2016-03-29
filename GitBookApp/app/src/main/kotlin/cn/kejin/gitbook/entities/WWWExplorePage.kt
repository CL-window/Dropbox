package cn.kejin.gitbook.entities

import cn.kejin.gitbook.networks.WWWApiImpl
import org.jsoup.Jsoup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * Parse explore html
 * https://www.gitbook.com/explore
 */
class WWWExplorePage : WWW() {
    companion object {
        /**
         * parse explore html page
         */
        fun parse(body: String): WWWExplorePage {
            val ePage = WWWExplorePage()

            val doc = Jsoup.parse(body)
            val books = doc.getElementsByClass("book");
            for (book in books) {
                ePage.books.add(WWWBook.parse(book));
            }

            val topicsList = doc.getElementsByClass("topics-list")
            if (topicsList.isNotEmpty()) {
                for (topic in topicsList[0].getElementsByTag("li")) {
                    ePage.topics.add(WWWTopic.parse(topic));
                }
            }

            return ePage
        }
    }

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