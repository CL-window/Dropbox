package cn.kejin.gitbook

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import android.content.Context
import android.util.Log
import cn.kejin.gitbook.networks.Models

/**
 * save user account information
 */
class UserAccount
{
    companion object {
        val TAG = "UserAccount"

        val PREF_NAME = "USER"
        val KEY_PREF = "info"

        val mUser = UserAccount()

        fun initalizeUserAccount() = mUser.restoreFromPref()

        fun setUserAccount(account: Models.MyAccount = Models.MyAccount()) = mUser.setFrom(account)

        /**
         * clear user information
         */
        fun signout() = setUserAccount()

        /**
         * if has token, not use password
         */
        fun getTokenOrPwd() : String  =
                if (!mUser.token.isNullOrEmpty()) { mUser.token } else { mUser.password }


        fun isSignedIn() : Boolean = !mUser.token.isNullOrEmpty()
    }


    var id = ""
    var type = ""
    var username = ""
    var name = ""
    var location = ""
    var website = ""

    var email = ""

    var profile = ""
    var avatar = ""

    var token = ""

    // clear it after signed in
    var password = ""

    var github_username = ""
    var github_token = ""


    fun saveToPreference()
    {
        var pref = MainApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        var editor = pref.edit();
        editor.putString(KEY_PREF, Utils.mGson.toJson(mUser))
        editor.commit()
    }

    fun restoreFromPref()
    {
        var pref = MainApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        var value = pref.getString(KEY_PREF, "")

        try {
            val user = Utils.mGson.fromJson(value, UserAccount::class.java)

            mUser.setFrom(user)
        }
        catch(e : Exception) {
            Log.e(TAG, "restore user account exception: " + e.message);
            mUser.setFrom(UserAccount())
        }
    }

    fun setFrom(user : UserAccount)
    {
        this.id = user.id
        this.type = user.type
        this.username = user.username
        this.name = user.name
        this.location = user.location
        this.website = user.website
        this.email = user.email
        this.profile = user.profile
        this.avatar = user.avatar
        this.token = user.token
        this.github_token = user.github_token
        this.github_username = user.github_username
    }

    fun setFrom(account: Models.MyAccount)
    {
        this.id = account.id
        this.type = account.type
        this.username = account.username
        this.name = account.name
        this.location = account.location
        this.website = account.website
        this.email = account.email
        this.profile = account.urls.profile
        this.avatar = account.urls.avatar
        this.token = account.token
        this.github_token = account.github.token
        this.github_username = account.github.username

        saveToPreference()
    }

}
