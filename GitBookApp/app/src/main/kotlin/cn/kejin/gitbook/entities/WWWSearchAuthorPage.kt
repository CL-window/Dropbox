package cn.kejin.gitbook.entities

import org.jsoup.Jsoup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

class WWWSearchAuthorPage : WWW() {

    companion object {
        /**
         * parse search author result page
         */
        fun parse(body: String) : WWWSearchAuthorPage {
            val page = WWWSearchAuthorPage()

            val doc = Jsoup.parse(body)
            val head = doc.getElementsByClass("pagehead")
            if (head.isNotEmpty()) {
                val menu = head[0].getElementsByClass("menu")
                if (menu.isNotEmpty()) {
                    val m = menu[0].getElementsByTag("a")
                    if (m.size == 2) {
                        page.sum_book = m[0].text()
                        page.sum_author = m[1].text()
                    }
                }
            }

            doc.getElementsByClass("user-inner").forEach {
                page.authors.add(WWWAuthor.parse(it))
            };

            return page;
        }
    }

    var authors : MutableList<WWWAuthor> = mutableListOf()
    var sum_book = "0"
        set(value) {
            field = getNumString(value)
        }

    var sum_author = "0"
        set(value) {
            field = getNumString(value)
        }

    override fun toString(): String {
        var str = "TopicPage ($sum_book:Books, $sum_author:Authors){\n"
        authors.forEach { str += "$it\n" }
        str += "}\n"

        return str
    }
}