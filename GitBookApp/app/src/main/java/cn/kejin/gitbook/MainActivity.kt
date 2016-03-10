package cn.kejin.gitbook

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Manager All Fragment
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener
{
    companion object {
        val TAG = "MainActivity"
    }

    private var mBackFlag = false;
    private var mLastSelectedItem = R.id.actionExplore;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        navView.setNavigationItemSelectedListener(this)
        /**
         * first
         */
        navView.menu.findItem(mLastSelectedItem).isChecked = true
    }

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        return if (keyCode == KeyEvent.KEYCODE_BACK && !mBackFlag) {
            closeDrawer()
            mBackFlag = true;
            MainApplication.handler.postDelayed({mBackFlag = false}, 2000)
            return true
        }
        else {
            super.onKeyDown(keyCode, event)
        }
    }

    fun openDrawer() = drawerLayout.openDrawer(GravityCompat.START)

    fun closeDrawer() = drawerLayout.closeDrawer(GravityCompat.START)


    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        if (item == null || item.itemId == mLastSelectedItem) {
            return false
        }

        navView.menu.findItem(mLastSelectedItem).isChecked = false
        item.isChecked = true;

        when (item.itemId) {
            R.id.actionExplore -> startExplorePage()
            R.id.actionMyBooks -> startMyBooksPage()
            R.id.actionProfile -> startProfilePage()
            R.id.actionSettings -> startSettingsActivity()
        }

        mLastSelectedItem = item.itemId;
        closeDrawer()

        return true;
    }

    private fun startMyBooksPage()
    {
        //
    }

    private fun startExplorePage()
    {
        //
    }

    private fun startProfilePage()
    {
        //
    }

    private fun startSettingsActivity()
    {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
