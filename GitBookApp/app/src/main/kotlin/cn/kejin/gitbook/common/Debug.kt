package cn.kejin.gitbook.common

import android.util.Log

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/15
 */

val DEBUG = false

internal fun error (tag : String, msg : String) : Any
        = if (DEBUG) {Log.e(tag, msg)} else {println("$tag : $msg")}
internal fun warn (tag : String, msg : String) : Any
        = if (DEBUG) {Log.e(tag, msg)} else {println("$tag : $msg")}
internal fun debug (tag : String, msg : String) : Any
        = if (DEBUG) {Log.e(tag, msg)} else {println("$tag : $msg")}
internal fun info (tag : String, msg : String) : Any
        = if (DEBUG) {Log.e(tag, msg)} else {println("$tag : $msg")}
internal fun verbose (tag : String, msg : String) : Any
        = if (DEBUG) {Log.e(tag, msg)} else {println("$tag : $msg")}

internal fun error (tag : Class<*>, msg : String) : Any
        = if (DEBUG) {Log.e(tag.simpleName, msg)} else {println("$tag : $msg")}
internal fun warn (tag : Class<*>, msg : String) : Any
        = if (DEBUG) {Log.e(tag.simpleName, msg)} else {println("$tag : $msg")}
internal fun debug (tag : Class<*>, msg : String) : Any
        = if (DEBUG) {Log.e(tag.simpleName, msg)} else {println("$tag : $msg")}
internal fun info (tag : Class<*>, msg : String) : Any
        = if (DEBUG) {Log.e(tag.simpleName, msg)} else {println("$tag : $msg")}
internal fun verbose (tag : Class<*>, msg : String) : Any
        = if (DEBUG) {Log.e(tag.simpleName, msg)} else {println("$tag : $msg")}