package cn.kejin.gitbook.fragments

import cn.kejin.gitbook.MainActivity

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/22
 */
abstract class BaseMainFragment : BaseFragment()
{
    protected val mainActivity: MainActivity by lazy { activity as MainActivity }
}