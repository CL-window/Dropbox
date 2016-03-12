package cn.kejin.gitbook.networks

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import cn.kejin.gitbook.UserAccount
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * Control all network jobs, GitBook REST APIs
 */
class NetworkManager private constructor()// init
{
    companion object {
        val TAG = "NetworkManager"

        val REGISTER_URL = "https://www.gitbook.com/join"
        val BASE_API_URL = "https://api.gitbook.com/"

        val JSON_TYPE = MediaType.parse("application/json; charset:utf-8")

        val DEF_CONNECT_TIMEOUT_SEC = 5

        val instance = NetworkManager()
    }

    private var mHttpClient = OkHttpClient.Builder()
            .authenticator({ route, response ->
                var value = UserAccount.getTokenOrPwd();
                response.request().newBuilder().addHeader("Authorization",
                        Credentials.basic(UserAccount.mUser.name, value)).build()
            })
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
    fun get(uri : String, callback: HttpCallback<Models.BaseResp>) {
        val req = Request.Builder().url(uri).get().build()
        mHttpClient.newCall(req).enqueue(callback)
    }

    /**
     * post method
     */
    fun post(uri : String, json : String,
             callback: HttpCallback<Models.BaseResp>, auth : Boolean = false) {

        val body = RequestBody.create(JSON_TYPE, json);
        val builder = Request.Builder();

        if (auth) {
            var value = UserAccount.getTokenOrPwd();
            if (value.isNullOrEmpty()) {
                callback.onFailure(HttpCallback.E_NOT_SIGN, "not singed")
                return;
            }

            builder.addHeader("Authorization", Credentials.basic(UserAccount.mUser.name, value));
        }

        builder.addHeader("Content-Type", "application/json").url(getAbsUrl(uri)).post(body);

        mHttpClient.newCall(builder.build()).enqueue(callback);
    }

    /**
     * REST APIs
     */

}
