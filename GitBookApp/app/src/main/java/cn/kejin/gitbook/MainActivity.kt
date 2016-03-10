package cn.kejin.gitbook

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import cn.kejin.gitbook.fragments.ExploreFragment
import cn.kejin.gitbook.fragments.MyBooksFragment
import cn.kejin.gitbook.fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Manager All Fragment
 *
 * TODO: 自定义menu
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener
{
    companion object {
        val TAG = "MainActivity"

        val F_EXPLORE = "Explore"
        val F_MY_BOOK = "MyBooks"
        val F_PROFILE = "Profile"
    }

    private val exploreFragment: ExploreFragment by lazy { ExploreFragment() }
    private val myBooksFragment: MyBooksFragment by lazy { MyBooksFragment() }
    private val profileFragment: ProfileFragment by lazy { ProfileFragment() }

    private var mBackFlag = false;
    private val mContentId = R.id.fragmentContent;
    private var mLastSelectedItem = R.id.actionExplore;

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        navView.setNavigationItemSelectedListener(this)
        navView.menu.findItem(mLastSelectedItem).isChecked = true

        if (savedInstanceState == null) {
            addFragment(exploreFragment, F_EXPLORE)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        return if (keyCode == KeyEvent.KEYCODE_BACK && !mBackFlag) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            else if (mLastSelectedItem != R.id.actionExplore) {
                replaceFragment(R.id.actionExplore)
            }
            else {
                mBackFlag = true;
                snack(drawerLayout, R.string.press_again)
                MainApplication.handler.postDelayed({ mBackFlag = false }, 2000)
            }

            true
        }
        else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem?): Boolean
    {
        closeDrawer()

        if (item != null && item.itemId != mLastSelectedItem) {
            when (item.itemId) {
                R.id.actionSettings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    return true;
                };

                R.id.actionAbout -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    return true
                }
            }

            replaceFragment(item.itemId)
        }

        return true;
    }

    override fun setSupportActionBar(toolbar: Toolbar?)
    {
        super.setSupportActionBar(toolbar)

        if (toolbar != null) {
            val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawerLayout.setDrawerListener(toggle)
            toggle.syncState()
        }
    }

    private fun addFragment(fragment: Fragment, tag : String) =
            fragmentManager.beginTransaction().add(mContentId, fragment, tag).commit()


    private fun replaceFragment(itemId: Int)
    {

        when (itemId) {
            mLastSelectedItem -> return
            R.id.actionExplore -> replaceFragment(exploreFragment, F_EXPLORE)
            R.id.actionMyBooks -> replaceFragment(myBooksFragment, F_MY_BOOK)
            R.id.actionProfile -> replaceFragment(profileFragment, F_PROFILE)
            else -> return
        }

        navView.menu.findItem(mLastSelectedItem)?.isChecked = false
        navView.menu.findItem(itemId)?.isChecked = true;

        mLastSelectedItem = itemId;
    }

    private fun replaceFragment(fragment: Fragment, tag: String) =
            fragmentManager.beginTransaction().replace(mContentId, fragment, tag).addToBackStack(null).commit()

    fun openDrawer() =
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START)
            } else {}

    fun closeDrawer() =
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {}

}
