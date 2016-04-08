package cn.kejin.gitbook.fragments

import cn.kejin.gitbook.MainActivity
import cn.kejin.gitbook.base.BaseFragment
import cn.kejin.gitbook.base.CustomStatusBarFragment

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/22
 */
abstract class BaseMainFragment : CustomStatusBarFragment()
{
    protected val mainActivity: MainActivity by lazy { activity as MainActivity }
}