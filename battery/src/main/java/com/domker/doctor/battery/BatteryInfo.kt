package com.domker.doctor.battery

import android.os.BatteryManager

/**
 * 电池信息实体类
 * Created by wanlipeng on 2021/10/28 11:55 上午
 */
open class BatteryInfo {
    /**
     * 电池状态
     */
    var status: Int = BatteryManager.BATTERY_STATUS_UNKNOWN

    /**
     * 是否是低电源模式
     */
    var batteryLow: Boolean = false

    /**
     * 电池健康度
     */
    var health: Int = BatteryManager.BATTERY_HEALTH_UNKNOWN

    /**
     * 当前是否在充电
     */
    var present: Boolean = false

    /**
     * 电池剩余电量
     */
    var level: Int = 0

    /**
     * 取得电池的总容量，通常为 100
     */
    var scale: Int = 100

    /**
     * 取得电池对应的图标 ID
     */
    var iconSmall: Int = 0

    /**
     * 连接的电源插座类型，返回的状态由 android.os.BatteryManager 类定义的常量所决定，包括：
     * USB 电源（ BATTERY_PLUGGED_USB ）
     * 交流电电源（ BATTERY_PLUGGED_AC ）
     */
    var plugged: Int = BatteryManager.BATTERY_PLUGGED_USB

    /**
     * 取得电池的电压
     */
    var voltage: Int = 0

    /**
     * 取得电池的温度，单位是摄氏度
     */
    var temperature: Int = 0

    /**
     * 取得电池的类型
     */
    var technology: String = ""

    override fun toString(): String {
        return "BatteryInfo(status=$status, health=$health, present=$present, level=$level, scale=$scale, iconSmall=$iconSmall, plugged=$plugged, voltage=$voltage, temperature=$temperature, technology='$technology')"
    }

}