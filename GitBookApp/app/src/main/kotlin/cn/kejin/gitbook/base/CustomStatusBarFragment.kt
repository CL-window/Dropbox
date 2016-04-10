package cn.kejin.gitbook.base

import android.os.Bundle
import android.view.View
import cn.kejin.gitbook.R

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/8
 */
@Deprecated("太麻烦, 不值得")
abstract class CustomStatusBarFragment : BaseFragment() {

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (needTranslucentStatusBar() &&
                activity is CustomStatusBarActivity) {
            val csbActivity = (activity as CustomStatusBarActivity)
            if (csbActivity.statusBarTranslucentFlag) {
                view?.findViewById(R.id.customStatusBar)?.visibility = View.VISIBLE
            }
            else {
                view?.findViewById(R.id.customStatusBar)?.visibility = View.GONE
            }
        }
        else {
            view?.findViewById(R.id.customStatusBar)?.visibility = View.GONE
        }
    }

    open fun needTranslucentStatusBar(): Boolean = true
}