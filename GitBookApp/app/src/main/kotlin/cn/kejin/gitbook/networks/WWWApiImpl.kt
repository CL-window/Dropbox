package cn.kejin.gitbook.networks

import okhttp3.Call
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */
class WWWApiImpl private constructor(val requester: HttpRequester= Network.defRequester) : WWWApi
{
    companion object {
        val TAG = "WWWApiImpl"
        val instance = WWWApiImpl()

        /****************** Parse Methods ***********************/

        /**
         * parse a book html
         */
        fun parseABook(e: Element): Models.WWWBook {
            var book = Models.WWWBook();
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

        /**
         * parse a topic html
         */
        fun parseATopic(e: Element): Models.WWWTopic {
            var topic = Models.WWWTopic();

            val a = e.getElementsByTag("a");
            if (a.isNotEmpty()) {
                topic.url = a[0].attr("href")
                topic.name = a[0].getElementsByClass("name").text()
                topic.num = a[0].getElementsByClass("count").text()
            }

            return topic;
        }

        /**
         * parse a author html
         */
        fun parseAAuthor(author: Element): Models.WWWAuthor {
            val w = Models.WWWAuthor();
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

        /**
         * parse explore html page
         */
        fun parseExplorePage(body: String): Models.WWWExplorePage {
            val ePage = Models.WWWExplorePage()

            val doc = Jsoup.parse(body)
            val books = doc.getElementsByClass("book");
            for (book in books) {
                ePage.books.add(parseABook(book));
            }

            val topicsList = doc.getElementsByClass("topics-list")
            if (topicsList.isNotEmpty()) {
                for (topic in topicsList[0].getElementsByTag("li")) {
                    ePage.topics.add(parseATopic(topic));
                }
            }

            return ePage
        }

        /**
         * parse all topics page
         */
        fun parseTopicsPage(body: String): Models.WWWTopicsPage {
            val page = Models.WWWTopicsPage()

            val doc = Jsoup.parse(body)
            val topicsList = doc.getElementsByClass("topics-byname");
            topicsList.forEach {
                it.children().forEach {
                    topic ->
                    val t = Models.WWWTopic();
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

        /**
         * parse search book result page
         */
        fun parseBookSearchPage(body: String) : Models.WWWSearchBookPage {
            val page = Models.WWWSearchBookPage()

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

            doc.getElementsByClass("book").forEach { page.books.add(parseABook(it)) };

            return page;
        }

        /**
         * parse search author result page
         */
        fun parseAuthorSearchPage(body: String) : Models.WWWSearchAuthorPage {
            val page = Models.WWWSearchAuthorPage()

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
                page.authors.add(parseAAuthor(it))
            };

            return page;
        }
    }

    override fun getExplorePage(page: Int,
                                callback: HttpCallback<Models.WWWExplorePage>): Call? {
        val url = getAbsUrl("/explore?page=$page")

        return requester.get(url, object : HttpCallback<Models.WWWExplorePage>(Models.WWWExplorePage::class.java) {
            override fun onResponse(model: Models.WWWExplorePage?, exception: HttpException?) {
                callback.onResponse(model, exception)
            }

            override fun onSuccess(body: String) {
                val ePage = parseExplorePage(body)
                post { onResponse(ePage) }
            }
        })
    }

    override fun getTopicsPage(callback: HttpCallback<Models.WWWTopicsPage>): Call? {
        val url = getAbsUrl("/explore/topics")

        return requester.get(url, object : HttpCallback<Models.WWWTopicsPage>(Models.WWWTopicsPage::class.java) {
            override fun onResponse(model: Models.WWWTopicsPage?, exception: HttpException?) {
                callback.onResponse(model, exception)
            }

            override fun onSuccess(body: String) {
                val page = parseTopicsPage(body)
                post { onResponse(page) }
            }
        })
    }

    override fun getSearchBookPage(key: String,
                                   sort: WWWApi.SearchSort,
                                   callback: HttpCallback<Models.WWWSearchBookPage>): Call? {
        val url = getAbsUrl("/search?q=$key&sort=$sort&type=books")

        return requester.get(url, object : HttpCallback<Models.WWWSearchBookPage>(Models.WWWSearchBookPage::class.java) {
            override fun onResponse(model: Models.WWWSearchBookPage?, exception: HttpException?) {
                callback.onResponse(model, exception)
            }

            override fun onSuccess(body: String) {
                val page = parseBookSearchPage(body)
                post { onResponse(page) }
            }
        })
    }

    override fun getSearchAuthorPage(key: String,
                                     sort: WWWApi.SearchSort,
                                     callback: HttpCallback<Models.WWWSearchAuthorPage>): Call? {
        var csort = sort;
        if (sort == WWWApi.SearchSort.stars) {
            csort = WWWApi.SearchSort.default;
        }

        val url = getAbsUrl("/search?q=$key&sort=$csort&type=authors")

        return requester.get(url, object : HttpCallback<Models.WWWSearchAuthorPage>(Models.WWWSearchAuthorPage::class.java) {
            override fun onResponse(model: Models.WWWSearchAuthorPage?, exception: HttpException?) {
                callback.onResponse(model, exception)
            }

            override fun onSuccess(body: String) {
                val page = parseAuthorSearchPage(body)
                post { onResponse(page) }
            }
        })
    }

    /**
     * TODO
     */
    override fun getTopicBookPage(topic: String, latest: Boolean, callback: HttpCallback<Models.WWWExplorePage>): Call? {
        throw UnsupportedOperationException()
    }
}