package cn.kejin.gitbook.navmenu

import cn.kejin.gitbook.AboutActivity
import cn.kejin.gitbook.R
import cn.kejin.gitbook.SettingsActivity
import cn.kejin.gitbook.fragments.DashboardFragment
import cn.kejin.gitbook.fragments.ExploreFragment
import cn.kejin.gitbook.fragments.TopicsFragment

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/7
 */
/**
 * MainActivity Navigation Menu 的控制接口
 */
interface INavMenu {

    /**
     * 对应 menuItems 的 index
     */
    object Item {
        const val dashboard = 0x01
        const val explore   = 0x04
        const val topics    = 0x05

        const val settings  = 0x08
        const val about     = 0x09
    }

    val menuItems: List<MenuItem>
        get() = listOf(
            MenuItem(R.string.sub_menu_home),
            MenuItem(R.string.action_dashboard, R.drawable.ic_vector_dashboard_white_48dp, DashboardFragment()),
            MenuItem(),
            MenuItem(R.string.sub_menu_gitbook),
            MenuItem(R.string.action_explore, R.drawable.ic_vector_explore_white_48dp, ExploreFragment()),
            MenuItem(R.string.action_topics, R.drawable.ic_vector_bookmark_white_48dp, TopicsFragment()),
            MenuItem(),
            MenuItem(R.string.sub_menu_more),
            MenuItem(R.string.action_settings, R.drawable.ic_vector_settings_white_48dp, SettingsActivity::class.java),
            MenuItem(R.string.action_about, R.drawable.ic_vector_info_white_48dp, AboutActivity::class.java))

    /**
     * 重新检查用户登录状态, 根据用户状态配置 nav header
     */
    fun checkUserState()

    /**
     * 点击了一个 item
     */
    fun clickItem(item: Int)

    /**
     * 如果当前的页面不是 explore, 则先返回到这个页面
     * @return Boolean
     */
    fun onBackPressed(): Boolean

    /**
     * 打开抽屉
     */
    fun openDrawer()

    /**
     * 关闭抽屉
     */
    fun closeDrawer()
}