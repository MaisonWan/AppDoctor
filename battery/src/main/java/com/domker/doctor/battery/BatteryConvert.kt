package com.domker.doctor.battery

import android.os.BatteryManager

/**
 * 电池展示换算
 */
object BatteryConvert {

    /**
     * 更新电池的状态信息
     */
    fun getBatteryState(batteryInfo: BatteryInfo): Int {
        return when (batteryInfo.status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> R.string.battery_charging
            BatteryManager.BATTERY_STATUS_DISCHARGING, BatteryManager.BATTERY_STATUS_NOT_CHARGING -> R.string.battery_discharging
            BatteryManager.BATTERY_STATUS_FULL -> R.string.battery_charging_full
            else -> R.string.battery_charging_unknown
        }
    }
}