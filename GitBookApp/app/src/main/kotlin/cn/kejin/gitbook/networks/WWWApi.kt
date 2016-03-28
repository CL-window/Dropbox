package cn.kejin.gitbook.networks

import okhttp3.Call

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */
interface WWWApi
{
    companion object {
        val impl : WWWApi
            get() = WWWApiImpl.instance

        val BASE_URL = "https://www.gitbook.com/"

        val RESET_PWD_URL = getAbsUrl("/settings/password/reset")

        val REGISTER_URL = getAbsUrl("/join")

        fun getAbsUrl(uri: String): String =
                BASE_URL.removeSuffix("/") + "/" + uri.removePrefix("/")
    }


    /*****************************APIs***********************************/

    /**
     * get books and topics info in explore page
     */
    fun getExplorePage(page: Int, callback: HttpCallback<Models.WWWExplorePage>): Call?

    /**
     * get topics page
     */
    fun getTopicsPage(callback: HttpCallback<Models.WWWTopicsPage>): Call?


    /**
     * three sort method for search result
     */
    enum class SearchSort { default, stars, updated }

    /**
     * get all searched books
     */
    fun getSearchBookPage(key: String,
                          sort: SearchSort,
                          callback: HttpCallback<Models.WWWSearchBookPage>): Call?

    /**
     * get all searched authors
     */
    fun getSearchAuthorPage(key: String,
                            sort: SearchSort,
                            callback: HttpCallback<Models.WWWSearchAuthorPage>): Call?

    /**
     * get all topics books
     */
    fun getTopicBookPage(topic: String,
                         latest: Boolean,
                         callback: HttpCallback<Models.WWWExplorePage>): Call?
}