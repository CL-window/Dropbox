package cn.kejin.gitbook.navmenu

import android.app.Activity
import android.app.Fragment
import android.graphics.Color
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.kejin.android.views.ExRecyclerAdapter
import cn.kejin.android.views.ExRecyclerView
import cn.kejin.gitbook.*
import cn.kejin.gitbook.fragments.DashboardFragment
import cn.kejin.gitbook.fragments.ExploreFragment
import cn.kejin.gitbook.fragments.TopicsFragment

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/7
 */
/**
 * 控制所有的 Navigation Menu
 */
class NavMenuCtrl(val drawer: DrawerLayout, val activity: Activity) : INavMenu
{

    private val menuItems: List<MenuItem> = listOf(
            MenuItem(R.string.sub_menu_home),
            MenuItem(R.string.action_dashboard, R.drawable.ic_vector_dashboard_white_48dp, DashboardFragment::class.java),
            MenuItem(),
            MenuItem(R.string.sub_menu_gitbook),
            MenuItem(R.string.action_explore, R.drawable.ic_vector_explore_white_48dp, ExploreFragment::class.java),
            MenuItem(R.string.action_topics, R.drawable.ic_vector_bookmark_white_48dp, TopicsFragment::class.java),
            MenuItem(),
            MenuItem(R.string.sub_menu_more),
            MenuItem(R.string.action_settings, R.drawable.ic_vector_settings_white_48dp, SettingsActivity::class.java),
            MenuItem(R.string.action_about, R.drawable.ic_vector_info_white_48dp, AboutActivity::class.java)
    )

    val menuAdapter = MenuItemAdapter()

    val exRecycler = drawer.findViewById(R.id.navList) as ExRecyclerView

    val navHeader = exRecycler.getHeader()


    init {
        exRecycler.setHasFixedSize(true)
        exRecycler.adapter = menuAdapter
        exRecycler.layoutManager = LinearLayoutManager(activity)
    }

    override fun checkUserState() {
        if (UserAccount.isSignedIn()) {
            //
        }
        else {
            //
        }
    }

    override fun clickItem(item: INavMenu.Item) {
        throw UnsupportedOperationException()
    }

    override fun onBackPressed(): Boolean {
        throw UnsupportedOperationException()
    }

    inner class MenuItemAdapter : RecyclerView.Adapter<MenuItemAdapter.MenuViewHolder>()
    {
        override fun getItemCount(): Int = menuItems.size

        override fun onBindViewHolder(holder: MenuViewHolder?, position: Int) {
            holder?.bindView(data[position], position)
        }

        override fun getItemViewType(position: Int): Int {
            return data[position].type.layout
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MenuViewHolder? {
            return MenuViewHolder(inflateView(viewType, parent))
        }

        inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindView(model: MenuItem, pos: Int) {
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

                        itemView.setOnClickListener {
                            selectMenuItem(pos)
                        }
                    }

                    MenuType.GROUP -> {
                        itemView.isEnabled = false

                        var textView = itemView as TextView
                        textView.text = getString(model.title) ?: ""
                    }

                    MenuType.SEPARATOR -> {
                        itemView.isEnabled = false
                    }
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

                    closeDrawer()
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