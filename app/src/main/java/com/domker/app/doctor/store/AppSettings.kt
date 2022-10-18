package com.domker.app.doctor.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * app配置sp文件的名字
 */
const val APP_SETTINGS_SP_NAME = "app_settings"

// 最前面的初始化，挂在到Context上
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = APP_SETTINGS_SP_NAME)

/**
 *
 * Created by wanlipeng on 2022/2/11 3:06 下午
 */
object AppSettings {
    /**
     * 是否包含所有的app
     */
    private val includeAllAppKey = booleanPreferencesKey("include_all_app")

    /**
     * App首页默认启动的样式
     */
    private val appListStyleKey = stringPreferencesKey("app_list_mode")

    /**
     * 启动阶段Landing页面索引
     */
    private val launchPageIndexKey = intPreferencesKey("launch_index")

    /**
     * 是否包含所有的app
     */
    fun getLaunchSetting(context: Context): Flow<LaunchSetting> {
        return context.settingsDataStore.data.map { p ->
            LaunchSetting().also { setting ->
                setting.includeAllApp = p[includeAllAppKey] ?: false
                setting.appListStyle = p[appListStyleKey] ?: APP_LIST_STYLE_LIST
                setting.launchPageIndex = p[launchPageIndexKey] ?: 0
            }
        }
    }

    /**
     * 存储当前是否包含所有app的值到sp里面
     */
    suspend fun setIncludeAllApp(context: Context, include: Boolean) {
        context.settingsDataStore.edit {
            it[includeAllAppKey] = include
        }
    }

    /**
     * 设置启动时候app界面的样式
     */
    suspend fun setAppListStyle(context: Context, mode: String) {
        context.settingsDataStore.edit {
            it[appListStyleKey] = mode
        }
    }

    /**
     * 启动阶段Landing的页面索引
     */
    suspend fun setLaunchPageIndex(context: Context, index: Int) {
        context.settingsDataStore.edit {
            it[launchPageIndexKey] = index
        }
    }
}