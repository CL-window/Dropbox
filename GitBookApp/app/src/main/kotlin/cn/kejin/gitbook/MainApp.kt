package cn.kejin.gitbook

import android.app.Application
import android.os.Environment
import android.os.Handler
import android.util.DisplayMetrics
import android.view.WindowManager
import com.nostra13.universalimageloader.cache.disc.DiskCache
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import java.io.File

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */
class MainApp : Application()
{
    companion object {
        /**
         * constants
         */
        val APP_NAME = "GitBook"
        val APP_SHARED_PREF = "GitBookPref"

        val APP_DIR = File(Environment.getExternalStorageDirectory(), APP_NAME)

        // global context
        lateinit var instance : MainApp
            private set

        // global preference
        fun getSharedPref(name : String) = instance.getSharedPreferences(name, MODE_PRIVATE)

        // global handler
        val handler : Handler = Handler()

        val displayMetrics = DisplayMetrics()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        displayMetrics.setTo(resources.displayMetrics)

        UserAccount.restore()

        initImageLoader()
    }

    fun initImageLoader() {
        val display = DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .build()

        val conf = ImageLoaderConfiguration.Builder(this)
                .diskCache(UnlimitedDiskCache(cacheDir))
                .diskCacheSize(10 * 1024 * 1024)  // 10 MB
                .defaultDisplayImageOptions(display)
                .build()

        ImageLoader.getInstance().init(conf)
    }

    fun putString(key: String, value: String) {
        getSharedPref(APP_SHARED_PREF)?.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return getSharedPref(APP_SHARED_PREF)?.getString(key, defaultValue)?:defaultValue
    }
}