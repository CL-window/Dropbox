package cn.kejin.gitbook.navmenu

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/7
 */
/**
 * MainActivity Navigation Menu 的控制接口
 */
interface INavMenu {

    object Item {
        const val dashboard = 0x01
        const val explore   = 0x02
        const val topics    = 0x03

        const val settings  = 0x11
        const val about     = 0x12
    }

    /**
     * 重新检查用户登录状态, 根据用户状态配置 nav header
     */
    fun checkUserState()

    /**
     * 点击了一个 item
     */
    fun clickItem(item: Item)

    /**
     * 如果当前的页面不是 explore, 则先返回到这个页面
     * @return Boolean
     */
    fun onBackPressed(): Boolean
}