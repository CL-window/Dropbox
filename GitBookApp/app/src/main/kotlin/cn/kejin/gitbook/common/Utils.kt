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
import android.widget.ImageView
import android.widget.Toast
import cn.kejin.gitbook.MainApplication
import cn.kejin.gitbook.R
import com.google.gson.GsonBuilder
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader

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
        snack(view, MainApplication.instance.getString(msg), len)
internal fun snack(view: View, msg : String, len : Int = Snackbar.LENGTH_SHORT) =
        Snackbar.make(view, msg, len).show()

/**
 * Global toast show
 */
internal fun toast(msg: Int, len: Int = Toast.LENGTH_SHORT) =
        toast(MainApplication.instance.getString(msg), len)
internal fun toast(msg :String, len : Int = Toast.LENGTH_SHORT) =
        Toast.makeText(MainApplication.instance, msg, len).show()

/**
 * get current process name
 */
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

/**
 * whether the network is connected
 */
internal fun isNetworkConnected(context: Context = MainApplication.instance) : Boolean
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
 * Display Avatar
 */
private val avatarDisplayOption = DisplayImageOptions.Builder()
        .resetViewBeforeLoading(true)
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .showImageOnFail(R.drawable.ic_default_avatar)
        .showImageOnLoading(R.drawable.ic_default_avatar)
        .showImageForEmptyUri(R.drawable.ic_default_avatar).build()
internal fun displayAvatar(url : String, view : ImageView)
        = ImageLoader.getInstance().displayImage(url, view, avatarDisplayOption)

/**
 * Dp to px
 */
internal fun dpToPx(dp : Float,
                    density : Float = MainApplication.displayMetrics.density) : Int
                = (dp * density + 0.5f).toInt();

/**
 * Px to Dp
 */
internal fun pxToDp(px : Float,
                    density : Float = MainApplication.displayMetrics.density) : Int
                = (px / density + 0.5f).toInt();
