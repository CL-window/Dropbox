package cn.kejin.gitbook.common

import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.IBinder
import android.os.Process
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import cn.kejin.gitbook.MainApplication
import com.google.gson.GsonBuilder
import com.nostra13.universalimageloader.core.DisplayImageOptions

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */

class Utils
{
    companion object {

    }
}

internal fun snack(view: View, msg : String, len : Int = Snackbar.LENGTH_SHORT) =
        Snackbar.make(view, msg, len).show()

internal val GSON by lazy { GsonBuilder().create() }

internal fun getCurProcessName(context: Context = MainApplication.instance) : String
{
    val pid = Process.myPid();
    val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
    for (process in am.runningAppProcesses) {
        if (process.pid == pid) {
            return process.processName;
        }
    }

    return ""
}


internal fun isNetworkConnected(context: Context = MainApplication.instance) : Boolean
{
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val info = cm.activeNetworkInfo;

    return info.isConnected && info.state == NetworkInfo.State.CONNECTED;
}

internal fun defaultImageDisplayOption() = DisplayImageOptions.createSimple()

internal fun  dismissSoftInputMethod(context: Context, windowToken : IBinder)
{
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;

    imm.hideSoftInputFromWindow(windowToken, 0);
}