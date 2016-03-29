package cn.kejin.gitbook.entities

import com.google.gson.annotations.SerializedName

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * One Book Model
 */
open class ABook{
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