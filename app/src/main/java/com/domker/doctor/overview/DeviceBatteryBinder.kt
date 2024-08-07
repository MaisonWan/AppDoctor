package com.domker.doctor.overview

import androidx.fragment.app.Fragment
import com.domker.doctor.R
import com.domker.doctor.battery.BatteryConvert
import com.domker.doctor.battery.BatteryWatcher

/**
 * 电池信息绑定
 */
class DeviceBatteryBinder(private val fragment: Fragment) : DeviceBinder {

    private val batteryWatcher by lazy { BatteryWatcher(fragment.requireContext()) }

    override fun bind(holder: OverviewRecyclerViewAdapter.ViewHolder, item: DeviceItem) {
        holder.iconView?.setImageResource(R.drawable.ic_baseline_battery_charging_full_24)

        batteryWatcher.register(fragment) { battery ->
            // 获取电量
            val charge = battery.level * 100 / battery.scale
            holder.chargeTextView?.text = "${charge}%"
            holder.progressBar?.progress = charge

            // 电压和温度
            val vol = fragment.getString(R.string.battery_voltage_format, battery.voltage)
            val temp =
                fragment.getString(R.string.battery_temperature_format, battery.temperature / 10f)

            val state = fragment.getString(BatteryConvert.getBatteryState(battery))
            holder.secondaryTextView?.text = "容量：${vol}，温度：${temp}, $state"
        }
    }

}