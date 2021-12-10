package com.domker.app.doctor.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

/**
 * 传感器信息数据监控，反馈
 */
class SensorWatcher(context: Context) {
    // 获取传感器服务
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    fun addRegister() {
        val sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL)
        sensorList.forEach {
            println(it.name)
        }
    }
}