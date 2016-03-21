package cn.kejin.gitbook.common

import android.util.Log

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/15
 */
class Debug
{
    companion object {
        val DEBUG = false

        fun e (tag : String, msg : String) : Any = if (DEBUG) {Log.e(tag, msg)} else {println("$tag : $msg")}
        fun w (tag : String, msg : String) : Any = if (DEBUG) {Log.w(tag, msg)} else {println("$tag : $msg")}
        fun d (tag : String, msg : String) : Any = if (DEBUG) {Log.d(tag, msg)} else {println("$tag : $msg")}
        fun i (tag : String, msg : String) : Any = if (DEBUG) {Log.i(tag, msg)} else {println("$tag : $msg")}
        fun v (tag : String, msg : String) : Any = if (DEBUG) {Log.v(tag, msg)} else {println("$tag : $msg")}

        fun e (tag : Class<*>, msg : String) : Any = if (DEBUG) {Log.e(tag.simpleName, msg)} else {println("$tag : $msg")}
        fun w (tag : Class<*>, msg : String) : Any = if (DEBUG) {Log.w(tag.simpleName, msg)} else {println("$tag : $msg")}
        fun d (tag : Class<*>, msg : String) : Any = if (DEBUG) {Log.d(tag.simpleName, msg)} else {println("$tag : $msg")}
        fun i (tag : Class<*>, msg : String) : Any = if (DEBUG) {Log.i(tag.simpleName, msg)} else {println("$tag : $msg")}
        fun v (tag : Class<*>, msg : String) : Any = if (DEBUG) {Log.v(tag.simpleName, msg)} else {println("$tag : $msg")}
    }
}