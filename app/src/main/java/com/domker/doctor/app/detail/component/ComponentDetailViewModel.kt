package com.domker.doctor.app.detail.component

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domker.doctor.base.toChinese
import com.domker.doctor.app.data.AppChecker
import com.domker.doctor.app.data.AppCheckFactory
import com.domker.doctor.app.entiy.AppItemInfo
import com.domker.doctor.util.ManifestLabel
import kotlinx.coroutines.launch

/**
 * App的四大组件具体的详情，比如单独查看某个Activity的详情
 * Created by wanlipeng on 2020/6/17 6:28 PM
 */
class ComponentDetailViewModel : ViewModel() {
    // LiveData
    private val liveData: MutableLiveData<Detail> = MutableLiveData()

    /**
     * 根据当前组件的类型，来获取相关的数据
     */
    fun updateData(c: ComponentInfo) {
        val checker = AppCheckFactory.instance.checker
        // 异步操作
//        AppExecutors.executor.execute {
//
//        }
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
            fetchData(c, checker)
        }
    }

    private fun fetchData(c: ComponentInfo, checker: AppChecker) {
        val packageName = c.packageName!!
        val name = c.name!!
        // 根据当前的类型，分别获取原始数据
        val originalData = when (c.type) {
            ComponentInfo.TYPE_ACTIVITY -> checker.getActivityInfo(packageName, name)
            ComponentInfo.TYPE_SERVICE -> checker.getServiceInfo(packageName, name)
            ComponentInfo.TYPE_PROVIDER -> checker.getProviderInfo(packageName, name)
            ComponentInfo.TYPE_RECEIVER -> checker.getReceiverInfo(packageName, name)
            else -> checker.getActivityInfo(c.processName!!, name)
        }
        val detail = Detail(warpDetailInfo(originalData))
        detail.icon = originalData.icon
        detail.name = originalData.name
        this.liveData.postValue(detail)
    }

    /**
     * 整合系统原始数据，然后按照需要的信息展示
     */
    private fun warpDetailInfo(info: ComponentInfo): List<AppItemInfo> {
        val itemList: MutableList<AppItemInfo> = mutableListOf()
        itemList.add(AppItemInfo("组件名称", info.name ?: "NONE"))
        itemList.add(AppItemInfo("进程名", info.processName ?: "NONE"))

        // 按照不同类型，分别展开每个类型的数据
        when (info.type) {
            ComponentInfo.TYPE_ACTIVITY -> warpActivityData(info, itemList)
            ComponentInfo.TYPE_SERVICE -> warpServiceData(info, itemList)
            ComponentInfo.TYPE_PROVIDER -> warpProviderData(info, itemList)
            ComponentInfo.TYPE_RECEIVER -> warpReceiverData(info, itemList)
        }
        itemList.add(AppItemInfo("Enable", info.enabled.toChinese()))
        itemList.add(AppItemInfo("Export", info.exported.toChinese()))
        return itemList
    }

    private fun warpActivityData(it: ComponentInfo, detailList: MutableList<AppItemInfo>) {
        detailList.add(AppItemInfo("屏幕方向", ManifestLabel.screen(it.screenOrientation), ManifestLabel.intToHex(it.screenOrientation)))
        detailList.add(AppItemInfo("任务栈名称", it.taskAffinity ?: "NONE"))
        detailList.add(AppItemInfo("输入法模式", ManifestLabel.inputMethod(it.softInputMode), ManifestLabel.intToHex(it.softInputMode)))
        detailList.add(AppItemInfo("Config", ManifestLabel.config(it.configChanges), ManifestLabel.intToHex(it.configChanges)))
        detailList.add(AppItemInfo("Flag", ManifestLabel.activityFlag(it.flags), ManifestLabel.intToHex(it.flags)))
        detailList.add(AppItemInfo("启动模式", it.launchMode.toString()))
    }

    private fun warpServiceData(it: ComponentInfo, detailList: MutableList<AppItemInfo>) {
        detailList.add(AppItemInfo("权限", it.permission.toChinese()))
        detailList.add(AppItemInfo("Flag", ManifestLabel.activityFlag(it.flags), ManifestLabel.intToHex(it.flags)))
    }

    private fun warpProviderData(it: ComponentInfo, detailList: MutableList<AppItemInfo>) {
        detailList.add(AppItemInfo("Flag", ManifestLabel.providerFlag(it.flags), ManifestLabel.intToHex(it.flags)))
    }

    private fun warpReceiverData(it: ComponentInfo, detailList: MutableList<AppItemInfo>) {
        detailList.add(AppItemInfo("任务栈名称", it.taskAffinity!!))
        detailList.add(AppItemInfo("Config", ManifestLabel.config(it.configChanges), ManifestLabel.intToHex(it.configChanges)))
        detailList.add(AppItemInfo("Flag", ManifestLabel.activityFlag(it.flags), ManifestLabel.intToHex(it.flags)))
    }

    /**
     * 得到详细信息的LiveData
     */
    fun getDetail(): MutableLiveData<Detail> {
        return liveData
    }

    /**
     * 数据类
     */
    data class Detail(val itemList: List<AppItemInfo>) {
        var icon: Drawable? = null
        var name: String? = null
    }
}