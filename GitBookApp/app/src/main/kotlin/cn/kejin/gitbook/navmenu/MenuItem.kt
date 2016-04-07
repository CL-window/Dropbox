package cn.kejin.gitbook.navmenu

import cn.kejin.gitbook.R

/**
 * Author: Kejin ( Liang Ke Jin )
 * Date: 2016/4/7
 */
/**
 * A Navigation Menu Item
 */
class MenuItem(val type: Type, var title: Int, var icon: Int, var target: Any?) {

    constructor() : this(Type.SEPARATOR, 0, 0, null)
    constructor(t: Int): this(Type.GROUP, t, 0, null)
    constructor(t: Int, ic: Int, tar: Any) : this(Type.ITEM, t, ic, tar)

    /**
     * 三种类型
     */
    enum class Type(val layout: Int) {
        ITEM(R.layout.layout_menu_normal),
        GROUP(R.layout.layout_menu_subheader),
        SEPARATOR(R.layout.layout_menu_separator)
    }

    val selectable: Boolean
        get() = type == Type.ITEM
}
