package cn.kejin.gitbook

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Environment
import android.os.Handler
import android.util.DisplayMetrics
import cn.kejin.gitbook.entities.AppAccount
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

        /**
         * default shared preferences
         */
        val sharedPref: SharedPreferences
            get() = instance.getSharedPreferences(APP_SHARED_PREF, MODE_PRIVATE)

        // global preference
        fun getSharedPref(name : String) = instance.getSharedPreferences(name, MODE_PRIVATE)

        // global handler
        val handler : Handler = Handler()

        // for  dpToPx, pxToDp
        val displayMetrics = DisplayMetrics()

        /**
         * get resources
         */
        val resources: Resources
            get() = instance.resources

        fun string(id: Int) = resources.getString(id)

        fun color(id: Int) = resources.getColor(id)


        /**
         * User Account Manage
         */
        /**
         * 保证不被改变
         */
        private val user : AppAccount by lazy {
            val account = AppAccount()
            account.restore()
            account
        }

        /**
         * 传递副本
         */
        val account : AppAccount
            get() = user.copy()

        /**
         * 登录
         */
        fun signIn(ac: AppAccount?):Boolean
                = if (ac != null && ac.isSingedIn()) { user.set(ac); true } else { false }

        /**
         * 登出
         */
        fun signOut() = user.signOut()

        fun isSignedIn() = user.isSingedIn()

        fun authValue() = user.authValue()
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        displayMetrics.setTo(resources.displayMetrics)

        user.restore()
    }


    fun putString(key: String, value: String) {
        sharedPref.edit()?.putString(key, value)?.apply()
    }

    fun getString(key: String, defaultValue: String = ""): String {
        return sharedPref.getString(key, defaultValue)?:defaultValue
    }
}