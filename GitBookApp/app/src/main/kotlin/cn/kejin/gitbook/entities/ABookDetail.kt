package cn.kejin.gitbook.entities

import java.util.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

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
