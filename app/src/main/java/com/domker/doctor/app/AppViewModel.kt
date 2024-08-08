package com.domker.doctor.app

import android.content.Context
import androidx.lifecycle.*
import com.domker.doctor.data.store.AppSettings
import com.domker.doctor.data.store.LaunchSetting
import kotlinx.coroutines.launch

/**
 * 全局状态监控，包括全局是否包含所有应用，包括系统应用的状态变化
 * Created by wanlipeng on 3/5/21 2:32 PM
 */
class AppViewModel : ViewModel() {

    /**
     * 是否包含所有的App，false的时候只包含第三方应用
     */
    private val launchSetting = MutableLiveData<LaunchSetting>()

    /**
     * 返回实例，只能监听变化，不能post数据
     */
    fun getLaunchSetting(): LiveData<LaunchSetting> = launchSetting

    /**
     * 加载配置
     */
    fun loadLaunchSettings(context: Context) {
        viewModelScope.launch {
            println(Thread.currentThread().name)
            AppSettings.getLaunchSetting(context).collect {
                launchSetting.postValue(it)
            }
        }
    }

    /**
     * 第一次加载
     */
    fun observeLoadSettingsOnce(lifecycleOwner: LifecycleOwner, func: (LaunchSetting) -> Unit) {
        val observer = object : Observer<LaunchSetting> {
            override fun onChanged(t: LaunchSetting) {
                func(t)
                launchSetting.removeObserver(this)
            }
        }
        launchSetting.observe(lifecycleOwner, observer)
    }

    /**
     * 开关变化的时候，发送异步消息同步
     */
    fun switchChanged(isChecked: Boolean) {
        // 如果还没有值的时候，创新一个新的
        val v = launchSetting.value?.also {
            it.includeAllApp = isChecked
        } ?: LaunchSetting()
        launchSetting.postValue(v)
    }
}