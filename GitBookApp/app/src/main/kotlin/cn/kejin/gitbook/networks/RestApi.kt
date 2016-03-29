package cn.kejin.gitbook.networks

import cn.kejin.gitbook.entities.*
import okhttp3.Call

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/28
 */
interface RestApi : Net {
    companion object {
        val DEF_PAGE_LIMIT = 20
    }

    fun getAbsUrl(uri: String) = Net.getApiAbsUrl(uri)

    /**
     * get author's avatar url
     */
    fun getAuthorAvatarUrl(name: String) = Net.getApiAbsUrl("/author/$name/avatar")

    /**********************************APIs*************************************************/
    /**
     * sign in gitbook
     */
    fun signIn(username: String,
               pwd: String,
               callback: HttpCallback<MyAccount>): Call?

    ////////////////////   Books  //////////////////////////////////////////////////////////
    /**
     * List my books. This includes books from organizations the user can access.
     */
    fun getMyBooks(page: Int,
                   limit: Int = DEF_PAGE_LIMIT,
                   callback: HttpCallback<Books>): Call?

    /**
     * List books by myself
     */
    fun getMyAuthBooks(page: Int,
                       limit: Int = DEF_PAGE_LIMIT,
                       callback: HttpCallback<Books>): Call?

    /**
     * get all public books
     */
    fun getPublicBooks(page: Int,
                       limit: Int = DEF_PAGE_LIMIT,
                       callback: HttpCallback<Books>): Call?

    /**
     * get details about a book
     */
    fun getBookDetail(id: String,
                      callback: HttpCallback<ABookDetail>): Call?

    ///////////////////// Authors ///////////////////////////////////////////////////////////
    /**
     * get details about author
     */
    fun getAuthorDetail(name: String,
                        callback: HttpCallback<Account>): Call?


    ////////////////////// Topics ///////////////////////////////////////////////////////////
    /**
     * get all topics
     */
    fun getAllTopics(page: Int,
                     limit: Int = DEF_PAGE_LIMIT,
                     callback: HttpCallback<Topics>): Call?

    /**
     * search topics of 'key'
     */
    fun searchTopics(key: String,
                     page: Int,
                     limit: Int = DEF_PAGE_LIMIT,
                     callback: HttpCallback<Topics>): Call?

    /**
     * get specified topic
     */
    fun getTopic(id: String,
                 callback: HttpCallback<ATopic>): Call?


    //////////////////// Versions //////////////////////////////////////////////////////////////
    /**
     * get all branches for a book
     */
    fun getBookBranches(id: String,
                        callback: HttpCallback<List<ABranch>>): Call?

    fun getBookVersionTags(id: String,
                           callback: HttpCallback<List<ABranch>>): Call?


    //////////////////// Contents //////////////////////////////////////////////////////////////
    fun getBookContents(id: String,
                        file: String,
                        lang: String = "",
                        callback: HttpCallback<ABookContent>): Call?

    fun getBookVersionContents(id: String,
                               version: String,
                               file: String,
                               lang: String = "",
                               callback: HttpCallback<ABookContent>): Call?
}