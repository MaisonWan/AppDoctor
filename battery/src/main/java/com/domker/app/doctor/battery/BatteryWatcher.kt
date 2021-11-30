package com.domker.app.doctor.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * 电池状态监听器
 * Created by wanlipeng on 2021/10/28 11:38 上午
 */
class BatteryWatcher(private val context: Context) {
    private val batteryInfo = BatteryInfo()
    private lateinit var batteryViewModel: BatteryViewModel


    // 广播接收器
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_BATTERY_CHANGED) {
                parserBattery(intent)
                batteryViewModel.batteryInfo.postValue(batteryInfo)
            }
        }

    }

    /**
     * 初始化的接口
     */
    fun register(owner: Fragment, listener: (BatteryInfo) -> Unit) {
        // 注册广播
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW)
        context.registerReceiver(receiver, intentFilter)

        // 注册监听器
        batteryViewModel = ViewModelProvider(owner).get(BatteryViewModel::class.java)
        batteryViewModel.batteryInfo.observe(owner) {
            listener(batteryInfo)
        }
    }

    /**
     * 反注册监听器
     */
    fun unregister(owner: Fragment) {
        context.unregisterReceiver(receiver)
        batteryViewModel.batteryInfo.removeObservers(owner)
    }

    /**
     * 解析电池信息内容
     */
    private fun parserBattery(intent: Intent) {
        batteryInfo.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0)
        batteryInfo.health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
        batteryInfo.present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)
        batteryInfo.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        batteryInfo.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
        batteryInfo.iconSmall = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0)
        batteryInfo.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        batteryInfo.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        batteryInfo.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
        batteryInfo.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: ""
        batteryInfo.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: ""
        if (Build.VERSION.SDK_INT >= 28) {
            batteryInfo.batteryLow = intent.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false)
        }
    }
}