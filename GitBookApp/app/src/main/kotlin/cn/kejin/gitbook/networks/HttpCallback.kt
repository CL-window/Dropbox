package cn.kejin.gitbook.networks

import android.os.Handler

import cn.kejin.gitbook.common.GSON
import cn.kejin.gitbook.MainApplication
import cn.kejin.gitbook.common.Debug
import cn.kejin.gitbook.common.isNetworkConnected
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */
abstract class HttpCallback<Model> (val cls : Class<Model>) : Callback
{
    companion object {
        val TAG = "HttpCallback"

        val E_NO_NETWORK = 1000

        val E_NOT_SIGN = 1001

        // Gson parse error
        val E_GSON = 1002

        val E_UNKNOWN = 9999

    }

    private fun post(r : ()->Unit ) {
        MainApplication.handler.post({ r() });
    }

    override fun onFailure(call: Call?, e: IOException?) {
        call?.cancel();

        if (!isNetworkConnected()) {
            onFailure(E_NO_NETWORK, "no network")
        }
        else {
            onFailure(E_UNKNOWN, e?.message?:"unknown")
        }
    }

    override fun onResponse(call: Call?, resp: Response?) {
        if (resp != null) {
            val msg = resp.body().string();

            try {
                Debug.e(cls, msg)
                var model: Model = GSON.fromJson(msg, cls) ?: throw Exception("parse json failed")

                if (model is Models.BaseResp && model.code != 0) {
                    onFailure(model.code, model.error)
                }
                else {
                    onSuccess(model)
                }
            }
            catch(e : Exception) {
                onFailure(E_GSON, e.message?:"unknown")
            }
        }
        else {
            onFailure(E_UNKNOWN, "unknown")
        }
    }

    /**
     * this method run in thread
     */
    open fun onSuccess(model: Model) = post { onResponse(true, model) }

    fun onFailure(code : Int, msg : String)
    {
        Debug.e(this.cls, msg);
        post { onResponse(false, null, code, msg) }
    }

    abstract fun onResponse(success : Boolean, model : Model?, code : Int=0, msg : String="")
}