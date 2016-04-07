package cn.kejin.gitbook.controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import cn.kejin.gitbook.MainApp

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
abstract class BaseViewController (val rootView : View, var activity: Activity?=null)
{
    fun findViewById(id: Int) = rootView.findViewById(id)

    fun finish() = activity?.finish()

    fun startActivity(clz : Class<*>) = activity?.startActivity(Intent(activity, clz))

    fun startActivity(intent: Intent) = activity?.startActivity(intent)
}