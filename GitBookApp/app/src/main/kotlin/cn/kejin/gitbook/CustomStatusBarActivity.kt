package cn.kejin.gitbook

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import cn.kejin.gitbook.activity.BaseActivity
import cn.kejin.gitbook.entities.MyAccount

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */

/**
 * 为了支持 19 的透明状态栏, 配合 R.layout.layout_custom_status_bar, R.layout.layout_color_status_bar
 */
abstract class CustomStatusBarActivity : BaseActivity()
{
    /**
     * 标记状态栏是否透明了
     */
    var statusBarTranslucentFlag = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (needTranslucentStatusBar()) {
            translucentStatusBar()
            if (!statusBarTranslucentFlag) {
                findViewById(R.id.customStatusBar)?.visibility=View.GONE
                findViewById(R.id.customColorStatusBar)?.visibility=View.GONE
            }
        }
    }

    protected fun translucentStatusBar()
    {
        if (!statusBarTranslucentFlag &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            statusBarTranslucentFlag = true
        }
    }

    protected fun setStatusColor(color: Int) {
        //
    }

    abstract fun needTranslucentStatusBar(): Boolean

}