package cn.kejin.gitbook

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import cn.kejin.gitbook.adapters.BaseRecyclerAdapter
import cn.kejin.gitbook.common.displayAvatar
import cn.kejin.gitbook.common.snack
import cn.kejin.gitbook.entities.MyAccount
import cn.kejin.gitbook.fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_nav_header.*
import kotlin.reflect.KClass

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

    class MenuItem {
        val type: MenuType
        var title: Int;
        var icon:  Int;
        var color: Int;
        var fragment: Fragment?
        var clazz: Class<*>?

        var selected = false
            set(value) {
                if (type == MenuType.ITEM) {
                    field = value
                }
            }

        constructor() :
            this(MenuType.SEPARATOR, 0, 0, 0, null, null)

        constructor(_title: Int):
            this(MenuType.GROUP, 0, _title, 0, null, null)

        constructor(_icon: Int, _title: Int, _color: Int, _fragment:Fragment) :
            this(MenuType.ITEM, _icon, _title, _color, _fragment, null)

        constructor(_icon: Int, _title: Int, _color: Int, _clazz: Class<*>) :
            this(MenuType.ITEM, _icon, _title, _color, null, _clazz)

        private constructor(
                _type: MenuType,
                _icon: Int,
                _title: Int,
                _color: Int,
                _fragment:Fragment?,
                _activity:Class<*>?) {
            type = _type
            icon = _icon
            title = _title
            color = _color
            fragment = _fragment
            clazz = _activity
        }
    }

    private val dashboard =
            MenuItem(R.drawable.ic_vector_dashboard_white_48dp,
                     R.string.action_dashboard,
                     Color.GREEN,
                     DashboardFragment())

    private val explore =
            MenuItem(R.drawable.ic_vector_explore_white_48dp,
                     R.string.action_explore,
                     Color.BLUE,
                     ExploreFragment())

    private val topics =
            MenuItem(R.drawable.ic_vector_bookmark_white_48dp,
                     R.string.action_topics,
                     Color.CYAN,
                     TopicsFragment())

    private val sigin =
            MenuItem(R.drawable.ic_vector_person_pin_48dp,
                     R.string.action_sign,
                     Color.MAGENTA,
                     SignActivity::class.java)

    private val settings =
            MenuItem(R.drawable.ic_vector_settings_white_48dp,
                     R.string.action_settings,
                     Color.BLUE,
                     SettingsActivity::class.java)

    private val about =
            MenuItem(R.drawable.ic_vector_info_white_48dp,
                     R.string.action_about,
                     Color.YELLOW,
                     AboutActivity::class.java)

    private val menuItems: MutableList<MenuItem> = mutableListOf(
            MenuItem(),
            dashboard,
            MenuItem(),
            explore,
            topics,
            MenuItem(),
            settings,
            about
    )

    private var mBackFlag = false;
    private val mContentId = R.id.fragmentContent;

    private val mMenuAdapter by lazy {  MenuItemAdapter()  }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        initializeNavigationView()
    }

    override fun getLayoutId(): Int = R.layout.activity_main

    private fun initializeNavigationView()
    {
        // menu items
        val header = inflateView(R.layout.layout_nav_header)
        if (header != null) {
            navList.addHeader(header)
        }
        navList.layoutManager = LinearLayoutManager(this)
        navList.adapter = mMenuAdapter
        mMenuAdapter.set(menuItems)

        exitAccount?.setOnClickListener {
            UserAccount.signOut()
            checkUserState()
        }

        onUserStateChanged(MyAccount())
        mMenuAdapter.selectMenuItem(explore)
    }

    override fun onUserStateChanged(last: MyAccount, now: MyAccount) {
        if (!now.isSingedIn()) {
            // signed out
            menuItems.remove(dashboard)
            menuItems.add(1, sigin)
            mMenuAdapter.notifyDataSetChanged()

            userLayout?.visibility = View.GONE
            return;
        }

        if (!last.isSingedIn()) {
            menuItems.remove(dashboard)
            menuItems.remove(sigin)

            menuItems.add(1, dashboard)

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
            else if (mMenuAdapter.mCurSelectedItem != explore) {
                closeDrawer()
                mMenuAdapter.selectMenuItem(explore)
            }
            else {
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

    private fun addFragment(fragment: Fragment)
            = fragmentManager.beginTransaction()
                    .add(mContentId, fragment, fragment.toString()).commit()

    private fun replaceFragment(fragment: Fragment)
            = fragmentManager.beginTransaction()
                    .replace(mContentId, fragment, fragment.toString())
                    .addToBackStack(fragment.toString()).commit()

    fun openDrawer()
            = drawerLayout.openDrawer(GravityCompat.START)

    fun closeDrawer()
            = if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {}


    private fun startSignInActivity() {
        val intent = Intent(this, SignActivity::class.java)
        startActivityForResult(intent, REQ_SIGN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

    private inner class MenuItemAdapter : BaseRecyclerAdapter<MenuItem, MenuItemAdapter.MenuViewHolder>(this)
    {
        override fun getItemViewType(position: Int): Int {
            return data[position].type.ordinal
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MenuViewHolder? {
            return when (viewType) {
                MenuType.GROUP.ordinal -> MenuViewHolder(inflateView(MenuType.GROUP.layout))
                MenuType.SEPARATOR.ordinal -> MenuViewHolder(inflateView(MenuType.SEPARATOR.layout))
                else -> {
                    MenuViewHolder(inflateView(MenuType.ITEM.layout))
                }
            }
        }

        inner class MenuViewHolder(view: View) : BaseViewHolder<MenuItem>(view) {
            override fun bindView(model: MenuItem, pos: Int) {
                when (model.type) {
                    MenuType.ITEM -> {
                        itemView.isEnabled = true;

                        var iconView = itemView.findViewById(R.id.menuIcon) as ImageView
                        var textView = itemView.findViewById(R.id.menuText) as TextView

                        textView.text = getString(model.title) ?: ""
                        iconView.setImageResource(model.icon)

                        var bgColor = Color.TRANSPARENT
                        var textColor = resources.getColor(R.color.textPrimary);
                        var iconColor = model.color;

                        if (model.selected) {
                            bgColor = Color.LTGRAY
                            iconColor = resources.getColor(R.color.colorPrimary)
                            textColor = iconColor
                        }

                        itemView.setBackgroundColor(bgColor)
                        iconView.setColorFilter(iconColor)
                        textView.setTextColor(textColor)
                    }

                    MenuType.GROUP -> {
                        itemView.isEnabled = false

                        var textView = itemView as TextView
                        textView.text = getString(model.title)?:""
                    }

                    MenuType.SEPARATOR -> {
                        itemView.isEnabled = false
                    }
                }

                itemView.setOnClickListener {
                    selectMenuItem(pos)
                }
            }
        }

        var mCurSelectedItem = explore
            private set(value) {
                if (value.type == MenuType.ITEM) {

                    if (value.fragment != null) {
                        field.selected = false
                        value.selected = true
                        field = value
                        replaceFragment(value.fragment as Fragment)
                    }
                    else if (value.clazz != null) {
                        if (value.clazz == SignActivity::class.java) {
                            startSignInActivity()
                        }
                        else {
                            startActivity(value.clazz as Class<*>)
                        }
                    }

                    notifyDataSetChanged()
                }
            }

        fun selectMenuItem(pos: Int) {
            selectMenuItem(data[pos])
        }

        fun selectMenuItem(item: MenuItem) {
            mCurSelectedItem = item
        }

    }

}
