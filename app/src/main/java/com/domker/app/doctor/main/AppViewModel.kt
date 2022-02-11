package com.domker.app.doctor.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domker.app.doctor.store.AppSettings
import kotlinx.coroutines.flow.collect

/**
 * 全局状态监控，包括全局是否包含所有应用，包括系统应用的状态变化
 * Created by wanlipeng on 3/5/21 2:32 PM
 */
class AppViewModel : ViewModel() {

    /**
     * 是否包含所有的App，false的时候只包含第三方应用
     */
    val includeAllApp = MutableLiveData<Boolean>()

    /**
     * 加载配置
     */
    suspend fun loadSettings(context: Context) {
        AppSettings.getIncludeAllApp(context).collect {
            includeAllApp.postValue(it)
        }
    }
}