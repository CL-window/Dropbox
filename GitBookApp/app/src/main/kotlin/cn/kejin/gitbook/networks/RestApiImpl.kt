package cn.kejin.gitbook.networks

import cn.kejin.gitbook.common.info
import cn.kejin.gitbook.entities.*
import okhttp3.Call
import okhttp3.Credentials
import okhttp3.Request

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */
class RestApiImpl private constructor(val requester: HttpRequester= HttpRequester()) : RestApi
{
    companion object {
        val TAG="RestApiImpl"
        val instance=RestApiImpl()
    }

    fun <Model> get(uri: String, callback: HttpCallback<Model>, auth: Boolean = false): Call?
            = requester.get(getAbsUrl(uri), callback, auth)


    /**
     * Special
     */
    override fun signIn(username: String,
                        pwd: String,
                        callback: HttpCallback<MyAccount>): Call? {
        val url = getAbsUrl("account")
        info(TAG, "URL: $url , UserName: $username, Pwd: $pwd")

        val builder = Request.Builder().url(url).get()
        builder.addHeader("Authorization", Credentials.basic(username, pwd));

        val call = requester.httpClient.newCall(builder.build())
        call.enqueue(callback)

        return call
    }

    override fun getMyBooks(page: Int,
                            limit: Int,
                            callback: HttpCallback<Books>): Call?
            = get("/books?page=$page&limit=$limit", callback, true)

    override fun getMyAuthBooks(page: Int,
                                limit: Int,
                                callback: HttpCallback<Books>): Call?
            = get("/books/author?page=$page&limit=$limit", callback, true)

    override fun getPublicBooks(page: Int,
                                limit: Int,
                                callback: HttpCallback<Books>): Call?
            = get("/books/all?page=$page&limit=$limit", callback)

    override fun getBookDetail(id: String,
                               callback: HttpCallback<ABookDetail>): Call?
            = get("/book/$id", callback)

    override fun getAuthorDetail(name: String,
                                 callback: HttpCallback<Account>): Call?
            = get("/author/$name", callback)

    override fun getAllTopics(page: Int,
                              limit: Int,
                              callback: HttpCallback<Topics>): Call?
            = get("/topics?page=$page&limit=$limit", callback)

    override fun searchTopics(key: String,
                              page: Int,
                              limit: Int,
                              callback: HttpCallback<Topics>): Call?
            = get("/topics?key=$key&page=$page&limit=$limit", callback)

    override fun getTopic(id: String,
                          callback: HttpCallback<ATopic>): Call?
            = get("/topic/$id", callback)

    override fun getBookBranches(id: String,
                                 callback: HttpCallback<List<ABranch>>): Call?
            = get("/book/$id/versions/branches", callback)

    override fun getBookVersionTags(id: String,
                                    callback: HttpCallback<List<ABranch>>): Call?
            = get("/book/$id/versions/tags", callback)

    override fun getBookContents(id: String,
                                 file: String,
                                 lang: String,
                                 callback: HttpCallback<ABookContent>): Call? {
        var filename = file;
        var index = file.lastIndexOf('.')
        if (index > 0) {
            filename.removeRange(index..file.length - 1)
            filename += ".json"
        }

        var url = "/book/$id/contents/"
        if (!lang.isNullOrEmpty()) {
            url += "/$lang/";
        }
        url += filename

        return get(url, callback)
    }

    override fun getBookVersionContents(id: String,
                                        version: String,
                                        file: String,
                                        lang: String,
                                        callback: HttpCallback<ABookContent>): Call? {
        var filename = file;
        var index = file.lastIndexOf('.')
        if (index > 0) {
            filename.removeRange(index..file.length - 1)
            filename += ".json"
        }

        var url = "/book/$id/contents/"
        if (!lang.isNullOrEmpty()) {
            url += "/$lang/";
        }
        url += "v/$version/$filename"

        return get(url, callback)
    }
}