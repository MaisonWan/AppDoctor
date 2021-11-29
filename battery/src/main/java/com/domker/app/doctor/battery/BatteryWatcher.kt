package com.domker.app.doctor.battery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

/**
 * 电池状态监听器
 * Created by wanlipeng on 2021/10/28 11:38 上午
 */
class BatteryWatcher(context: Context) {
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

    init {
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(receiver, intentFilter)
    }

    /**
     * 初始化的接口
     */
    fun init(owner: Fragment, listener: (BatteryInfo) -> Unit) {
        batteryViewModel = ViewModelProvider(owner).get(BatteryViewModel::class.java)
        batteryViewModel.batteryInfo.observe(owner) {
            listener(batteryInfo)
        }
    }

    /**
     * 解析电池信息内容
     */
    private fun parserBattery(intent: Intent) {
        batteryInfo.status = intent.getIntExtra("status", 0)
        batteryInfo.health = intent.getIntExtra("health", 0)
        batteryInfo.present = intent.getBooleanExtra("present", false)
        batteryInfo.level = intent.getIntExtra("level", 0)
        batteryInfo.scale = intent.getIntExtra("scale", 0)
        batteryInfo.iconSmall = intent.getIntExtra("icon-small", 0)
        batteryInfo.plugged = intent.getIntExtra("plugged", 0)
        batteryInfo.voltage = intent.getIntExtra("voltage", 0)
        batteryInfo.temperature = intent.getIntExtra("temperature", 0)
        batteryInfo.technology = intent.getStringExtra("technology") ?: ""
    }
}