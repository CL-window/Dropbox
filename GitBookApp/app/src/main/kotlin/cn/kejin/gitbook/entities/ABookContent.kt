package cn.kejin.gitbook.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * Book Contents
 */
class ABookContent : BaseResp() {
    inner class Next {
        var path = ""
        var title = ""
        var level = "0"
        var exists = false
        var external = false
        var introduction = false
    }
    inner class Chapter {
        var index = 0
        var title = ""
        var introduction = false
        var level = "0"
        var path = ""
        var percent = 0.0
        var done = false

        var prev = Next()
        var next = Next()
    }

    inner class Progress {
        var prevPercent : Double = 0.0
        var percent : Double = 0.0

        var chapters : List<Chapter> = listOf()

        var current = Chapter()
    }

    inner class Sections {
        var type = ""
        var content = ""
    }

    var progress = Progress()
    var sections = Sections()
}
