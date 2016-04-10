package cn.kejin.gitbook.base

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */

/**
 *
 */
abstract class BaseFragment : Fragment()
{
    protected var rootView : View? = null;

    fun startActivity(clz : Class<*>) {
        startActivity(Intent(activity, clz))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(hasOptionMenu())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        val id = getMenuLayoutId()
        if (id > 0) {
            inflater?.inflate(id, menu)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater?.inflate(getLayoutId(), container, false)
            if (rootView != null) {
                initializeView(rootView as View)
            }
        }
        else {
            val parent = (rootView as View).parent
            if (parent != null) {
                (parent as ViewGroup).removeView(rootView)
            }
        }

        return rootView;
    }

    open fun hasOptionMenu() = true

    open fun getMenuLayoutId(): Int = 0

    abstract fun getLayoutId() : Int

    abstract fun initializeView(view: View)
}