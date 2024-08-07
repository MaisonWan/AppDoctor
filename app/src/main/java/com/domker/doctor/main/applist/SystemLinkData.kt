package com.domker.doctor.main.applist

import android.content.Intent
import android.provider.Settings
import com.domker.doctor.R

/**
 * 系统链接跳转的数据类
 */
class SystemLinkData {
    private val list = mutableListOf<SystemLink>()

    /**
     * 添加Item
     */
    fun addItem(name: String, iconId: Int, action: String) {
        val intent = Intent(action)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        list.add(SystemLink(name, iconId, intent))
    }

    /**
     * 返回链接数据
     */
    fun getData(): List<SystemLink> {
        list.clear()
        addItem("系统设置", R.drawable.icon_settings_24, Settings.ACTION_SETTINGS)

        addItem(
            "开发者选项",
            R.drawable.icon_developer_board_24,
            Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
        )

        addItem(
            "设备信息",
            R.drawable.icon_devices_other_24,
            Settings.ACTION_DEVICE_INFO_SETTINGS
        )

        addItem(
            "显示设置",
            R.drawable.icon_display_settings_24,
            Settings.ACTION_DISPLAY_SETTINGS
        )

        addItem(
            "蓝牙设置",
            R.drawable.icon_settings_bluetooth_24,
            Settings.ACTION_BLUETOOTH_SETTINGS
        )

        return list
    }
}