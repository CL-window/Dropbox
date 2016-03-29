package cn.kejin.gitbook.entities

import cn.kejin.gitbook.networks.Models

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

class WWWTopic {
    var url = "" // topic url

    var name = ""

    var num = "0"
        set(value) {
            field = Models.getNumString(value)
        }

    var letter = ""

    override fun toString(): String {
        return "Topic ($name, $num, $letter, $url)"
    }
}
