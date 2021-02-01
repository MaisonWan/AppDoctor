package com.domker.app.doctor.detail.component

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 四大组件的ViewModel
 * Created by wanlipeng on 2020/6/7 3:19 PM
 */
class ComponentViewModel : ViewModel() {
    private val _activityList = MutableLiveData<List<ComponentInfo>>()
    private val _serviceList = MutableLiveData<List<ComponentInfo>>()
    private val _contentProviderList = MutableLiveData<List<ComponentInfo>>()
    private val _broadcastList = MutableLiveData<List<ComponentInfo>>()
    private val _permissionList = MutableLiveData<List<ComponentInfo>>()

    val activityInfo = _activityList
    val serviceInfo = _serviceList
    val providerInfo = _contentProviderList
    val receiverInfo = _broadcastList
    val permissionInfo = _permissionList
}