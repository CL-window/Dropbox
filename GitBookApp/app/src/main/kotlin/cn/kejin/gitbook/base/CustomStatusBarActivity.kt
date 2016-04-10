package cn.kejin.gitbook.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import cn.kejin.gitbook.R
import cn.kejin.gitbook.base.BaseActivity

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */

/**
 * 为了支持 19 的透明状态栏, 配合 R.layout.layout_custom_status_bar
 * 状态栏的 id 为 customStatusBar
 */
@Deprecated("太麻烦。。。没必要")
abstract class CustomStatusBarActivity : BaseActivity()
{
    /**
     * 标记状态栏是否透明了
     */
    var statusBarTranslucentFlag = false;
        private set

    val customStatusBar: View?
        get() = findViewById(R.id.customStatusBar)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (needTranslucentStatusBar()) {
            translucentStatusBar()
        }

        super.onCreate(savedInstanceState)
    }

    protected fun translucentStatusBar()
    {
        if (!statusBarTranslucentFlag &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            statusBarTranslucentFlag = true
        }
    }

    protected fun setStatusColor(color: Int=resources.getColor(R.color.colorPrimary)) {
        if (statusBarTranslucentFlag) {
            customStatusBar?.setBackgroundColor(color)
        }
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        configCustomStatusBar()
    }

    private fun configCustomStatusBar() {
        if (statusBarTranslucentFlag) {
            customStatusBar?.visibility= View.VISIBLE
        }
        else {
            customStatusBar?.visibility= View.GONE
        }
    }

    open fun needTranslucentStatusBar(): Boolean = true
}