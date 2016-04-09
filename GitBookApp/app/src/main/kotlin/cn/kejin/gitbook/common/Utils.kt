package cn.kejin.gitbook.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.IBinder
import android.os.Process
import android.support.design.widget.Snackbar
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import cn.kejin.gitbook.MainApp
import cn.kejin.gitbook.R
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */

class Utils
{
    companion object {

    }
}

internal val GSON by lazy { GsonBuilder().create() }

/**
 * Global snack show
 */
internal fun snack(view: View, msg: Int, len : Int = Snackbar.LENGTH_SHORT) =
        snack(view, MainApp.string(msg), len)
internal fun snack(view: View, msg : String, len : Int = Snackbar.LENGTH_SHORT) =
        Snackbar.make(view, msg, len).show()

/**
 * Global toast show
 */
internal fun toast(msg: Int, len: Int = Toast.LENGTH_SHORT) =
        toast(MainApp.string(msg), len)
internal fun toast(msg :String, len : Int = Toast.LENGTH_SHORT) =
        Toast.makeText(MainApp.instance, msg, len).show()

/**
 * get current process name
 */
internal fun getCurProcessName(context: Context = MainApp.instance) : String
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

/**
 * whether the network is connected
 */
internal fun isNetworkConnected(context: Context = MainApp.instance) : Boolean
{
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val info = cm.activeNetworkInfo;

    return info != null && info.isConnected && info.state == NetworkInfo.State.CONNECTED;
}

/**
 * Dismiss soft input method
 */
internal fun  dismissSoftInputMethod(context: Context, windowToken : IBinder)
{
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager;
    imm.hideSoftInputFromWindow(windowToken, 0);
}


/**
 * Dp to px
 */
internal fun dpToPx(dp : Float,
                    density : Float = MainApp.displayMetrics.density) : Int
                = (dp * density + 0.5f).toInt();

/**
 * Px to Dp
 */
internal fun pxToDp(px : Float,
                    density : Float = MainApp.displayMetrics.density) : Int
                = (px / density + 0.5f).toInt();


/**
 * 显示头像
 */
internal fun glideAvatar(activity: Activity, url: String, view: ImageView) {
    Glide.with(activity)
            .load(url)
            .error(R.drawable.ic_default_avatar)
            .placeholder(R.drawable.ic_default_avatar)
            .dontAnimate()
            .into(view)
}