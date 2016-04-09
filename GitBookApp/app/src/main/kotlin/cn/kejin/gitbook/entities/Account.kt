package cn.kejin.gitbook.entities

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

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