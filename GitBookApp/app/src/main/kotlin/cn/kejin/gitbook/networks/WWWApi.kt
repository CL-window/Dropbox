package cn.kejin.gitbook.networks

import cn.kejin.gitbook.entities.*
import okhttp3.Call

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */
interface WWWApi : Net {
    companion object {
//        val impl = WWWApiImpl.instance
    }

    fun getAbsUrl(uri: String): String = Net.getWWWAbsUrl(uri)

    /*****************************APIs***********************************/

    /**
     * get books and topics info in explore page
     */
    fun getExplorePage(page: Int, callback: HttpCallback<WWWExplorePage>): Call?

    /**
     * get topics page
     */
    fun getTopicsPage(callback: HttpCallback<WWWTopicsPage>): Call?


    /**
     * three sort method for search result
     */
    enum class SearchSort { default, stars, updated }

    /**
     * get all searched books
     */
    fun getSearchBookPage(key: String,
                          sort: SearchSort,
                          callback: HttpCallback<WWWSearchBookPage>): Call?

    /**
     * get all searched authors
     */
    fun getSearchAuthorPage(key: String,
                            sort: SearchSort,
                            callback: HttpCallback<WWWSearchAuthorPage>): Call?

    /**
     * get all topics books
     */
    fun getTopicBookPage(topic: String,
                         latest: Boolean,
                         callback: HttpCallback<WWWExplorePage>): Call?
}