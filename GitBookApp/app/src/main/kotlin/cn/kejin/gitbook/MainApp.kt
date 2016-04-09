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

        // global handler
        val handler : Handler = Handler()

        // for  dpToPx, pxToDp
        val displayMetrics = DisplayMetrics()

        /**
         * shared preferences
         */
        val sharedPref: SharedPreferences
            get() = instance.getSharedPreferences(APP_SHARED_PREF, MODE_PRIVATE)

        // global preference
        fun getSharedPref(name : String) = instance.getSharedPreferences(name, MODE_PRIVATE)

        fun <Model> putValue(key:String, value: Model) {
            when (value) {
                is String -> {
                    sharedPref.edit()?.putString(key, value)?.apply()
                }
                is Int -> {
                    sharedPref.edit()?.putInt(key, value)?.apply()
                }
                is Float -> {
                    sharedPref.edit()?.putFloat(key, value)?.apply()
                }
                is Boolean -> {
                    sharedPref.edit()?.putBoolean(key, value)?.apply()
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun <Model> getValue(key: String, defValue: Model): Model {
            when (defValue) {
                is String -> {
                    return sharedPref.getString(key, defValue) as Model
                }
                is Int -> {
                    return sharedPref.getInt(key, defValue) as Model
                }
                is Float -> {
                    return sharedPref.getFloat(key, defValue) as Model
                }
                is Boolean -> {
                    return sharedPref.getBoolean(key, defValue) as Model
                }
            }

            return defValue
        }


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
        private val userStateListeners = mutableSetOf<UserStateListener>()

        fun registerUserStateListener(listener: UserStateListener) {
            userStateListeners.add(listener)
        }

        fun unregisterUserStateListener(listener: UserStateListener) {
            userStateListeners.remove(listener)
        }

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
         * 是否登录
         */
        fun isSignedIn() = user.isSingedIn()

        /**
         * 授权值
         */
        fun authValue() = user.authValue()


        /**
         * 登录,并保存数据到 xml
         */
        fun signIn(ac: AppAccount?):Boolean {
            if (ac != null && ac.isSingedIn()) {
                val old = user.copy()
                user.set(ac).save();
                userStateListeners.forEach {
                    it.onUserStateChanged(UserStateListener.Action.SIGN_IN, old)
                }
                return true
            }
            return false
        }

        /**
         * 登出, 清除用户数据
         */
        fun signOut() {
            val old = user.copy()
            user.save(true)
            userStateListeners.forEach {
                it.onUserStateChanged(UserStateListener.Action.SIGN_OUT, old)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        displayMetrics.setTo(resources.displayMetrics)
    }

    interface UserStateListener {
        enum class Action {
            SIGN_IN, SIGN_OUT, INFO_CHANGE
        }

        fun onUserStateChanged(action: Action, oldState: AppAccount)
    }
}