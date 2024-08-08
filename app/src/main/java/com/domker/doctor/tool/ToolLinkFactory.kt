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
        addItem("图片分析", R.drawable.ic_baseline_gpp_maybe_24, "", R.id.nav_photo_info)

        return list
    }
}