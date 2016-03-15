package cn.kejin.gitbook

import android.app.Application
import android.os.Handler
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

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
        val handler : Handler = Handler()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        UserAccount.initializeUserAccount()

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this))
    }
}