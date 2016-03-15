package cn.kejin.gitbook

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import android.content.Context
import android.util.Log
import cn.kejin.gitbook.common.GSON
import cn.kejin.gitbook.networks.Models
import com.google.gson.Gson

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

        fun initializeUserAccount() = mUser.restoreFromPref()

        fun setUserAccount(account: Models.MyAccount = Models.MyAccount()) = mUser.setFrom(account)

        /**
         * clear user information
         */
        fun signout() = setUserAccount()

        /**
         * if has token, not use password
         */
        fun getToken() : String  = mUser.token


        fun isSignedIn() : Boolean = mUser.isSignedIn()

        fun addUserStateListener(listener: UserStateListener) = mUser.addUserStateListener(listener)
        fun removeUserStateListener(listener: UserStateListener) = mUser.removeUserStateListener(listener)
    }

    constructor()
    constructor(user : UserAccount) { copyFrom(user) }

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

    var github_username = ""
    var github_token = ""


    fun isSignedIn() : Boolean = !token.isNullOrEmpty()

    fun saveToPreference()
    {
        var pref = MainApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        var editor = pref.edit();
        editor.putString(KEY_PREF, GSON.toJson(mUser))
        editor.commit()
    }

    fun restoreFromPref()
    {
        var pref = MainApplication.instance.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        var value = pref.getString(KEY_PREF, "")

        try {
            val user = GSON.fromJson(value, UserAccount::class.java)
            if (user != null) {
                mUser.copyFrom(user)
            }
        }
        catch(e : Exception) {
            Log.e(TAG, "restore user account exception: " + e.message);
            mUser.copyFrom(UserAccount())
        }
    }

    private fun copyFrom(user : UserAccount)
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
        val last = UserAccount(this)

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

        checkUserState(last)
    }

    private fun checkUserState(last : UserAccount)
    {
        val lastToken = last.token;
        if (lastToken.isNullOrEmpty()) {
            if (!token.isNullOrEmpty() && mListeners.isNotEmpty()) {
                mListeners.forEach { it.onUserSignIn(this@UserAccount) }
            }
        }
        else {
            if (token.isNullOrEmpty()) {
                if (mListeners.isNotEmpty()) {
                    mListeners.forEach { it.onUserSignOut() }
                }
            }
            else if (token != lastToken){

                if (mListeners.isNotEmpty()) {
                    mListeners.forEach { it.onUserSignRefresh(this@UserAccount) }
                }
            }
            else {
                if (mListeners.isNotEmpty()) {
                    mListeners.forEach { it.onUserInfoChanged(last, this@UserAccount) }
                }
            }
        }
    }

    var mListeners = mutableSetOf<UserStateListener>();

    fun addUserStateListener(listener: UserStateListener) = mListeners.add(listener)
    fun removeUserStateListener(listener: UserStateListener) = mListeners.remove(listener)

    interface UserStateListener
    {
        fun onUserInfoChanged(last : UserAccount, now : UserAccount)

        fun onUserSignIn(user : UserAccount)

        fun onUserSignOut()

        /**
         * refresh login state
         */
        fun onUserSignRefresh(user : UserAccount)
    }
}
