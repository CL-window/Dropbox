package cn.kejin.gitbook.networks

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient

/**
 * Control all network jobs, GitBook REST APIs
 */
class NetworkManager private constructor()// init
{

    val mGson = GsonBuilder().create()
    private var mHttpClient = OkHttpClient.Builder().connectTimeout(DEF_CONNECT_TIMEOUT_SEC.toLong(), TimeUnit.SECONDS).readTimeout((DEF_CONNECT_TIMEOUT_SEC * 2).toLong(), TimeUnit.SECONDS).writeTimeout((DEF_CONNECT_TIMEOUT_SEC * 2).toLong(), TimeUnit.SECONDS).build()

    /**
     * setting okhttpclient 'connect', 'read' and 'write' timeout,
     * the unit is TimeUnit.MILLISECONDS
     * if value <= 0, then will not change
     * @param connect if (connect > 0) set connect timeout
     * *
     * @param read    if (read > 0) set read timeout
     * *
     * @param write   if (write > 0) set write timeout
     */
    fun setHttpTimeout(connect: Long, read: Long, write: Long) {
        if (connect <= 0 && read <= 0 && write <= 0) {
            return
        }

        val builder = mHttpClient.newBuilder()

        if (connect > 0) {
            builder.connectTimeout(connect, TimeUnit.MILLISECONDS)
        }
        if (read > 0) {
            builder.readTimeout(read, TimeUnit.MILLISECONDS)
        }
        if (write > 0) {
            builder.writeTimeout(write, TimeUnit.MILLISECONDS)
        }

        mHttpClient = builder.build()
    }

    /**
     * get absolute URL
     */
    fun getAbsUrl(uri: String?): String {
        var uri = uri
        if (uri == null) {
            uri = ""
        }

        if (uri.startsWith("/")) {
            uri = uri.substring(1, uri.length)
        }
        return BASE_URL + uri
    }

    /**
     * Base GET, POST Method
     */
    operator fun get(uri: String) {
        //
    }

    companion object {
        val TAG = "NetworkManager"

        val instance = NetworkManager()

        val BASE_URL = "https://api.gitbook.com/"

        private val DEF_CONNECT_TIMEOUT_SEC = 5
    }

    /**
     * REST APIs
     */
}
