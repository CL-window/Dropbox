package cn.kejin.gitbook.networks

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import cn.kejin.gitbook.UserAccount
import cn.kejin.gitbook.common.Debug
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Control all network jobs, GitBook REST APIs
 */
class NetworkManager private constructor()// init
{
    companion object {
        val TAG = "NetworkManager"

        val DEF_PAGE_LIMIT = 20

        val RESET_PWD_URL = "https://www.gitbook.com/settings/password/reset"
        val REGISTER_URL = "https://www.gitbook.com/join"
        val BASE_API_URL = "https://api.gitbook.com/"
        val BASE_WWW_URL = "https://www.gitbook.com/"

        val JSON_TYPE = MediaType.parse("application/json; charset:utf-8")

        val DEF_CONNECT_TIMEOUT_SEC = 5

        val instance = NetworkManager()
    }

    private var mHttpClient = OkHttpClient.Builder()
//            .authenticator({ route, response ->
//                var value = UserAccount.getToken();
//                response.request().newBuilder().addHeader("Authorization",
//                        Credentials.basic(UserAccount.mUser.name, value)).build()
//            })
            .connectTimeout(DEF_CONNECT_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS)
            .readTimeout((DEF_CONNECT_TIMEOUT_SEC * 2).toLong(), TimeUnit.SECONDS)
            .writeTimeout((DEF_CONNECT_TIMEOUT_SEC * 2).toLong(), TimeUnit.SECONDS).build()

    /**
     * setting okhttpclient 'connect', 'read' and 'write' timeout,
     * the unit is TimeUnit.MILLISECONDS
     * @param connect set connect timeout
     * *
     * @param read set read timeout
     * *
     * @param write set write timeout
     */
    fun setHttpTimeout(connect: Long = mHttpClient.connectTimeoutMillis().toLong(),
                       read: Long = mHttpClient.readTimeoutMillis().toLong(),
                       write: Long = mHttpClient.writeTimeoutMillis().toLong()) {

        mHttpClient =  mHttpClient.newBuilder()
                .connectTimeout(connect, TimeUnit.MILLISECONDS)
                .readTimeout(read, TimeUnit.MILLISECONDS)
                .writeTimeout(write, TimeUnit.MICROSECONDS).build();
    }

    /**
     * get absolute URL
     */
    fun getApiAbsUrl(uri: String): String {
        return BASE_API_URL + if (uri.trim().startsWith("/")) { "/" + uri } else { uri }
    }

    fun getWWWAbsUrl(uri: String): String {
        return BASE_WWW_URL + if (uri.trim().startsWith("/")) { "/" + uri } else { uri }
    }

    /**
     * get method
     */
    fun <Model> get(uri : String, callback: HttpCallback<Model>, auth: Boolean = false) : Call?
    {
        var url = uri;
        if (!uri.startsWith("http") && !uri.startsWith("https")) {
            url = getApiAbsUrl(uri);
        }

        Debug.e(TAG, "Method: GET, URL: $url, Auth: $auth")

        val builder = Request.Builder().url(url).get()
        if (auth) {
            if (!UserAccount.isSignedIn()) {
                callback.onFailure(HttpCallback.E_NOT_SIGN, "not singed")
                return null;
            }

            builder.addHeader("Authorization", UserAccount.getAuthValue());
        }

        var call = mHttpClient.newCall(builder.build());
        call .enqueue(callback)

        return call;
    }

    /**
     * post method
     */
    fun <Model> post(uri : String, json : String,
             callback: HttpCallback<Model>, auth : Boolean = false) : Call? {

        var url = uri;
        if (!uri.startsWith("http") && !uri.startsWith("https")) {
            url = getApiAbsUrl(uri);
        }

        Debug.e(TAG, "Method: GET, URL: $url, Auth: $auth, Json: $json")

        val body = RequestBody.create(JSON_TYPE, json);
        val builder = Request.Builder();

        if (auth) {
            if (!UserAccount.isSignedIn()) {
                callback.onFailure(HttpCallback.E_NOT_SIGN, "not singed")
                return null;
            }

            builder.addHeader("Authorization", UserAccount.getAuthValue());
        }

        builder.addHeader("Content-Type", "application/json").url(url).post(body);

        var call = mHttpClient.newCall(builder.build())
        call.enqueue(callback);

        return call;
    }

    /**
     * REST APIs
     */

    /**
     * Sign In to user account
     * Method: GET
     */
    fun signIn(username : String, pwd : String, callback: HttpCallback<Models.MyAccount>) : Call
    {
        val url = getApiAbsUrl("account")
        Debug.e(TAG, "URL: $url , UserName: $username, Pwd: $pwd")

        val builder = Request.Builder().url(url).get()
        builder.addHeader("Authorization", Credentials.basic(username, pwd));

        val call = mHttpClient.newCall(builder.build())
        call.enqueue(callback)

        return call
    }

    ////////////////////   Books  //////////////////////////////////////////////////////////
    /**
     * List my books. This includes books from organizations the user can access.
     */
    fun getMyBooks(page: Int, limit: Int = DEF_PAGE_LIMIT, callback : HttpCallback<Models.Books>)
            = get("/books?page=$page&limit=$limit", callback, true)

    /**
     * List books by myself
     */
    fun getMyAuthBooks(page: Int, limit: Int = DEF_PAGE_LIMIT, callback: HttpCallback<Models.Books>)
            = get("/books/author?page=$page&limit=$limit", callback, true)

    /**
     * get all public books
     */
    fun getPublicBooks(page: Int, limit: Int = DEF_PAGE_LIMIT, callback: HttpCallback<Models.Books>)
            = get("/books/all?page=$page&limit=$limit", callback)

    /**
     * get details about a book
     */
    fun getBookDetail(id : String, callback : HttpCallback<Models.ABookDetail>) = get("/book/$id", callback)

    ///////////////////// Authors ///////////////////////////////////////////////////////////
    /**
     * get details about author
     */
    fun getAuthorDetail(name : String, callback : HttpCallback<Models.Account>) = get("/author/$name", callback)

    /**
     * get author's avatar url
     */
    fun getAuthorAvatarUrl(name : String) = getApiAbsUrl("/author/$name/avatar")

    ////////////////////// Topics ///////////////////////////////////////////////////////////
    /**
     * get all topics
     */
    fun getAllTopics(page: Int, limit: Int = DEF_PAGE_LIMIT, callback: HttpCallback<Models.Topics>)
            = get("/topics?page=$page&limit=$limit", callback)

    /**
     * search topics of 'key'
     */
    fun searchTopics(key : String, page: Int, limit: Int = DEF_PAGE_LIMIT, callback: HttpCallback<Models.Topics>)
            = get("/topics?key=$key&page=$page&limit=$limit", callback)

    /**
     * get specified topic
     */
    fun getTopic(id : String, callback: HttpCallback<Models.ATopic>)
            = get("/topic/$id", callback)


    //////////////////// Versions //////////////////////////////////////////////////////////////
    /**
     * get all branches for a book
     */
    fun getBookBranches(id: String, callback : HttpCallback<List<Models.ABranch>>)
            = get("/book/$id/versions/branches", callback)

    fun getBookVersionTags(id: String, callback : HttpCallback<List<Models.ABranch>>)
            = get("/book/$id/versions/tags", callback)


    //////////////////// Contents //////////////////////////////////////////////////////////////
    fun getBookContents(id : String,
                        file : String,
                        lang : String  = "",
                        callback : HttpCallback<Models.BookContents>) : Call? {

        var filename = file;
        var index = file.lastIndexOf('.')
        if (index > 0) {
            filename.removeRange(index..file.length-1)
            filename += ".json"
        }

        var url = "/book/$id/contents/"
        if (!lang.isNullOrEmpty())  {
            url += "/$lang/";
        }
        url += filename

        return get(url, callback)
    }

    fun getBookVersionContents(id : String, version : String, file : String, lang : String = "",
                               callback: HttpCallback<Models.BookContents>) : Call? {

        var filename = file;
        var index = file.lastIndexOf('.')
        if (index > 0) {
            filename.removeRange(index..file.length-1)
            filename += ".json"
        }

        var url = "/book/$id/contents/"
        if (!lang.isNullOrEmpty())  {
            url += "/$lang/";
        }
        url += "v/$version/$filename"

        return get(url, callback)
    }


    //////////////////////// WWW API ///////////////////////////////////////////////////////////
    fun getExploreBooks(page: Int, callback : HttpCallback<Models.WWWExplorePage>) : Call? {
        val url = getWWWAbsUrl("/explore?page=$page")

        return get(url, object : HttpCallback<Models.WWWExplorePage>(Models.WWWExplorePage::class.java) {
            override fun onSuccess(body: String) {
                val page = Models.WWWExplorePage()

                val doc = Jsoup.parse(body)
                // TODO: parse html

                post { onResponse(true, page) }
            }

            override fun onResponse(success: Boolean, model: Models.WWWExplorePage?, code: Int, msg: String) {
                callback.onResponse(success, model, code, msg)
            }
        })
    }

    fun getTopicsPage(callback : HttpCallback<Models.WWWTopicsPage>) : Call? {
        val url = getWWWAbsUrl("/explore/topics")

        return get(url, object : HttpCallback<Models.WWWTopicsPage>(Models.WWWTopicsPage::class.java) {
            override fun onSuccess(body: String) {
                val page = Models.WWWTopicsPage()

                val doc = Jsoup.parse(body)
                // TODO: parse html

                post { onResponse(true, page) }
            }


            override fun onResponse(success: Boolean, model: Models.WWWTopicsPage?, code: Int, msg: String) {
                callback.onResponse(success, model, code, msg)
            }
        })
    }

    enum class  SearchType { books, authors }

    enum class  SearchSort { default,  stars , updated }

    fun getSearchPage(key:String, sort:SearchSort, type:SearchType, callback : HttpCallback<Models.WWWSearchPage>) : Call? {
        var csort = sort
        if (type == SearchType.authors && sort == SearchSort.stars) {
            csort = SearchSort.default
        }

        val url = getWWWAbsUrl("/search?key=$key&sort=$csort&type=$type")

        return get(url, object : HttpCallback<Models.WWWSearchPage>(Models.WWWSearchPage::class.java) {
            override fun onSuccess(body: String) {
                val page = Models.WWWSearchPage()

                val doc = Jsoup.parse(body)
                // TODO: parse html

                post { onResponse(true, page) }
            }


            override fun onResponse(success: Boolean, model: Models.WWWSearchPage?, code: Int, msg: String) {
                callback.onResponse(success, model, code, msg)
            }
        })
    }
}
