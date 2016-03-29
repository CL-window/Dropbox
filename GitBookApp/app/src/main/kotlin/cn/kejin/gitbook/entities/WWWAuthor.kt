package cn.kejin.gitbook.entities

import org.jsoup.nodes.Element

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * WWW Author
 */
class WWWAuthor : WWW() {
    companion object {

        /**
         * parse a author html
         */
        fun parse(author: Element): WWWAuthor {
            val w = WWWAuthor();
            w.avatar = author.getElementsByTag("img").attr("src")
            val infos = author.getElementsByClass("user-infos");
            if (infos.isNotEmpty()) {
                val tag = infos[0].getElementsByTag("a");
                w.url = tag.attr("href");
                w.name = tag.text();
            }

            w.join_time = author.getElementsByTag("p").text()

            return w;
        }
    }

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