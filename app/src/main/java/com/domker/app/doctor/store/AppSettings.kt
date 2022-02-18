package com.domker.app.doctor.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 最前面的初始化，挂在到Context上
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

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
     * 启动阶段展示菜单
     */
    private val launchMenuStyleKey = intPreferencesKey("launch_menu_style")

    /**
     * 是否包含所有的app
     */
    fun getLaunchSetting(context: Context): Flow<LaunchSetting> {
        return context.settingsDataStore.data.map { p ->
            LaunchSetting().also { setting ->
                setting.includeAllApp = p[includeAllAppKey] ?: false
                setting.launchMenuStyle = p[launchMenuStyleKey] ?: MENU_STYLE_LIST
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
    suspend fun setLaunchMenuStyle(context: Context, style: Int) {
        context.settingsDataStore.edit {
            it[launchMenuStyleKey] = style
        }
    }
}