package com.domker.doctor.tool

import com.domker.doctor.R

/**
 * 系统链接跳转的数据类
 */
class ToolLinkFactory {
    private val list = mutableListOf<ToolLink>()

    /**
     * 添加Item
     */
    fun addItem(name: String, iconId: Int, desc: String, navId: Int) {
        list.add(ToolLink(iconId, name, desc, navId))
    }

    /**
     * 返回链接数据
     */
    fun getData(): List<ToolLink> {
        list.clear()
        addItem(
            "图片分析",
            R.drawable.ic_menu_gallery,
            "指定一张图片，分析图片Exif信息",
            R.id.nav_photo_info
        )
        addItem(
            "数据统计",
            R.drawable.ic_dashboard_black_24dp,
            "统计系统相关数据",
            R.id.nav_dashboard
        )
        return list
    }
}