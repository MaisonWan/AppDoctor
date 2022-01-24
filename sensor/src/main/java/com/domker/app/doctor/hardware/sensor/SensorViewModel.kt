package com.domker.app.doctor.hardware.sensor

import android.hardware.Sensor
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SensorViewModel(private val sensorWatcher: SensorWatcher) : ViewModel() {
    /**
     * 所有的传感器列表
     */
    val allSensorList = MutableLiveData<List<Sensor>>()

    /**
     * 异步获取传感器数据
     */
    fun updateSensorData() {
        viewModelScope.launch(Dispatchers.Default) {
            val sensorList = sensorWatcher.getAllSensor()
            allSensorList.postValue(sensorList)
            println("current thread name ${Thread.currentThread().name}")
        }
    }

    /**
     * 多参数的工厂类
     */
    class SensorFactory(private val sensorWatcher: SensorWatcher) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SensorViewModel(sensorWatcher) as T
        }

    }
}

