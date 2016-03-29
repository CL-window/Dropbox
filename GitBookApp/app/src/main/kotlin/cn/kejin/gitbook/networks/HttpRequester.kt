package cn.kejin.gitbook.networks

import cn.kejin.gitbook.UserAccount
import cn.kejin.gitbook.common.info
import okhttp3.*
import java.util.concurrent.TimeUnit

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/28
 */
class HttpRequester
{
    companion object {
        val TAG = "HttpRequester"

        val JSON_TYPE = MediaType.parse("application/json; charset:utf-8")

        val DEF_CONNECT_TIMEOUT_SEC = 10L
    }

    var httpClient : OkHttpClient;
    constructor(timeout: Long=DEF_CONNECT_TIMEOUT_SEC) {
        httpClient = OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS).build()
    }

    constructor(okHttpClient: OkHttpClient) {
        httpClient = okHttpClient
    }

    /**
     * get sync
     */
    fun get(url: String, auth: Boolean = false) : Response? {
        info(TAG, "Method: GET sync, URL: $url, Auth: $auth")

        val builder = Request.Builder().url(url).get()
        if (auth) {
            if (!UserAccount.isSignedIn()) {
                return null;
            }

            builder.addHeader("Authorization", UserAccount.getAuthValue());
        }

        return httpClient.newCall(builder.build()).execute()
    }

    /**
     * get method async
     */
    fun <Model> get(url: String, callback: HttpCallback<Model>, auth: Boolean = false): Call? {

        info(TAG, "Method: GET async, URL: $url, Auth: $auth")

        val builder = Request.Builder().url(url).get()
        if (auth) {
            if (!UserAccount.isSignedIn()) {
                callback.onFailure(HttpException.E_NOT_SIGN, "not singed")
                return null;
            }

            builder.addHeader("Authorization", UserAccount.getAuthValue());
        }

        var call = httpClient.newCall(builder.build());
        call.enqueue(callback)

        return call;
    }


    /**
     * post method sync
     */
    fun post(url: String, json: String, auth: Boolean = false): Response? {

        info(TAG, "Method: POST, URL: $url, Auth: $auth, Json: $json")

        val body = RequestBody.create(JSON_TYPE, json);
        val builder = Request.Builder();

        if (auth) {
            if (!UserAccount.isSignedIn()) {
                return null;
            }

            builder.addHeader("Authorization", UserAccount.getAuthValue());
        }

        builder.addHeader("Content-Type", "application/json").url(url).post(body);

        return httpClient.newCall(builder.build()).execute()
    }

    /**
     * post method async
     */
    fun <Model> post(url: String, json: String,
                     callback: HttpCallback<Model>, auth: Boolean = false): Call? {

        info(TAG, "Method: POST, URL: $url, Auth: $auth, Json: $json")

        val body = RequestBody.create(JSON_TYPE, json);
        val builder = Request.Builder();

        if (auth) {
            if (!UserAccount.isSignedIn()) {
                callback.onFailure(HttpException.E_NOT_SIGN, "not singed")
                return null;
            }

            builder.addHeader("Authorization", UserAccount.getAuthValue());
        }

        builder.addHeader("Content-Type", "application/json").url(url).post(body);

        var call = httpClient.newCall(builder.build())
        call.enqueue(callback);

        return call;
    }
}