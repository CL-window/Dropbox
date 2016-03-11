package cn.kejin.gitbook

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.kejin.gitbook.fragments.BaseFragment
import cn.kejin.gitbook.fragments.ExploreFragment
import cn.kejin.gitbook.fragments.MyBooksFragment
import cn.kejin.gitbook.fragments.ProfileFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Manager All Fragment
 *
 * TODO: 自定义menu
 */
class MainActivity : BaseActivity()
{
    companion object {
        val TAG = "MainActivity"

        val M_EXPLORE = "Explore"
        val M_MY_BOOK = "MyBooks"
        val M_PROFILE = "Profile"
        val M_SETTING = "Setting"
        val M_ABOUT = "About"

        val TYPE_NORMAL = 1
        val TYPE_NO_ICON = 2
        val TYPE_SEPARATOR = 3

    }

    private val exploreFragment: ExploreFragment by lazy { ExploreFragment() }
    private val myBooksFragment: MyBooksFragment by lazy { MyBooksFragment() }
    private val profileFragment: ProfileFragment by lazy { ProfileFragment() }

    private var mBackFlag = false;
    private val mContentId = R.id.fragmentContent;
    private var mCurSelectedPos = 0;

    /**
     * All Menu Items
     */

    private val mMenuTypes: Map<Int, Int> = mapOf(
            TYPE_NORMAL to R.layout.layout_menu_normal,
            TYPE_NO_ICON to R.layout.layout_menu_subheader,
            TYPE_SEPARATOR to R.layout.layout_menu_separator
    )

    private val mMenuItems : Array<ListMenuItem> = arrayOf(
            ListMenuItem(M_EXPLORE, R.string.action_explore, R.drawable.ic_explore),
            ListMenuItem(M_MY_BOOK, R.string.action_my_books, R.drawable.ic_book),
            ListMenuItem(M_PROFILE, R.string.action_profile, R.drawable.ic_account_circle),
            ListMenuItem(),
            ListMenuItem(M_SETTING, R.string.action_settings, R.drawable.ic_settings_black),
            ListMenuItem(M_ABOUT, R.string.action_about, R.drawable.ic_book)
    )

    private val mMenuAdapter : MenuItemAdapter by lazy { MenuItemAdapter() }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        navList.addHeaderView(View.inflate(this, R.layout.layout_nav_header, null))
        navList.adapter = mMenuAdapter
        navList.choiceMode = ListView.CHOICE_MODE_SINGLE
        navList.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView, view, pos, id -> processListMenuItemClicked(pos-navList.headerViewsCount)
        }
//        navList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                throw UnsupportedOperationException()
//            }
//
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                throw UnsupportedOperationException()
//            }
//        }

        if (savedInstanceState == null) {
            addFragment(exploreFragment, M_EXPLORE)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        return if (keyCode == KeyEvent.KEYCODE_BACK && !mBackFlag) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            else if (mCurSelectedPos != 0) {
                mCurSelectedPos = 0
                closeDrawer()
                mMenuAdapter.notifyDataSetChanged()
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

    fun processListMenuItemClicked(pos : Int)
    {
        Log.e(TAG, "Pos: " + pos)
        if (pos in 0..mMenuItems.size-1) {
            val item = mMenuItems[pos];
            val back = mCurSelectedPos;
            mCurSelectedPos = pos;
            when (item.key) {
                M_EXPLORE -> replaceFragment(exploreFragment, item.key)
                M_MY_BOOK -> replaceFragment(myBooksFragment, item.key)
                M_PROFILE -> replaceFragment(profileFragment, item.key)
                M_SETTING -> startActivity(SettingsActivity::class.java)
                M_ABOUT -> startActivity(AboutActivity::class.java)
                else -> mCurSelectedPos = back;
            }
        }

        closeDrawer()
        mMenuAdapter.notifyDataSetChanged()
    }
//    override fun onNavigationItemSelected(item: MenuItem?): Boolean
//    {
//        closeDrawer()
//
//        if (item != null && item.itemId != mLastSelectedItem) {
//            when (item.itemId) {
//                R.id.actionSettings -> {
//                    startActivity(Intent(this, SettingsActivity::class.java))
//                    return true;
//                };
//
//                R.id.actionAbout -> {
//                    startActivity(Intent(this, AboutActivity::class.java))
//                    return true
//                }
//            }
//
//            replaceFragment(item.itemId)
//        }
//
//        return true;
//    }

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



    private inner class ListMenuItem(val key : String,
                                     val strId : Int,
                                     val icon : Int = 0,
                                     val type : Int = TYPE_NORMAL)
    {
        var enable = true

        constructor() : this("", 0, 0, TYPE_SEPARATOR)
        constructor(strId : Int) : this("", strId, 0, TYPE_NO_ICON)

        fun getDestination() : Any? = when (key) {
                M_EXPLORE -> exploreFragment
                M_MY_BOOK -> myBooksFragment
                M_PROFILE -> profileFragment
                M_SETTING -> SettingsActivity::class.java
                M_ABOUT -> AboutActivity::class.java
                else -> null
            }
    }


    private inner class MenuItemAdapter : BaseAdapter()
    {
        fun selectItem()
        {
            //
        }


        override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View?
        {
            Log.e(TAG, "View Pos: " + pos)

            var view = convertView

            val item = mMenuItems[pos]
            if (view == null) {
                view = inflateView(mMenuTypes[item.type] ?:0)
            }

            when(item.type) {
                TYPE_NORMAL -> {
                    if (view != null) {
                        var iconView = view.findViewById(R.id.icon) as ImageView
                        var textView = view.findViewById(R.id.text) as TextView
                        var icon = resources.getDrawable(item.icon);

                        textView.text = getString(item.strId)?:""
                        iconView.setImageDrawable(icon)
                    }

                    if (pos == mCurSelectedPos) {
                        view?.setBackgroundColor(Color.LTGRAY)
                    }
                    else {
                        view?.setBackgroundColor(Color.TRANSPARENT)
                    }

                }

                TYPE_NO_ICON -> {
                    if (view != null) {
                        var textView = view as TextView
                        textView.text = getString(item.strId)?:""
                    }
                    view?.setBackgroundColor(Color.TRANSPARENT)
                }

                TYPE_SEPARATOR -> {
                    view?.setBackgroundColor(Color.TRANSPARENT)
                }
            }
            return view
        }

        override fun getItem(pos: Int): Any? =
                if (pos in 0..mMenuItems.size-1) {mMenuItems[pos] } else {null}

        override fun getItemId(pos: Int): Long = pos.toLong()

        override fun getCount(): Int = mMenuItems.size

        override fun getItemViewType(pos: Int): Int =
                if (pos in 0..mMenuItems.size-1) { mMenuItems[pos].type } else {0}

        override fun getViewTypeCount(): Int = mMenuTypes.size

        fun setIconColor(icon : Drawable)
        {
            var textColorSecondary = android.R.attr.textColorSecondary;
            var value = TypedValue();
            if (!theme.resolveAttribute(textColorSecondary, value, true)) {
                return;
            }

            icon.setColorFilter(resources.getColor(value.resourceId), PorterDuff.Mode.MULTIPLY);
        }
    }

}
