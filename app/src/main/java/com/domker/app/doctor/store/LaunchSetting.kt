package com.domker.app.doctor.store

/**
 * 启动阶段的配置
 * Created by wanlipeng on 2022/2/18 5:04 下午
 */

/**
 * 启动展示界面List风格
 */
const val MENU_STYLE_LIST = 0

/**
 * 启动阶段界面表格风格
 */
const val MENU_STYLE_GRID = 1

/**
 * 启动阶段的Setting
 */
class LaunchSetting {
    /**
     * 是否包含所有的app
     */
    var includeAllApp = false

    /**
     * 启动阶段的样式
     */
    var launchMenuStyle = MENU_STYLE_LIST
}