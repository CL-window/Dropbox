package cn.kejin.gitbook.entities

import cn.kejin.gitbook.networks.WWWApiImpl
import org.jsoup.Jsoup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * https://
 */
class WWWSearchBookPage : WWW() {

    companion object {

        /**
         * parse search book result page
         */
        fun parse(body: String) : WWWSearchBookPage {
            val page = WWWSearchBookPage()

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

            doc.getElementsByClass("book").forEach { page.books.add(WWWBook.parse(it)) };

            return page;
        }
    }


    var books : MutableList<WWWBook> = mutableListOf()

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
        books.forEach { str += "$it\n" }
        str += "}\n"

        return str
    }
}