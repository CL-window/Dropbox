package cn.kejin.gitbook.networks

import android.os.Handler
import android.view.View

import cn.kejin.gitbook.MainApplication
import cn.kejin.gitbook.R
import cn.kejin.gitbook.common.*
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

        fun processException(code: Int, msg: String, view: View?) {
            Debug.e(TAG, "HttpE: $code, $msg");
            var errStrId :Int = when (code) {
                E_NO_NETWORK -> R.string.no_network
                E_NOT_SIGN ->  R.string.not_sign
                else -> R.string.unknown_problem
            }

            if (view == null) {
                toast(errStrId)
            }
            else {
                snack(view, errStrId)
            }
        }
    }

    protected  fun post(r : ()->Unit ) {
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
                onSuccess(msg)
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
    open fun onSuccess(body : String) {
        var model: Model = GSON.fromJson(body, cls) ?: throw Exception("parse json failed")

        if (model is Models.BaseResp && model.code != 0) {
            onFailure(model.code, model.error)
        }
        else {
            post {
                onResponse(true, model)
            }
        }
    }

    fun onFailure(code : Int, msg : String)
    {
        Debug.e(this.cls, msg);
        post { onResponse(false, null, code, msg) }
    }

    abstract fun onResponse(success : Boolean, model : Model?, code : Int=0, msg : String="")
}