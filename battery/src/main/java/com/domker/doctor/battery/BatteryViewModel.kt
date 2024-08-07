package com.domker.doctor.battery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * Created by wanlipeng on 2021/11/3 3:27 下午
 */
class BatteryViewModel : ViewModel() {

    // 电池的信息
    val batteryInfo = MutableLiveData<BatteryInfo>()
}
