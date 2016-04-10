package cn.kejin.gitbook.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import cn.kejin.gitbook.MainActivity
import cn.kejin.gitbook.R
import cn.kejin.gitbook.navmenu.INavMenu

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/22
 */

/**
 * 在 MainActivity 里的 Fragment
 */
abstract class BaseMainFragment : BaseFragment()
{
    protected val mainActivity: MainActivity by lazy { activity as MainActivity }

    protected val navMenuCtrl: INavMenu by lazy { mainActivity.navMenuCtrl }

    protected fun setSupportActionBar(toolbar: Toolbar?)  {
        mainActivity.setSupportActionBar(toolbar)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (hasOptionMenu()) {
            val toolbar = view?.findViewById(R.id.toolbar)
            if (toolbar != null && toolbar is Toolbar) {
                setSupportActionBar(toolbar)
            }
        }
    }
}