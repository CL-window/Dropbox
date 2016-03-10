package cn.kejin.gitbook.fragments

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.kejin.gitbook.BaseActivity
import cn.kejin.gitbook.R

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
abstract class BaseFragment : Fragment()
{
    fun configStatusBar(visible : Int,
                        color : Int = activity.getColor(R.color.colorPrimary))
    {
        if (activity is BaseActivity) {
            var act = activity as BaseActivity

            act.configStatusBar(visible, color)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        var view = inflater?.inflate(getLayoutId(), container, false)
        if (view != null) {
            initializeView(view)
        }

        return view;
    }

    abstract fun getLayoutId() : Int

    abstract fun initializeView(view: View)
}