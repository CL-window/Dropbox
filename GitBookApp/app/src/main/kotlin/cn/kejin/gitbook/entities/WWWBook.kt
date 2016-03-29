package cn.kejin.gitbook.entities

import org.jsoup.nodes.Element

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * WWW book
 */
class WWWBook : WWW() {
    companion object {
        /**
         * parse a book
         */
        fun parse(e : Element) : WWWBook {
            var book = WWWBook();
            val header = e.getElementsByClass("book-header")
            if (header.isNotEmpty()) {
                val href = header[0].getElementsByTag("a")
                if (href.isNotEmpty()) {
                    book.details = href[0].attr("href");
                    book.title = href[0].text();
                }
            }

            val meta = e.getElementsByClass("book-meta")
            if (meta.isNotEmpty()) {
                val info = meta[0].getElementsByTag("span");
                if (info.size == 2) {
                    book.star_num = info[0].text();
                    book.pub_time = info[1].text();
                }
            }

            val summary = e.getElementsByClass("book-summary");
            if (summary.isNotEmpty()) {
                book.summary = summary[0].text()
            }

            val author = e.getElementsByClass("book-footer");
            if (author.isNotEmpty()) {
                val links = author[0].getElementsByTag("a");
                if (links.size == 2) {
                    book.author.avatar = links[0].getElementsByTag("img").attr("src")
                    book.author.url = links[1].attr("href")
                    book.author.name = links[1].text()
                }
            }

            return book;
        }
    }

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