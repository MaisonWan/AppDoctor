package com.domker.doctor.hardware.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * 传感器信息数据监控，反馈
 */
class SensorWatcher(context: Context) {
    // 获取传感器服务
    private val sensorManager: SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    // 缓存使用的传感器
    private val sensorMap = mutableMapOf<Int, Sensor>()

    /**
     * 返回所有传感器列表
     */
    fun getAllSensor(): MutableList<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

    fun register(sensorType: Int) {
        if (!sensorMap.containsKey(sensorType)) {
            sensorMap[sensorType] = sensorManager.getDefaultSensor(sensorType)
        }
        val sensor = sensorMap[sensorType]

        sensorManager.registerListener(object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {

            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

            }

        }, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
}