package com.domker.app.doctor.detail.component

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domker.app.doctor.data.AppChecker
import com.domker.base.thread.AppExecutors

/**
 * 指定一个app的包名，获取四大组件和相关权限List的ViewModel
 * 新增，检测安装包里面的lib库的信息，相对耗时一些
 * Created by wanlipeng on 2020/6/7 3:19 PM
 */
class ComponentViewModel : ViewModel() {
    // 分别按照类型，每个类型一个独立的LiveData
    private val activityInfoList = MutableLiveData<List<ComponentInfo>>()
    private val serviceInfoList = MutableLiveData<List<ComponentInfo>>()
    private val providerInfoList = MutableLiveData<List<ComponentInfo>>()
    private val receiverInfoList = MutableLiveData<List<ComponentInfo>>()
    private val permissionInfoList = MutableLiveData<List<ComponentInfo>>()

    fun getActivityInfo() = activityInfoList
    fun getServiceInfo() = serviceInfoList
    fun getProviderInfo() = providerInfoList
    fun getReceiverInfo() = receiverInfoList
    fun getPermissionInfo() = permissionInfoList

    /**
     * 更新数据
     * @param appChecker app信息获取器
     * @param appPackageName 指定App的包名
     */
    fun updateData(appChecker: AppChecker, appPackageName: String) {
        AppExecutors.executor.execute {
            activityInfoList.postValue(appChecker.getActivityListInfo(appPackageName))
            serviceInfoList.postValue(appChecker.getServiceListInfo(appPackageName))
            providerInfoList.postValue(appChecker.getProvidersListInfo(appPackageName))
            receiverInfoList.postValue(appChecker.getReceiversListInfo(appPackageName))
            permissionInfoList.postValue(appChecker.getPermissions(appPackageName))
        }
    }
}