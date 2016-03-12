package cn.kejin.gitbook

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
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
import cn.kejin.gitbook.networks.NetworkManager
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_nav_header.*

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

        initializeNavigationView()
        if (savedInstanceState == null) {
            addFragment(exploreFragment, M_EXPLORE)
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initializeNavigationView()
    {
        // menu items
        navList.addHeaderView(inflateView(R.layout.layout_nav_header), null, false)
        navList.adapter = mMenuAdapter
        navList.choiceMode = ListView.CHOICE_MODE_SINGLE
        navList.onItemClickListener = AdapterView.OnItemClickListener {
            adapterView, view, pos, id -> mMenuAdapter.mCurSelectedPos = (pos-navList.headerViewsCount)
        }

        mMenuAdapter.mCurSelectedPos = 0

        // header view
        loginButton?.setOnClickListener({startActivity(LoginActivity::class.java)})
        registerButton?.setOnClickListener({
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(NetworkManager.REGISTER_URL)))
        })

        onUserStateChanged(mLastUserInfo)
    }

    override fun onUserStateChanged(last: UserAccount, now: UserAccount)
    {
        if (now.token.isNullOrEmpty()) {
            userLayout?.visibility = View.GONE
            loginLayout?.visibility = View.VISIBLE
            return;
        }

        if (last.token.isNullOrEmpty() && !now.token.isNullOrEmpty()) {
            userLayout?.visibility = View.VISIBLE
            loginLayout?.visibility = View.GONE
        }


        if (now.avatar != last.avatar && avatarImage != null) {
            ImageLoader.getInstance().displayImage(now.avatar, avatarImage)
        }

        if (now.name != last.name || now.username != last.username) {
            userName?.text = "${now.name} (${now.username})"
        }

        if (now.email != last.email) {
            userEmail?.text = now.email
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        return if (keyCode == KeyEvent.KEYCODE_BACK && !mBackFlag) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            else if (mMenuAdapter.mCurSelectedPos != 0) {
                closeDrawer()
                mMenuAdapter.mCurSelectedPos = 0
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
        var selectable = true

        constructor() : this("", 0, 0, TYPE_SEPARATOR)
        constructor(strId : Int) : this("", strId, 0, TYPE_NO_ICON)

        init {
            selectable = type == TYPE_NORMAL
        }

        fun select() : Boolean
        {
            var result = false;

            if (selectable) {
                when (key) {
                    M_EXPLORE -> {
                        replaceFragment(exploreFragment, key)
                        result =  true;
                    }
                    M_MY_BOOK -> {
                        if (UserAccount.isSignedIn()) {
                            replaceFragment(myBooksFragment, key)
                            result =  true;
                        }
                        else {
                            startActivity(LoginActivity::class.java)
                        }
                    }
                    M_PROFILE -> {
                        if (UserAccount.isSignedIn()) {
                            replaceFragment(profileFragment, key)
                            result =  true;
                        }
                        else {
                            startActivity(LoginActivity::class.java)
                        }
                    }
                    M_SETTING -> startActivity(SettingsActivity::class.java)
                    M_ABOUT -> startActivity(AboutActivity::class.java)
                }
            }

            if (result) {
                closeDrawer()
            }

            return result
        }
    }


    private inner class MenuItemAdapter : BaseAdapter()
    {
        var mCurSelectedPos = 0
            set(pos) {
                if (pos in 0..mMenuItems.size-1 &&
                        pos != mCurSelectedPos && mMenuItems[pos].select()) {
                    field = pos
                    mMenuAdapter.notifyDataSetChanged()
                }
            }

        override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View?
        {
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

                        var bgColor = Color.TRANSPARENT
                        var textColor = resources.getColor(R.color.textPrimary);
                        var iconColor = resources.getColor(R.color.textSecondary);

                        if (item.selectable) {
                            if (pos == mCurSelectedPos) {
                                bgColor = Color.LTGRAY
                                iconColor = resources.getColor(R.color.colorPrimary)
                                textColor = iconColor
                            }
                        }
                        else {
                            textColor = Color.LTGRAY
                            iconColor = textColor
                        }

                        view.setBackgroundColor(bgColor)
                        iconView.setColorFilter(iconColor)
                        textView.setTextColor(textColor)
                    }

                }

                TYPE_NO_ICON -> {
                    if (view != null) {
                        var textView = view as TextView
                        textView.text = getString(item.strId)?:""
                    }
                    view?.setBackgroundColor(Color.TRANSPARENT)
                    view?.isClickable = false
                    view?.isEnabled = false
                }

                TYPE_SEPARATOR -> {
                    view?.setBackgroundColor(Color.TRANSPARENT)
                    view?.isClickable = false
                    view?.isEnabled = false
                }
            }
            return view
        }

        override fun getItem(pos: Int): Any? =
                if (pos in 0..mMenuItems.size-1) {mMenuItems[pos] } else {null}

        override fun getItemId(pos: Int): Long = pos.toLong()

        override fun getCount(): Int = mMenuItems.size

//        override fun getItemViewType(pos: Int): Int = pos
//
//        override fun getViewTypeCount(): Int = mMenuTypes.size
        override fun areAllItemsEnabled(): Boolean  = false
        override fun isEnabled(pos :Int): Boolean
        {
            var item = getItem(pos)
            if (item != null) {
                item = item as ListMenuItem

                return item.selectable
            }

            return false
        }
    }

}
