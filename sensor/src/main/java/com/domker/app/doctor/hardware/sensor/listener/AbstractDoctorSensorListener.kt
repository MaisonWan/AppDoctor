package com.domker.app.doctor.hardware.sensor.listener

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener


/**
 * 传感器监听类的抽象基类
 */
abstract class AbstractDoctorSensorListener : SensorEventListener {
    // 当前监听的传感器
    private val sensorList = mutableListOf<Sensor>()

    /**
     * 返回需要监听的传感器类型列表
     */
    abstract fun getSensorType(): IntArray

    abstract override fun onSensorChanged(event: SensorEvent?)

    abstract override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int)

    /**
     * 获取当年正在监听的传感器
     */
    fun getCurrentSensors(): List<Sensor> = sensorList
}