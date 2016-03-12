package cn.kejin.gitbook

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Handler
import android.view.View
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import kotlin.properties.Delegates

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/9
 */
class MainApplication : Application()
{
    companion object {
        // global context
        lateinit var instance : MainApplication


        // global handler
        val handler : Handler by lazy { Handler() }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        UserAccount.initializeUserAccount()

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
    }
}