package cn.kejin.gitbook.controllers

import android.app.Activity
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import cn.kejin.gitbook.R
import java.util.jar.Attributes

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/30
 */

/**
 * view controller for title search layout
 */
class SearchLayerController(_v: View, activity: Activity? = null) : BaseViewController(_v, activity)
{
    companion object {
        val TAG = "SearchLayer"
    }

    init {
        if (rootView.id != R.id.searchLayer) {
            throw IllegalArgumentException("Need search layer!")
        }
    }

    inner class SearchEditBehavior : CoordinatorLayout.Behavior<CardView> {
        constructor():super()
        constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    }
}