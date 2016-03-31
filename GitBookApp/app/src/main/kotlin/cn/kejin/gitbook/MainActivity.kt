package cn.kejin.gitbook

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.kejin.gitbook.common.displayAvatar
import cn.kejin.gitbook.common.snack
import cn.kejin.gitbook.entities.MyAccount
import cn.kejin.gitbook.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_nav_header.*

/**
 * Manager All Fragment
 *
 */
class MainActivity : BaseActivity()
{
    companion object {
        val TAG = "MainActivity"


        val REQ_SIGN = 100
    }

    /**
     * A Navigation Menu Item
     */
    enum class MenuType(val layout: Int) {
        ITEM(R.layout.layout_menu_normal),
        GROUP(R.layout.layout_menu_subheader),
        SEPARATOR(R.layout.layout_menu_separator)
    }

    inner class MenuItem {
        val type: MenuType
        var title: Int;
        var icon:  Int;
        var color: Int;
        var target: Any;

        var selected = false
            set(value) {
                if (type == MenuType.ITEM) {
                    field = value
                }
            }

        constructor() :
            this(MenuType.SEPARATOR, 0, 0, 0, 0)

        constructor(_title: Int):
            this(MenuType.GROUP, 0, _title, 0, 0)

        constructor(_icon: Int, _title: Int, _color: Int, _target: Any) :
            this(MenuType.ITEM, _icon, _title, _color, _target)

        private constructor(_type: MenuType, _icon: Int, _title: Int, _color: Int, _target: Any) {
            type = _type
            icon = _icon
            title = _title
            color = _color
            target = _target
        }

        fun bindMenuItemView(context: Context, view: View?, parent: ViewGroup?): View? {
            var itemView = view;
            if (view == null) {
                itemView = LayoutInflater.from(context).inflate(type.layout, parent, false);
            }

            if (itemView != null) {
                when (type) {
                    MenuType.ITEM -> {
                        var iconView = itemView.findViewById(R.id.menuIcon) as ImageView
                        var textView = itemView.findViewById(R.id.menuText) as TextView
                        var icon = resources.getDrawable(icon);

                        textView.text = getString(title) ?: ""
                        iconView.setImageDrawable(icon)

                        var bgColor = Color.TRANSPARENT
                        var textColor = resources.getColor(R.color.textPrimary);
                        var iconColor = resources.getColor(R.color.textSecondary);

                        if (selected) {
                            bgColor = Color.LTGRAY
                            iconColor = resources.getColor(R.color.colorPrimary)
                            textColor = iconColor
                        }

                        itemView.setBackgroundColor(bgColor)
                        iconView.setColorFilter(iconColor)
                        textView.setTextColor(textColor)
                    }

                    MenuType.GROUP -> {
                        var textView = view as TextView
                        textView.text = getString(title)?:""
                    }

                    MenuType.SEPARATOR -> {
                        //
                    }
                }
            }

            return itemView
        }

        fun onSelected() : Boolean {
            if (type == MenuType.ITEM) {
                when (target) {
                    is BaseMainFragment -> {
                        replaceFragment(target as BaseMainFragment, target.toString())
                        return true;
                    }

                    is BaseActivity -> {
                        startActivity((target as BaseActivity).javaClass)
                    }
                }
            }

            return false;
        }
    }

    private val exploreFragment: ExploreFragment by lazy { ExploreFragment() }
    private val myBooksFragment: MyBooksFragment by lazy { MyBooksFragment() }
    private val profileFragment: ProfileFragment by lazy { ProfileFragment() }

    private var mBackFlag = false;
    private val mContentId = R.id.fragmentContent;

    /**
     * All Menu Items
     */
    private val M_SIGN = "Sign"
    private val M_MY_BOOKS = "MyBooks"
    private val M_MY_PROFILE = "MyProfile"

    private val M_EXPLORE = "Explore"
    private val M_HELP = "Help"

    private val M_SETTING = "Setting"
    private val M_ABOUT = "About"

    private val mMenuKeys = listOf(
            M_SIGN, M_MY_BOOKS, M_MY_PROFILE, M_EXPLORE, M_HELP, M_SETTING, M_ABOUT)


    private val TYPE_NORMAL = 1
    private val TYPE_NO_ICON = 2
    private val TYPE_SEPARATOR = 3
    private val mMenuTypes: Map<Int, Int> = mapOf(
            TYPE_NORMAL to R.layout.layout_menu_normal,
            TYPE_NO_ICON to R.layout.layout_menu_subheader,
            TYPE_SEPARATOR to R.layout.layout_menu_separator
    )

    private val mSignMenuItem = ListMenuItem(M_SIGN, R.string.action_sign, R.drawable.ic_vector_person_pin_48dp)
    private val mMyBooksMenuItem = ListMenuItem(M_MY_BOOKS, R.string.action_my_books, R.drawable.ic_vector_book_white_48dp)
    private val mMyProfileMenuItem = ListMenuItem(M_MY_PROFILE, R.string.action_my_profile, R.drawable.ic_vector_account_circle_white_48dp)

    private val mMenuItems : MutableList<ListMenuItem> = mutableListOf(
            ListMenuItem(R.string.sub_menu_home),

            ListMenuItem(),
            ListMenuItem(R.string.sub_menu_gitbook),
            ListMenuItem(M_EXPLORE, R.string.action_explore, R.drawable.ic_vector_explore_white_48dp),
            ListMenuItem(M_HELP, R.string.actionn_help, R.drawable.ic_vector_help_white_48dp),

            ListMenuItem(),
            ListMenuItem(R.string.sub_menu_more),
            ListMenuItem(M_SETTING, R.string.action_settings, R.drawable.ic_vector_help_white_48dp),
            ListMenuItem(M_ABOUT, R.string.action_about, R.drawable.ic_vector_book_white_48dp)
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
            adapterView, view, pos, id ->
                var item = mMenuAdapter.getItem(pos-navList.headerViewsCount)
                if (item != null) {
                    item = item as ListMenuItem
                    mMenuAdapter.mCurSelectedItemKey = item.key
                }
        }

        exitAccount?.setOnClickListener {
            UserAccount.signOut()
            checkUserState()
        }

        onUserStateChanged(MyAccount())
    }

    override fun onUserStateChanged(last: MyAccount, now: MyAccount) {
        if (!now.isSingedIn()) {
            // signed out
            mMenuItems.remove(mMyBooksMenuItem)
            mMenuItems.remove(mMyProfileMenuItem)
            mMenuItems.add(1, mSignMenuItem)
            mMenuAdapter.notifyDataSetChanged()

            userLayout?.visibility = View.GONE
            return;
        }

        if (!last.isSingedIn()) {
            mMenuItems.remove(mMyBooksMenuItem)
            mMenuItems.remove(mMyProfileMenuItem)
            mMenuItems.remove(mSignMenuItem)

            mMenuItems.add(1, mMyBooksMenuItem)
            mMenuItems.add(2, mMyProfileMenuItem)
            mMenuAdapter.notifyDataSetChanged()

            userLayout?.visibility = View.VISIBLE
        }

        userEmail?.text = now.email
        userName?.text = "${now.name} ( ${now.username} )"
        displayAvatar(now.urls.avatar, avatarImage as ImageView)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        return if (keyCode == KeyEvent.KEYCODE_BACK && !mBackFlag) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
            else if (mMenuAdapter.mCurSelectedItemKey != M_EXPLORE) {
                closeDrawer()
                mMenuAdapter.mCurSelectedItemKey = M_EXPLORE
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
            fragmentManager.beginTransaction().replace(mContentId, fragment, tag).addToBackStack(tag).commit()

    fun openDrawer() = drawerLayout.openDrawer(GravityCompat.START)
    fun closeDrawer() =
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {}


    private fun startSignInActivity()
    {
        val intent = Intent(this, SignActivity::class.java)
        startActivityForResult(intent, REQ_SIGN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQ_SIGN -> {
                    openDrawer()
                    postDelay({closeDrawer()}, 1000)
                }
            }
        }
    }

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
                    M_SIGN -> {
                        startSignInActivity()
                    }
                    M_MY_BOOKS -> {
                        if (UserAccount.isSignedIn()) {
                            replaceFragment(myBooksFragment, key)
                            result =  true;
                        }
                        else {
                            startSignInActivity()
                        }
                    }
                    M_MY_PROFILE -> {
                        if (UserAccount.isSignedIn()) {
                            replaceFragment(profileFragment, key)
                            result =  true;
                        }
                        else {
                            startSignInActivity()
                        }
                    }

                    M_EXPLORE -> {
                        replaceFragment(exploreFragment, key)
                        result =  true;
                    }
                    M_HELP -> {
                        startActivity(HelpActivity::class.java)
                    }

                    M_SETTING -> startActivity(SettingsActivity::class.java)
                    M_ABOUT -> startActivity(AboutActivity::class.java)
                }
                closeDrawer()
            }

            return result
        }
    }


    private inner class MenuItemAdapter : BaseAdapter()
    {
        var mCurSelectedItemKey = M_EXPLORE
            set(key) {
                if (mCurSelectedItemKey != key) {
                    var item = findItemByKey(key)
                    if (item != null && item.select()) {
                        field = key
                        mMenuAdapter.notifyDataSetChanged()
                    }
                }
            }

        fun findItemByKey(key : String) : ListMenuItem?
        {
            for (item in mMenuItems) {
                if (item.key == key) {
                    return item;
                }
            }
            return null
        }

        override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View?
        {
            val item = mMenuItems[pos]
            val view = inflateView(mMenuTypes[item.type] ?:0)

            when(item.type) {
                TYPE_NORMAL -> {
                    if (view != null) {
                        var iconView = view.findViewById(R.id.menuIcon) as ImageView
                        var textView = view.findViewById(R.id.menuText) as TextView
                        var icon = resources.getDrawable(item.icon);

                        textView.text = getString(item.strId)?:""
                        iconView.setImageDrawable(icon)

                        var bgColor = Color.TRANSPARENT
                        var textColor = resources.getColor(R.color.textPrimary);
                        var iconColor = resources.getColor(R.color.textSecondary);

                        if (item.selectable) {
                            if (item.key == mCurSelectedItemKey) {
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
