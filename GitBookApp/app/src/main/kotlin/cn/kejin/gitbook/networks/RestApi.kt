package cn.kejin.gitbook.networks

import okhttp3.Call

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/28
 */
interface RestApi {
    companion object {
        val impl : RestApi =  RestApiImpl.instance


        val DEF_PAGE_LIMIT = 20

        val BASE_URL = "https://api.gitbook.com/"

        /**
         * get absolute Url
         */
        fun getAbsUrl(uri: String)
                = BASE_URL.removeSuffix("/") + "/" + uri.removePrefix("/")

        /**
         * get author's avatar url
         */
        fun getAuthorAvatarUrl(name: String)
                = getAbsUrl("/author/$name/avatar")
    }

    /**********************************APIs*************************************************/
    /**
     * sign in gitbook
     */
    fun signIn(username: String,
               pwd: String,
               callback: HttpCallback<Models.MyAccount>): Call?

    ////////////////////   Books  //////////////////////////////////////////////////////////
    /**
     * List my books. This includes books from organizations the user can access.
     */
    fun getMyBooks(page: Int,
                   limit: Int = DEF_PAGE_LIMIT,
                   callback: HttpCallback<Models.Books>): Call?

    /**
     * List books by myself
     */
    fun getMyAuthBooks(page: Int,
                       limit: Int = DEF_PAGE_LIMIT,
                       callback: HttpCallback<Models.Books>): Call?

    /**
     * get all public books
     */
    fun getPublicBooks(page: Int,
                       limit: Int = DEF_PAGE_LIMIT,
                       callback: HttpCallback<Models.Books>): Call?

    /**
     * get details about a book
     */
    fun getBookDetail(id: String,
                      callback: HttpCallback<Models.ABookDetail>): Call?

    ///////////////////// Authors ///////////////////////////////////////////////////////////
    /**
     * get details about author
     */
    fun getAuthorDetail(name: String,
                        callback: HttpCallback<Models.Account>): Call?


    ////////////////////// Topics ///////////////////////////////////////////////////////////
    /**
     * get all topics
     */
    fun getAllTopics(page: Int,
                     limit: Int = DEF_PAGE_LIMIT,
                     callback: HttpCallback<Models.Topics>): Call?

    /**
     * search topics of 'key'
     */
    fun searchTopics(key: String,
                     page: Int,
                     limit: Int = DEF_PAGE_LIMIT,
                     callback: HttpCallback<Models.Topics>): Call?

    /**
     * get specified topic
     */
    fun getTopic(id: String,
                 callback: HttpCallback<Models.ATopic>): Call?


    //////////////////// Versions //////////////////////////////////////////////////////////////
    /**
     * get all branches for a book
     */
    fun getBookBranches(id: String,
                        callback: HttpCallback<List<Models.ABranch>>): Call?

    fun getBookVersionTags(id: String,
                           callback: HttpCallback<List<Models.ABranch>>): Call?


    //////////////////// Contents //////////////////////////////////////////////////////////////
    fun getBookContents(id: String,
                        file: String,
                        lang: String = "",
                        callback: HttpCallback<Models.BookContents>): Call?

    fun getBookVersionContents(id: String,
                               version: String,
                               file: String,
                               lang: String = "",
                               callback: HttpCallback<Models.BookContents>): Call?
}