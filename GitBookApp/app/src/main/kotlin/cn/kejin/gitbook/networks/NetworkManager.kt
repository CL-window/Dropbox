package cn.kejin.gitbook.networks

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import cn.kejin.gitbook.UserAccount
import cn.kejin.gitbook.common.Debug
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * Control all network jobs, GitBook REST APIs
 */
class NetworkManager private constructor()// init
{
    companion object {
        val TAG = "NetworkManager"

        val RESET_PWD_URL = "https://www.gitbook.com/settings/password/reset"
        val REGISTER_URL = "https://www.gitbook.com/join"
        val BASE_API_URL = "https://api.gitbook.com/"

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
    fun getAbsUrl(uri: String): String {

        return BASE_API_URL + if (uri.trim().startsWith("/")) { "/" + uri } else { uri }
    }

    /**
     * get method
     */
    fun get(uri : String, callback: HttpCallback<Models.BaseResp>, auth: Boolean = false) : Call?
    {
        val builder = Request.Builder().url(uri).get()
        if (auth) {
            var value = UserAccount.getToken();
            if (value.isNullOrEmpty()) {
                callback.onFailure(HttpCallback.E_NOT_SIGN, "not singed")
                return null;
            }

            builder.addHeader("Authorization", Credentials.basic(UserAccount.mUser.name, value));
        }

        var call = mHttpClient.newCall(builder.build());
        call .enqueue(callback)

        return call;
    }

    /**
     * post method
     */
    fun post(uri : String, json : String,
             callback: HttpCallback<Models.BaseResp>, auth : Boolean = false) : Call? {

        val body = RequestBody.create(JSON_TYPE, json);
        val builder = Request.Builder();

        if (auth) {
            var value = UserAccount.getToken();
            if (value.isNullOrEmpty()) {
                callback.onFailure(HttpCallback.E_NOT_SIGN, "not singed")
                return null;
            }

            builder.addHeader("Authorization", Credentials.basic(UserAccount.mUser.name, value));
        }

        builder.addHeader("Content-Type", "application/json").url(getAbsUrl(uri)).post(body);

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
        val url = getAbsUrl("account")
        Debug.e(TAG, "URL: $url , UserName: $username, Pwd: $pwd")

        val builder = Request.Builder().url(url).get()
        builder.addHeader("Authorization", Credentials.basic(username, pwd));

        val call = mHttpClient.newCall(builder.build())
        call.enqueue(callback)

        return call
    }
}
