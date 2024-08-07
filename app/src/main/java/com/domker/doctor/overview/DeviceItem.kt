package com.domker.doctor.overview

/**
 * 设备信息Item里面包含的内容
 */
class DeviceItem {

    /**
     * 布局资源的ID
     */
    var layoutResId: Int = 0

    /**
     * 布局类型的ID，长的代表一行一个View，SHORT代表每行2个View
     */
    var layoutType: Int = LAYOUT_SHORT

    /**
     * 代表当前展示设备信息的类型
     */
    var typeId: Int = DEVICE_TYPE_NORMAL

    /**
     * 展示的内容
     */
    var content: String = ""

    companion object {
        val LAYOUT_SHORT = 1
        val LAYOOUT_LONG = 2


        const val DEVICE_TYPE_NORMAL = 0
        const val DEVICE_TYPE_MODEL = 1
        const val DEVICE_TYPE_SYSTEM = 2
        const val DEVICE_TYPE_SCREEN = 3
        const val DEVICE_TYPE_SENSOR = 4
        const val DEVICE_TYPE_NET = 5
        const val DEVICE_TYPE_BATTERY = 6

        /**
         * 创建实体类
         */
        fun create(
            typeId: Int,
            layoutResId: Int,
            layoutType: Int = LAYOUT_SHORT
        ): DeviceItem {
            return DeviceItem().apply {
                this.typeId = typeId
                this.layoutResId = layoutResId
                this.layoutType = layoutType
            }
        }
    }
}