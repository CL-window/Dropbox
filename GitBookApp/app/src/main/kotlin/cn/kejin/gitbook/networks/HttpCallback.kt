package cn.kejin.gitbook.networks

import cn.kejin.gitbook.GSON
import cn.kejin.gitbook.MainApplication
import cn.kejin.gitbook.Utils
import cn.kejin.gitbook.isNetworkConnected
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */
abstract class HttpCallback<Model : Models.BaseResp> (val cls : Class<Model>) : Callback
{
    companion object {
        val TAG = "HttpCallback"

        val E_NO_NETWORK = 1000

        val E_NOT_SIGN = 1001

        // Gson parse error
        val E_GSON = 1002

        val E_UNKNOWN = 9999

    }

    private fun post(r : (()->Unit) ) {
        MainApplication.handler.post { r };
    }

    override fun onFailure(call: Call?, e: IOException?) {
        call?.cancel();

        post {
            if (!isNetworkConnected()) {
                onFailure(E_NO_NETWORK, "no network")
            }
            else {
                onFailure(E_UNKNOWN, e?.message?:"unknown")
            }
        }
    }

    override fun onResponse(call: Call?, resp: Response?) {
        if (resp != null) {
            val msg = resp.message();

            try {
                var model = GSON.fromJson(msg, cls)
                if (model.code != 0) {
                    post { onFailure(model.code, model.error) }
                }
                else {
                    post { onSuccess(model) }
                }
            }
            catch(e : Exception) {
                post { onFailure(E_GSON, e.message?:"unknown") }
            }
        }
        else {
            MainApplication.handler.post {
                onFailure(E_UNKNOWN, "unknown")
            }
        }
    }

    abstract fun onSuccess(model : Model);

    abstract fun onFailure(code : Int, msg : String);
}