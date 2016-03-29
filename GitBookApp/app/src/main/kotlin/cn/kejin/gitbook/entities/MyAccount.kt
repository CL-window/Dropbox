package cn.kejin.gitbook.entities

import cn.kejin.gitbook.common.GSON
import cn.kejin.gitbook.networks.Models

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * My Account Info, include token
 */
class MyAccount : Account() {
    var token = ""
    var github = GitHub()

    inner class GitHub {
        var username = ""
        var token = ""
    }

    fun copy() : MyAccount {
        var a = MyAccount()
        a.id = id;
        a.type = type;
        a.username = username;
        a.name = name;
        a.location = location;
        a.website = website;
        a.email = email;
        a.urls.avatar = urls.avatar
        a.urls.profile = urls.profile
        a.token = token;
        a.github.username = github.username;
        a.github.token = github.token;

        return a;
    }

    fun isSingedIn() = !token.isNullOrEmpty()

    override fun equals(other: Any?): Boolean {
        if (other is MyAccount) {
            try {
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