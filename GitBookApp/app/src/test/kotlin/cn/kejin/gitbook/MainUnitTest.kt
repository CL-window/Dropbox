package cn.kejin.gitbook;

import cn.kejin.gitbook.common.Debug
import cn.kejin.gitbook.networks.HttpCallback
import cn.kejin.gitbook.networks.Models
import cn.kejin.gitbook.networks.NetworkManager
import okhttp3.*
import org.jsoup.Jsoup
import org.junit.Test

class MainUnitTest {
    val nm = NetworkManager.instance
    val okHttp = OkHttpClient()

    fun get(uri : String) : String
    {
        var url = uri;
        if (!uri.startsWith("http") && !uri.startsWith("https")) {
            url = nm.getApiAbsUrl(uri);
        }

        Debug.e(NetworkManager.TAG, "Method: GET, URL: $url")

        val builder = Request.Builder().url(url).get()

        return okHttp.newCall(builder.build()).execute().body().string();
    }

    /**
     * post method
     */
    fun post(uri : String, json : String) : String {

        var url = uri;
        if (!uri.startsWith("http") && !uri.startsWith("https")) {
            url = nm.getApiAbsUrl(uri);
        }

        Debug.e(NetworkManager.TAG, "Method: GET, URL: $url, Json: $json")

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

    @Test
    fun testNetwork() {

        getExplorePage(0)
        System.`in`.read()
    }
}