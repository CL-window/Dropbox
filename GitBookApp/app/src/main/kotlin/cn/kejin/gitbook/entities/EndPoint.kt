package cn.kejin.gitbook.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */
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