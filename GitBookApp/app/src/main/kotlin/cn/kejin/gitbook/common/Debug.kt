package cn.kejin.gitbook.common

import android.util.Log

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/15
 */
class Debug
{
    companion object {
        val DEBUG = true

        fun e (tag : String, msg : String) = if (DEBUG) {Log.e(tag, msg)} else {}
        fun w (tag : String, msg : String) = if (DEBUG) {Log.w(tag, msg)} else {}
        fun d (tag : String, msg : String) = if (DEBUG) {Log.d(tag, msg)} else {}
        fun i (tag : String, msg : String) = if (DEBUG) {Log.i(tag, msg)} else {}
        fun v (tag : String, msg : String) = if (DEBUG) {Log.v(tag, msg)} else {}

        fun e (tag : Class<*>, msg : String) = if (DEBUG) {Log.e(tag.simpleName, msg)} else {}
        fun w (tag : Class<*>, msg : String) = if (DEBUG) {Log.w(tag.simpleName, msg)} else {}
        fun d (tag : Class<*>, msg : String) = if (DEBUG) {Log.d(tag.simpleName, msg)} else {}
        fun i (tag : Class<*>, msg : String) = if (DEBUG) {Log.i(tag.simpleName, msg)} else {}
        fun v (tag : Class<*>, msg : String) = if (DEBUG) {Log.v(tag.simpleName, msg)} else {}
    }
}