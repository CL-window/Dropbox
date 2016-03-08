package cn.kejin.gitbook

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

import cn.kejin.gitbook.networks.Models

/**
 * save user account information
 */
class UserAccount {
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

    companion object {

        val mUser = UserAccount()

        fun setUserAccount(account: Models.MyAccount?) {
            var account = account
            if (account == null) {
                account = Models.MyAccount()
            }

            mUser.id = account.id
            mUser.type = account.type
            mUser.username = account.username
            mUser.name = account.name
            mUser.location = account.location
            mUser.website = account.website
            mUser.email = account.email
            mUser.profile = account.urls.profile
            mUser.avatar = account.urls.avatar
            mUser.token = account.token
            mUser.github_token = account.github.token
            mUser.github_username = account.github.username
        }

        /**
         * clear user information
         */
        fun signout() {
            setUserAccount(null)
        }
    }
}
