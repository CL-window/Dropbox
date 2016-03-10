package cn.kejin.gitbook

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_custom_status_bar.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
abstract  class BaseActivity : AppCompatActivity()
{
    /**
     * prevent start activity twice
     */
    protected var mStartFlag = false;

    /**
     * mark whether the status bar has been translucent
     */
    var mStatusBarTranslucentFlag = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        translucentStatusBar()

        var layoutId = getLayoutId();
        if (layoutId > 0) {
            setContentView(layoutId)
        }
    }

    protected fun translucentStatusBar()
    {
        if (!mStatusBarTranslucentFlag &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            mStatusBarTranslucentFlag = true
        }
    }

    fun configStatusBar(visible : Int, color : Int = getColor(R.color.colorPrimary))
    {
        if (mStatusBarTranslucentFlag) {
            customStatusBar.visibility = visible;
            customStatusBar.setBackgroundColor(color)
        }
    }

    override fun onResume() {
        super.onResume()
        mStartFlag = false;
    }

    override fun startActivity(intent: Intent?) {
        if (!mStartFlag) {
            super.startActivity(intent)
            mStartFlag = true;
        }
    }

    override fun startActivity(intent: Intent?, options: Bundle?) {
        if (!mStartFlag) {
            super.startActivity(intent, options)
            mStartFlag = true;
        }
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
        if (!mStartFlag) {
            super.startActivityForResult(intent, requestCode, options)
            mStartFlag = true;
        }
    }

    abstract fun getLayoutId() : Int;

    /**
     * Control the content view of this activity
     */
//    protected inner abstract class BaseContentViewController(
//            val mActivity : BaseActivity = this@BaseActivity)
//    {
//        init {
//            setContentView(getViewId())
//        }
//
//        protected abstract fun getViewId() : Int;
//
//        protected fun setFinishView(id : Int)
//        {
//            findViewById(id)?.setOnClickListener { finish() }
//        }
//    }
}