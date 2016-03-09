package cn.kejin.gitbook.networks

import com.google.gson.annotations.SerializedName

import java.util.ArrayList

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/8
 */

/**
 * Json Models
 */
class Models {
    /**
     * just for generic
     */
    open class BaseReq//


    /**
     * Base Response Model
     */
    open class BaseResp {
        var code = 0
        var error = ""
    }

    /**
     * Pagination Model
     */
    open class Pagination<Item> : BaseResp() {
        var list = ArrayList<Item>()
        var page = 0
        var limit = 0
        var total = 0
    }

    /**
     * get details about the instance
     */
    class EndPoint : BaseResp() {
        var type = "public"
        var version = ""

        var urls = URLS()

        inner class URLS {
            var main = ""
            var api = ""
        }
    }

    /**
     * Basic Account Info
     */
    open class Account : BaseResp() {
        var id = ""
        var type = ""
        var username = ""
        var name = ""
        var location = ""
        var website = ""

        var email = ""

        var urls = URLS()

        inner class URLS {
            var profile = ""
            var avatar = ""
        }
    }


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
    }

    /**
     * One Book Model
     */
    open class ABook {
        var id = ""
        var name = ""
        var title = ""
        var description = ""

        @SerializedName("public")
        var isPublic = true

        inner class GitHub {
            var id = ""
        }

        var github = GitHub()

        inner class Cover {
            var large = ""
            var small = ""
        }

        var cover = Cover()

        inner class BookUrls {
            var git = ""
            var access = ""
            var homepage = ""
            var read = ""

            inner class Download {
                var epub = ""
                var mobi = ""
                var pdf = ""
            }

            var download = Download()
        }

        var urls = BookUrls()

        var author = Account()
    }

    /**
     * Details of a book
     */
    class ABookDetail : ABook() {
        inner class License {
            var id = ""
            var title = ""
            var category = ""
            var types = ArrayList<String>()
            var url = ""
            var required = ArrayList<String>()
            var permitted = ArrayList<String>()
            var forbidden = ArrayList<String>()
        }

        var license = License()

        inner class Language {
            var code = ""
            var name = ""
            var nativeName = ""
        }

        var language = Language()

        inner class Dates {
            var build = ""
            var created = ""
        }

        var dates = Dates()

        inner class Permissions {
            var read = false
            var write = false
            var manage = false
        }

        var permissions = Permissions()

        inner class LatestBuild {
            var version = ""
            var finished = ""
            var started = ""
        }

        var latestBuild = LatestBuild()
    }

    class Books : Pagination<ABook>()//

    /**
     * A topic
     */
    class ATopic {
        var id = ""
        var name = ""
        var books = 0
    }

    class Topics : Pagination<ATopic>()//
}
