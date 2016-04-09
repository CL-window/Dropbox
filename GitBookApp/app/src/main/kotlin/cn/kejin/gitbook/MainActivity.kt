package cn.kejin.gitbook

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import cn.kejin.gitbook.base.CustomStatusBarActivity
import cn.kejin.gitbook.common.snack
import cn.kejin.gitbook.entities.AppAccount
import cn.kejin.gitbook.navmenu.INavMenu
import cn.kejin.gitbook.navmenu.NavMenuCtrl
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Manager All Fragment
 *
 */
class MainActivity : CustomStatusBarActivity() {
    companion object {
        val TAG = "MainActivity"
    }

    private var mBackFlag = false;

    val navMenuCtrl : INavMenu by lazy {
        NavMenuCtrl(drawerLayout!!, this)
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navMenuCtrl.checkUserState()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && !mBackFlag) {
            if (!navMenuCtrl.onBackPressed()) {
                mBackFlag = true;
                snack(drawerLayout, R.string.press_again)
                handler.postDelayed({ mBackFlag = false }, 2000)
            }

            true
        }
        else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onUserStateChanged(action: MainApp.UserStateListener.Action, oldState: AppAccount) {
        navMenuCtrl.checkUserState()
    }


    //
//    override fun setSupportActionBar(toolbar: Toolbar?) {
//        super.setSupportActionBar(toolbar)
//
//        if (toolbar != null) {
//            val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
//                    R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//            drawerLayout.setDrawerListener(toggle)
//            toggle.syncState()
//        }
//    }
//
}
