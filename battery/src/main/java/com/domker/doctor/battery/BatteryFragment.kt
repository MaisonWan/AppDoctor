package com.domker.doctor.battery

import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.domker.doctor.battery.databinding.BatteryFragmentLayoutBinding

/**
 * 电池展示信息的界面
 */
class BatteryFragment : Fragment() {
    private lateinit var binding: BatteryFragmentLayoutBinding
    private lateinit var batteryWatcher: BatteryWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        batteryWatcher = BatteryWatcher(requireContext())

    }

    override fun onResume() {
        super.onResume()
        batteryWatcher.register(this) { battery ->
            binding.batteryChargeView.charge = battery.level * 100 / battery.scale
            updateBatteryState(battery)
            updateBatteryDetail(battery)
            println(battery)
        }
    }

    override fun onStop() {
        super.onStop()
        batteryWatcher.unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BatteryFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * 更新电池的状态信息
     */
    private fun updateBatteryState(batteryInfo: BatteryInfo) {
        val id = when (batteryInfo.status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> R.string.battery_charging
            BatteryManager.BATTERY_STATUS_DISCHARGING, BatteryManager.BATTERY_STATUS_NOT_CHARGING -> R.string.battery_discharging
            BatteryManager.BATTERY_STATUS_FULL -> R.string.battery_charging_full
            else -> R.string.battery_charging_unknown
        }
        binding.textViewState.setText(id)
    }

    /**
     * 更新电池的详情
     */
    private fun updateBatteryDetail(batteryInfo: BatteryInfo) {
        // 充电类型
        val type = when (batteryInfo.plugged) {
            BatteryManager.BATTERY_PLUGGED_USB -> R.string.battery_plugged_usb
            BatteryManager.BATTERY_PLUGGED_AC -> R.string.battery_plugged_ac
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> R.string.battery_plugged_wireless
            else -> R.string.battery_discharging
        }
        binding.textViewPlugged.setText(type)

        // 温度
        binding.textViewTemperature.text =
            getString(R.string.battery_temperature_format, batteryInfo.temperature / 10f)

        // 电池容量
        binding.textViewVoltage.text =
            getString(R.string.battery_voltage_format, batteryInfo.voltage)

        // 健康度
        val healthResId = when (batteryInfo.health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> R.string.battery_health_good
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> R.string.battery_health_overheat
            BatteryManager.BATTERY_HEALTH_DEAD -> R.string.battery_health_dead
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> R.string.battery_health_over_voltage
            BatteryManager.BATTERY_HEALTH_COLD -> R.string.battery_health_cold
            else -> R.string.battery_health_unknown
        }
        binding.textViewHealth.setText(healthResId)
    }
}
