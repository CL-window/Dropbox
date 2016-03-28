package cn.kejin.gitbook

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import cn.kejin.gitbook.common.GSON
import cn.kejin.gitbook.common.warn
import cn.kejin.gitbook.networks.Models
import okhttp3.Credentials

/**
 * save user account information
 */
class UserAccount {
    private constructor()

    companion object {
        val TAG = "UserAccount"

        val PREF_NAME = "USER"
        val KEY_PREF = "info"

        var user = Models.MyAccount()
            get() = field.copy()
            private set

        fun set(account: Models.MyAccount): Models.MyAccount {
            val back = user;
            user = account;
            save()
            return back
        }

        fun getToken(): String = user.token

        fun isSignedIn() = !user.token.isNullOrEmpty()

        /**
         * Basic auth
         */
        fun getAuthValue() = Credentials.basic(user.name, user.token)

        /**
         * clear user information
         */
        fun signOut() = set(Models.MyAccount())

        /**
         * restore from shared preferences
         */
        fun restore() {
            val pref = MainApplication.getSharedPref(PREF_NAME)

            var value = pref.getString(KEY_PREF, "")
            if (value.isNullOrEmpty()){
                return;
            }

            try {
                user = GSON.fromJson(value, Models.MyAccount::class.java)?: Models.MyAccount();
            }
            catch(e: Exception) {
                warn(TAG, "restore user account exception: " + e.message);
                user = Models.MyAccount()
            }
        }

        /**
         * save user info to shared preferences
         */
        private fun save() {
            val pref = MainApplication.getSharedPref(PREF_NAME)
            var editor = pref.edit();
            editor.putString(KEY_PREF, GSON.toJson(user))
            editor.commit()
        }
    }
}
