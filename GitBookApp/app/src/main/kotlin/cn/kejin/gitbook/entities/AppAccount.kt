package cn.kejin.gitbook.entities

import cn.kejin.gitbook.MainApp
import cn.kejin.gitbook.common.GSON
import cn.kejin.gitbook.common.warn
import okhttp3.Credentials

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * My Account Info, include token
 */
class AppAccount : Account() {
    companion object {
        val TAG = "AppAccount"

        val PREF_NAME = "Account"
        val PREF_KEY = "user"

        val userPref by lazy { MainApp.getSharedPref(PREF_NAME) }
    }

    var token = ""
    var github = GitHub()

    inner class GitHub {
        var username = ""
        var token = ""
    }

    /**
     * 判断是否登录
     */
    fun isSingedIn() = !name.isNullOrEmpty() && !token.isNullOrEmpty()

    /**
     * for Basic auth
     */
    fun authValue() = Credentials.basic(name, token)

    /**
     * copy
     */
    fun copy() : AppAccount {
        return AppAccount().set(this);
    }

    /**
     * set
     */
    fun set(a: AppAccount): AppAccount {
        id          = a.id;
        type        = a.type;
        username    = a.username;
        name        = a.name;
        location    = a.location;
        website     = a.website;
        email       = a.email;
        urls.avatar = a.urls.avatar
        urls.profile    = a.urls.profile
        token           = a.token;
        github.username = a.github.username;
        github.token    = a.github.token;
        return this
    }

    /**
     * 从 SharedPreferences 中读出用户数据
     */
    fun restore() {
        val value = userPref.getString(PREF_KEY, "")
        if (value.isNullOrEmpty()){
            return;
        }

        try {
            set(GSON.fromJson(value, AppAccount::class.java)?: AppAccount());
        }
        catch(e: Exception) {
            warn(TAG, "restore user account exception: " + e.message);
            set(AppAccount())
        }
    }

    /**
     * 保存数据到 SharedPreferences 中
     */
    fun save(clear: Boolean=false) {
        if (clear) {
            this.set(AppAccount())
        }
        else { // 把这两个值空
            this.code = 0
            this.error = ""
        }
        userPref.edit().putString(PREF_KEY, GSON.toJson(this)).commit()
    }

    /**
     * 判断两个用户状态是否相同
     */
    override fun equals(other: Any?): Boolean {
        if (other is AppAccount) {
            try {
                this.code = 0
                this.error = ""
                other.code = 0
                other.error = ""

                return GSON.toJson(this).equals(GSON.toJson(other))
            }
            catch (e : Exception) {
                return false
            }
        }
        return false
    }

    override fun hashCode(): Int{
        var result = token.hashCode()
        result += 31 * result + github.hashCode()
        return result
    }
}