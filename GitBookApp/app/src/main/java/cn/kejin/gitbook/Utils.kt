package cn.kejin.gitbook

import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.gson.GsonBuilder

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */

class Utils
{
    companion object {

        val mGson by lazy { GsonBuilder().create() }

        fun getCurProcessName(context: Context = MainApplication.instance) : String {
            val pid = android.os.Process.myPid();
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;
            for (process in am.runningAppProcesses) {
                if (process.pid == pid) {
                    return process.processName;
                }
            }

            return ""
        }

        fun isNetworkConnected(context: Context = MainApplication.instance) : Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val info = cm.activeNetworkInfo;

            return info.isConnected && info.state == NetworkInfo.State.CONNECTED;
        }

    }
}