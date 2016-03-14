package cn.kejin.gitbook

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_custom_status_bar.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/3/10
 */
abstract class BaseActivity : AppCompatActivity()
{
    /**
     * prevent start activity twice
     */
    protected var mStartFlag = false;

    /**
     * mark whether the status bar has been translucent
     */
    var mStatusBarTranslucentFlag = false;

    /**
     * the last time user information
     */
    var mLastUserInfo = UserAccount.mUser;

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

    override fun onResume() {
        super.onResume()
        mStartFlag = false;

        if (mLastUserInfo != UserAccount.mUser) {
            onUserStateChanged(mLastUserInfo)
        }
        mLastUserInfo = UserAccount.mUser
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

    /**
     * show progress dialog
     */
    private var progressDialog : AlertDialog? = null

    fun showProgressDialog()
    {
        if (progressDialog == null) {
            progressDialog = AlertDialog.Builder(this, R.style.TransDialog)
                    .setView(ProgressBar(this))
                    .setCancelable(true)
                    .create()
        }

        if (progressDialog?.isShowing ?:true) {
            return;
        }

        progressDialog?.show()
    }

    fun dismissProgressDialog()
    {
        progressDialog?.dismiss()
    }

    /**
     * post
     */
    fun post (r : () -> Unit) = MainApplication.handler.post { r() }

    fun postDelay (r : () -> Unit, delay : Long) = MainApplication.handler.postDelayed(r, delay)

    /**
     * inflate view
     */
    fun inflateView(id:Int) : View? = if (id > 0) View.inflate(this, id, null) else null

    /**
     * start activity
     */
    fun startActivity(clz : Class<*>) = startActivity(Intent(this, clz))

    /**
     * open browser
     */
    fun startBrowser(uri : String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))


    /**
     * show snack bar
     */
    fun snack(view: View, id : Int, len : Int = Snackbar.LENGTH_SHORT) = cn.kejin.gitbook.common.snack(view, getString(id), len)

    /**
     * signed
     * 0:
     */
    open fun onUserStateChanged(last : UserAccount, now : UserAccount = UserAccount.mUser) {}

    abstract fun getLayoutId() : Int;
}