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
class MainApplication : Application()
{
    companion object {
        val APP_DIR = File(Environment.getExternalStorageDirectory(), "GitBook")

        // global context
        lateinit var instance : MainApplication

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
}