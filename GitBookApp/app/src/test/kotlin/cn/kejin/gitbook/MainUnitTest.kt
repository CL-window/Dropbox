package cn.kejin.gitbook;

import cn.kejin.gitbook.networks.NetworkManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.jsoup.Jsoup
import org.junit.Test

class MainUnitTest {
    val nm = NetworkManager.instance
    val okHttp = OkHttpClient()

    fun get(uri: String): String {
        var url = uri;
        if (!uri.startsWith("http") && !uri.startsWith("https")) {
            url = nm.getApiAbsUrl(uri);
        }

        println("Method: GET, URL: $url")

        val builder = Request.Builder().url(url).get()

        return okHttp.newCall(builder.build()).execute().body().string();
    }

    /**
     * post method
     */
    fun post(uri: String, json: String): String {

        var url = uri;
        if (!uri.startsWith("http") && !uri.startsWith("https")) {
            url = nm.getApiAbsUrl(uri);
        }

        println("Method: GET, URL: $url, Json: $json")

        val body = RequestBody.create(NetworkManager.JSON_TYPE, json);
        val builder = Request.Builder();

        builder.addHeader("Content-Type", "application/json").url(url).post(body);

        return okHttp.newCall(builder.build()).execute().body().string()
    }

    fun getExplorePage(page: Int) {
        val body = get(nm.getWWWAbsUrl("/explore?page=$page"))

        val epage = nm.parseExplorePage(body)

        println("Model: $epage")
    }

    //@Test
    fun testParseABook() {
        val html = """
        <div class="book">
    <div class="book-inner">
    <div class="book-header">
        <a href="/book/frontendmasters/front-end-handbook/details"><h4>Front-end Handbook</h4></a>
    </div>
    <div class="book-meta">
    <span class="meta">
        <i class="octicon octicon-star"></i> 1K
    </span>
    <span class="meta">
        <i class="octicon octicon-clock"></i> on Mar 8th
    </span>
    </div>
    <div class="book-summary">
        <p>The resources and tools for learning about the practice of front-end development. Written by Cody Lindley sponsored by</p>
    </div>
    </div>
    <div class="book-footer">
    <div class="author">
        <a href="/@frontendmasters" class="author-avatar">
        <img src="https://s.gravatar.com/avatar/3634db7785a8dd45853029aa4d60f5c5?s=220&amp;d=https%3A%2F%2Fwww.gitbook.com%2Fassets%2Fimages%2Favatars%2Fuser.png">
    </a>
    <span>Published by</span>
        <span> <a href="/@frontendmasters">Frontend Masters</a></span>
    </div>

    </div>
    </div>"""

        val doc = Jsoup.parse(html)

        val books = doc.getElementsByClass("book")
        for (book in books) {
            println(nm.parseABook(book))
        }
    }

    //@Test
    fun testParseATopic() {
        val html = """<li>
            <a href="/explore/topic/programming">
                <span class="name">Programming</span>
                <span class="count">| 143</span>
            </a>
        </li>"""

        val doc = Jsoup.parse(html)
        val topics = doc.getElementsByTag("li")
        assert(topics.isNotEmpty(), { println("Topic Size: ${topics.size}") })

        println(nm.parseATopic(topics[0]))
    }


    /**
     * 测试解析explore页面
     */
    //@Test
    fun testParseExplorePage() {
        getExplorePage(0)
    }

    /**
     * 测试解析 topics 页面
     */
    //@Test
    fun testParseTopicsPage() {

        val body = get(nm.getWWWAbsUrl("/explore/topics"))

        println(nm.parseTopicsPage(body))
    }

    /**
     * 测试搜索页面解析
     */
    @Test
    fun testParseSearchPage() {
        val key = "abc"
        val csort = "default"

        val books = get(nm.getWWWAbsUrl("/search?q=$key&sort=$csort&type=books"))
        println(nm.parseBookSearchPage(books))

        val authors = get(nm.getWWWAbsUrl("/search?q=$key&sort=$csort&type=authors"))
        println(nm.parseAuthorSearchPage(authors))
    }
}