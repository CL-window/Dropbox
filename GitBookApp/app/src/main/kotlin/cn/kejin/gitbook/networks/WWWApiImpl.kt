package cn.kejin.gitbook.networks

import cn.kejin.gitbook.entities.WWWExplorePage
import cn.kejin.gitbook.entities.WWWSearchAuthorPage
import cn.kejin.gitbook.entities.WWWSearchBookPage
import cn.kejin.gitbook.entities.WWWTopicsPage
import okhttp3.Call

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */
class WWWApiImpl private constructor(val requester: HttpRequester= Net.defRequester) : WWWApi
{
    companion object {
        val TAG = "WWWApiImpl"
        val instance = WWWApiImpl()
    }
    
    fun <Model> get(uri: String, callback: HttpCallback<Model>, auth: Boolean = false): Call?
            = requester.get(getAbsUrl(uri), callback, auth)


    override fun getExplorePage(page: Int,
                                callback: HttpCallback<WWWExplorePage>): Call? {
        return get("/explore?page=$page",
                object : HttpCallback<WWWExplorePage>(WWWExplorePage::class.java) {
            override fun onResponse(model: WWWExplorePage?, exception: HttpException?) {
                callback.onResponse(model, exception)
            }

            override fun onSuccess(body: String) {
                val ePage = WWWExplorePage.parse(body)
                post { onResponse(ePage) }
            }
        })
    }

    override fun getTopicsPage(callback: HttpCallback<WWWTopicsPage>): Call? {
        return get("/explore/topics",
                object : HttpCallback<WWWTopicsPage>(WWWTopicsPage::class.java) {
            override fun onResponse(model: WWWTopicsPage?, exception: HttpException?) {
                callback.onResponse(model, exception)
            }

            override fun onSuccess(body: String) {
                val page = WWWTopicsPage.parse(body)
                post { onResponse(page) }
            }
        })
    }

    override fun getSearchBookPage(key: String,
                                   sort: WWWApi.SearchSort,
                                   callback: HttpCallback<WWWSearchBookPage>): Call? {
        return get("/search?q=$key&sort=$sort&type=books",
                object : HttpCallback<WWWSearchBookPage>(WWWSearchBookPage::class.java) {
            override fun onResponse(model: WWWSearchBookPage?, exception: HttpException?) {
                callback.onResponse(model, exception)
            }

            override fun onSuccess(body: String) {
                val page = WWWSearchBookPage.parse(body)
                post { onResponse(page) }
            }
        })
    }

    override fun getSearchAuthorPage(key: String,
                                     sort: WWWApi.SearchSort,
                                     callback: HttpCallback<WWWSearchAuthorPage>): Call? {
        var csort = sort;
        if (sort == WWWApi.SearchSort.stars) {
            csort = WWWApi.SearchSort.default;
        }

        return get("/search?q=$key&sort=$csort&type=authors",
                object : HttpCallback<WWWSearchAuthorPage>(WWWSearchAuthorPage::class.java) {
            override fun onResponse(model: WWWSearchAuthorPage?, exception: HttpException?) {
                callback.onResponse(model, exception)
            }

            override fun onSuccess(body: String) {
                val page = WWWSearchAuthorPage.parse(body)
                post { onResponse(page) }
            }
        })
    }

    /**
     * TODO
     */
    override fun getTopicBookPage(topic: String, latest: Boolean, callback: HttpCallback<WWWExplorePage>): Call? {
        throw UnsupportedOperationException()
    }
}