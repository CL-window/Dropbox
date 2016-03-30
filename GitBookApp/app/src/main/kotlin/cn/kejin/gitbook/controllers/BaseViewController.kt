package cn.kejin.gitbook.controllers

import android.app.Activity
import android.content.Context
import android.view.View
import cn.kejin.gitbook.MainApplication

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
abstract class BaseViewController (val view : View, val activity: Activity?=null)
{
    fun findViewById(id: Int) = view.findViewById(id)

    fun finish() = activity?.finish()
}