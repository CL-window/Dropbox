package cn.kejin.gitbook.entities

import java.io.Serializable

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/29
 */

/**
 * Base Http Response Model
 */
open class BaseResp : Serializable {
    var code = 0
    var error = ""
}