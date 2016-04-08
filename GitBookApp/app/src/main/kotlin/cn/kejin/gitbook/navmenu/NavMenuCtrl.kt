package cn.kejin.gitbook.navmenu

import android.app.Activity
import android.app.Fragment
import android.content.Intent
import android.graphics.Color
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import cn.kejin.android.views.ExRecyclerView
import cn.kejin.gitbook.*
import cn.kejin.gitbook.common.displayAvatar
import cn.kejin.gitbook.common.error
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/7
 */
/**
 * 控制所有的 Navigation Menu
 */
class NavMenuCtrl(val drawer: DrawerLayout, val activity: Activity) : INavMenu
{

    val menuAdapter = MenuItemAdapter()

    val exRecycler = drawer.findViewById(R.id.navList) as ExRecyclerView

    val navHeader = exRecycler.getHeader()

    var curSelectedItemIndex = INavMenu.Item.explore

    init {
        exRecycler.setHasFixedSize(true)
        exRecycler.adapter = menuAdapter
        exRecycler.layoutManager = LinearLayoutManager(activity)

        checkUserState()
        menuAdapter.selectMenuItem(curSelectedItemIndex)
    }

    /**
     * 检查用户状态, 更新 nav Header
     */
    override fun checkUserState() {
        val loginLayout = navHeader?.findViewById(R.id.loginLayout)
        val userLayout = navHeader?.findViewById(R.id.userLayout)

        val loginBtn = loginLayout?.findViewById(R.id.loginButton)

        if (UserAccount.isSignedIn()) {
            loginLayout?.visibility = View.GONE
            userLayout?.visibility = View.VISIBLE

            loginBtn?.setOnClickListener(null)

            val avatar = userLayout?.findViewById(R.id.avatarImage) as ImageView
            val userName = userLayout?.findViewById(R.id.userName) as TextView
            val userEmail = userLayout?.findViewById(R.id.userEmail) as TextView

            userEmail.text = UserAccount.user.email
            userName.text = "${UserAccount.user.name} ( ${UserAccount.user.username} )"
            displayAvatar(UserAccount.user.urls.avatar, avatar)
        }
        else {
            loginLayout?.visibility = View.VISIBLE
            userLayout?.visibility = View.GONE
            loginBtn?.setOnClickListener {
                activity.startActivity(Intent(activity, SignActivity::class.java))
            }
        }
    }

    override fun clickItem(item: Int) {
        menuAdapter.selectMenuItem(item)
    }

    override fun onBackPressed(): Boolean {
        if (curSelectedItemIndex != INavMenu.Item.explore) {
            menuAdapter.selectMenuItem(INavMenu.Item.explore)
            return true;
        }
        return false
    }

    override fun openDrawer() {
        drawer.openDrawer(GravityCompat.START)
    }

    override fun closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START)
    }

    private fun replaceFragment(fragment: Fragment)
            = activity.fragmentManager.beginTransaction()
            .replace(R.id.fragmentContent, fragment).addToBackStack(fragment.toString()).commit()

    inner class MenuItemAdapter :
            RecyclerView.Adapter<MenuItemAdapter.MenuViewHolder>()
    {
        val data = menuItems

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: MenuViewHolder?, position: Int) {
            holder?.bindView(data[position], position)
        }

        override fun getItemViewType(position: Int): Int {
            return data[position].type.layout
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MenuViewHolder? {
            return MenuViewHolder(activity.layoutInflater.inflate(viewType, parent, false))
        }

        inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            fun bindView(model: MenuItem, pos: Int) {
                when (model.type) {
                    MenuItem.Type.ITEM -> {
                        itemView.isEnabled = true;

                        var iconView = itemView.findViewById(R.id.menuIcon) as ImageView
                        var textView = itemView.findViewById(R.id.menuText) as TextView

                        textView.text = MainApp.string(model.title) ?: ""
                        iconView.setImageResource(model.icon)

                        var bgColor = Color.TRANSPARENT
                        var textColor = MainApp.color(R.color.colorPrimary);

                        if (pos == curSelectedItemIndex) {
                            bgColor = Color.LTGRAY
                            textColor =  MainApp.color(R.color.colorSelected)
                        }

                        itemView.setBackgroundColor(bgColor)
                        iconView.setColorFilter(textColor)
                        textView.setTextColor(textColor)

                        itemView.setOnClickListener {
                            selectMenuItem(pos)
                        }
                    }

                    MenuItem.Type.GROUP -> {
                        itemView.isEnabled = false

                        var textView = itemView as TextView
                        textView.text = MainApp.string(model.title) ?: ""
                    }

                    MenuItem.Type.SEPARATOR -> {
                        itemView.isEnabled = false
                    }
                }
            }
        }

        fun selectMenuItem(pos: Int) {
            if (pos !in 0..data.size-1) {
                error("NavMenu", "invalid menu position")
                return
            }

            val item = data[pos]
            when (item.target) {
                is Fragment-> {
                    replaceFragment(item.target as Fragment)
                    val old = curSelectedItemIndex
                    curSelectedItemIndex = pos
                    if (old != pos) {
                        notifyItemChanged(old)
                    }
                    notifyItemChanged(pos)
                }

                is Class<*>-> {
                    activity.startActivity(Intent(activity, item.target as Class<*>))
                }
            }
        }
    }

}