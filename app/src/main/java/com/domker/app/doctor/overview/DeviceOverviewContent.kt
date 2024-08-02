package com.domker.app.doctor.overview

import com.domker.app.doctor.R
import com.domker.app.doctor.overview.DeviceItem.Companion.LAYOOUT_LONG
import com.domker.app.doctor.overview.DeviceItem.Companion.create

/**
 * 功能阅览界面中，展示项目数据生成
 */
object DeviceOverviewContent {

    /**
     * 展示设备信息列表
     */
    val ITEMS: MutableList<DeviceItem> = mutableListOf()

    val ITEM_MAP: MutableMap<Int, DeviceItem> = hashMapOf()

    private fun addItem(item: DeviceItem) {
        ITEMS.add(item)
        ITEM_MAP[item.typeId] = item
    }

    fun makeContent(): MutableList<DeviceItem> {
        ITEMS.clear()
        ITEM_MAP.clear()

        addItem(create(DeviceItem.DEVICE_TYPE_MODEL, R.layout.layout_device_item_updown))
        addItem(create(DeviceItem.DEVICE_TYPE_SYSTEM, R.layout.layout_device_item_updown))
        addItem(create(DeviceItem.DEVICE_TYPE_BATTERY, R.layout.layout_device_item_updown, LAYOOUT_LONG))
        addItem(create(DeviceItem.DEVICE_TYPE_SCREEN, R.layout.layout_device_item_updown))
        addItem(create(DeviceItem.DEVICE_TYPE_SENSOR, R.layout.layout_device_item_updown))

        return ITEMS
    }

}