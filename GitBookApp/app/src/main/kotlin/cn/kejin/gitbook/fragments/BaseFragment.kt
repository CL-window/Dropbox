package cn.kejin.gitbook.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
abstract class BaseFragment : Fragment()
{
//    fun configStatusBar(view: View, visible : Int = View.VISIBLE,
//                        color : Int = Color.RED)
//    {
//        if (activity is BaseActivity) {
//            var act = activity as BaseActivity
//
//            if (act.mStatusBarTranslucentFlag) {
//                val statusBar = view.findViewById(R.id.customStatusBar)
//                statusBar?.visibility = visible;
//                statusBar?.setBackgroundColor(color)
//            }
//        }
//    }

    fun startActivity(clz : Class<*>) {
        startActivity(Intent(activity, clz))
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