package cn.kejin.gitbook

import android.app.Application
import android.os.Handler
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

        UserAccount.initalizeUserAccount()
    }
}