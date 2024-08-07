package com.domker.doctor.overview

import com.domker.doctor.R
import com.domker.doctor.overview.DeviceItem.Companion.LAYOOUT_LONG
import com.domker.doctor.overview.DeviceItem.Companion.create

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

        val layoutId = R.layout.layout_device_item_horizontal
        addItem(create(DeviceItem.DEVICE_TYPE_MODEL, layoutId))
        addItem(create(DeviceItem.DEVICE_TYPE_SYSTEM, layoutId))
        addItem(
            create(
                DeviceItem.DEVICE_TYPE_BATTERY,
                R.layout.layout_device_item_battery,
                LAYOOUT_LONG
            )
        )
        addItem(create(DeviceItem.DEVICE_TYPE_SCREEN, layoutId))
        addItem(create(DeviceItem.DEVICE_TYPE_SENSOR, layoutId))

        return ITEMS
    }

}