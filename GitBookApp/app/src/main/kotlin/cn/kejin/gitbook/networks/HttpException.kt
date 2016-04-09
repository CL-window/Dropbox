package cn.kejin.gitbook.networks

import android.support.design.widget.Snackbar
import android.view.View
import cn.kejin.gitbook.R
import cn.kejin.gitbook.common.snack

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/28
 */
class HttpException (val code: Int, val msg: String) : Exception(msg)
{
    companion object {
        val TAG = "HttpException"

        val E_NO_NETWORK = 1000

        val E_NOT_SIGN = 1001

        val E_GSON_PARSE = 1002

        val E_LOGIN_FAIL = 1003

        val E_OTHER = 9999
    }


    fun process(view: View, len: Int=Snackbar.LENGTH_LONG) {

        var errStrId :Int = when (code) {
            E_NO_NETWORK ->
                R.string.no_network
            E_NOT_SIGN ->
                R.string.not_sign
            E_LOGIN_FAIL->
                R.string.login_failed
            else ->
                R.string.unknown_problem
        }

        snack(view, errStrId, len)
    }

    override fun toString(): String {
        return "HttpException: ($code, $msg)"
    }
}