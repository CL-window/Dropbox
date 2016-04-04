package cn.kejin.gitbook.controllers

import android.app.Activity
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import cn.kejin.gitbook.R
import java.util.jar.Attributes

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/30
 */

/**
 * view controller for title search layout
 */
class SearchLayerController(_v: View,
                            defVis: Boolean = false,
                            activity: Activity? = null) : BaseViewController(_v, activity)
{
    companion object {
        val TAG = "SearchLayer"
    }

    val editor by lazy {
        rootView.findViewById(R.id.searchEdit) as EditText
    }

    init {
        if (rootView.id != R.id.searchLayer) {
            throw IllegalArgumentException("Need search layer!")
        }

        if (defVis) {
            rootView.visibility = View.VISIBLE
        }
        else {
            rootView.visibility = View.GONE
        }

        rootView.setOnClickListener { dismissLayer() }
        rootView.findViewById(R.id.backSearchBtn).setOnClickListener { dismissLayer() }
        rootView.findViewById(R.id.clearButton).setOnClickListener { editor.setText("") }
    }

    fun showLayer() {
        rootView.visibility = View.VISIBLE
        editor.requestFocus()
        // TODO: animation
    }

    fun dismissLayer() {
        rootView.visibility = View.GONE
    }

    inner class SearchEditBehavior : CoordinatorLayout.Behavior<CardView> {
        constructor():super()
        constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    }
}