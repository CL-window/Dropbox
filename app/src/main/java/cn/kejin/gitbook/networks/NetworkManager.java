package cn.kejin.gitbook.networks;

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Control all network jobs, GitBook REST APIs
 */
public class NetworkManager
{
    public final static String TAG = "NetworkManager";

    private final static NetworkManager mInstance = new NetworkManager();
    public static NetworkManager getInstance()
    {
        return mInstance;
    }

    public final static String BASE_URL = "https://api.gitbook.com/";

    public final Gson mGson = new GsonBuilder().create();

    private final static int DEF_CONNECT_TIMEOUT_SEC = 5;
    private OkHttpClient mHttpClient = new OkHttpClient.Builder()
            .connectTimeout(DEF_CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .readTimeout(DEF_CONNECT_TIMEOUT_SEC * 2, TimeUnit.SECONDS)
            .writeTimeout(DEF_CONNECT_TIMEOUT_SEC * 2, TimeUnit.SECONDS).build();


    private NetworkManager()
    {
        // init
    }

    /**
     * setting okhttpclient 'connect', 'read' and 'write' timeout,
     * the unit is TimeUnit.MILLISECONDS
     * if value <= 0, then will not change
     * @param connect if (connect > 0) set connect timeout
     * @param read    if (read > 0) set read timeout
     * @param write   if (write > 0) set write timeout
     */
    public void setHttpTimeout(long connect, long read, long write)
    {
        if (connect <= 0 && read <= 0 && write <= 0) {
            return;
        }

        OkHttpClient.Builder builder = mHttpClient.newBuilder();

        if (connect > 0) {
            builder.connectTimeout(connect, TimeUnit.MILLISECONDS);
        }
        if (read > 0) {
            builder.readTimeout(read, TimeUnit.MILLISECONDS);
        }
        if (write > 0) {
            builder.writeTimeout(write, TimeUnit.MILLISECONDS);
        }

        mHttpClient = builder.build();
    }

    /**
     *
     */
}
