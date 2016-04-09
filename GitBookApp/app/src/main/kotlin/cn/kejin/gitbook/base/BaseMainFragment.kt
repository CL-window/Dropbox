package cn.kejin.gitbook.base

import cn.kejin.gitbook.MainActivity
import cn.kejin.gitbook.base.BaseFragment
import cn.kejin.gitbook.base.CustomStatusBarFragment
import cn.kejin.gitbook.navmenu.INavMenu

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/22
 */

/**
 * 在 MainActivity 里的 Fragment
 */
abstract class BaseMainFragment : CustomStatusBarFragment()
{
    protected val mainActivity: MainActivity by lazy { activity as MainActivity }

    protected val navMenuCtrl: INavMenu by lazy { mainActivity.navMenuCtrl }
}