package com.domker.doctor.settings

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.domker.base.sp.SettingValue
import com.domker.doctor.store.AppSettings
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.set

/**
 * 当设置里面发生了变化
 * Created by wanlipeng on 2022/8/11 20:58
 */
class SettingsViewModel : ViewModel() {
    private val settingChange = MutableLiveData<SettingValue<*>>()

    // 初始化的配置的值
    private val initSettingValueMap by lazy { mutableMapOf<String, SettingValue<*>>() }

    /**
     * 发生变化的时候，设置的回调
     */
    fun onChange(settingValue: SettingValue<*>) {
        settingChange.postValue(settingValue)
    }

    /**
     * app启动之后，初始化配置的初始值
     */
    fun onInit(allSettingMap: Map<String, *>) {
        allSettingMap.forEach { (t, u) ->
            initSettingValueMap[t] = SettingValue(t, u)
        }
    }

    /**
     * 获取初始化时配置的值，key不正确的情况下有可能返回null
     */
    fun getInitSettingValue(key: String): SettingValue<*>? {
        return initSettingValueMap[key]
    }

    /**
     * 监控App列表样式变化
     */
    fun getAppListStyleSync(context: Context, observer: (String) -> Unit) {
        viewModelScope.launch {
            AppSettings.getAppListStyle(context).collect {
                observer(it)
            }
        }
    }

    /**
     * 监听配置变化时的回调
     */
    fun getSettingChangeData() = settingChange
}