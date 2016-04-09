package cn.kejin.gitbook.networks

import android.os.Handler
import android.view.View

import cn.kejin.gitbook.MainApp
import cn.kejin.gitbook.R
import cn.kejin.gitbook.common.*
import cn.kejin.gitbook.entities.BaseResp
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */
abstract class HttpCallback<Model> (val cls : Class<Model>,
                                    val gson: Gson = HttpCallback.defGson,
                                    val handler: Handler = HttpCallback.defHandler) : Callback
{
    companion object {
        val defGson : Gson by lazy { GsonBuilder().create() }
        val defHandler = Handler()
    }

    protected fun post(r : ()->Unit ) {
        handler.post({ r() });
    }

    override fun onFailure(call: Call?, e: IOException?) {
        call?.cancel();

        if (!isNetworkConnected()) {
            onFailure(HttpException.E_NO_NETWORK, "no network")
        }
        else {
            onFailure(HttpException.E_OTHER, e?.message?:"unknown")
        }
    }

    override fun onResponse(call: Call?, resp: Response?) {
        if (resp != null) {
            if (resp.code() != 200) {
                onFailure(resp.code(), resp.message())
                return;
            }
            try {
                val msg = resp.body()?.string()?:"";
                onSuccess(msg)
            }
            catch(e : Exception) {
                onFailure(HttpException.E_GSON_PARSE, e.message?:"unknown")
            }
        }
        else {
            onFailure(HttpException.E_OTHER, "unknown")
        }
    }

    /**
     * this method run in thread
     */
    open fun onSuccess(body : String) {
        info(cls, "Success: $body")
        var model: Model = gson.fromJson(body, cls) ?: throw Exception("parse json failed")

        if (model is BaseResp && model.code != 0) {
            onFailure(model.code, model.error)
        }
        else {
            post {
                onResponse(model)
            }
        }
    }

    fun onFailure(code : Int, msg : String) {
        error(this.cls, "Failure: $code, $msg");
        post { onResponse(null, HttpException(code, msg)) }
    }

    /**
     * failed if exception != null
     */
    abstract fun onResponse(model : Model?, exception:HttpException?=null)
}