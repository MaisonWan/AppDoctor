package com.domker.app.doctor.main.dashboard

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.data.AppEntity
import com.domker.app.doctor.data.SORT_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dash_board")

/**
 * 看板的上下文
 * Created by wanlipeng on 2/3/21 4:21 PM
 */
class DashboardContext(val fragment: Fragment) {
    // 上下文
    val context = fragment.requireContext()

    // 配置
    val setting = DashboardSetting(context)

    // 数据列表
    var appList = AppCheckFactory.getInstance(context).getAppList()

    /**
     * 重载App List
     */
    fun reloadAppList(includeSystemApp: Boolean): List<AppEntity> {
        appList = AppCheckFactory.getInstance(context).getAppList(includeSystemApp)
        return appList
    }

    /**
     * 看板中的配置
     * Created by wanlipeng on 2/3/21 3:49 PM
     */
    class DashboardSetting(val context: Context) {
        /**
         * 排序类型
         */
        private val keySortType = intPreferencesKey("sort_type")

        /**
         * 是否降序排序
         */
        private val keySortDescending = booleanPreferencesKey("sort_descending")

        /**
         * 是否展示所有的app，包括系统应用
         */
        private val keyShowAllApp = booleanPreferencesKey("show_all_app")

        /**
         * 读取排序的类型
         */
        fun readSortType(): Flow<Int> {
            return context.dataStore.data.map { it[keySortType] ?: SORT_SIZE }
        }

        /**
         * 写入排序类型到存储中
         */
        suspend fun writeSortType(type: Int) {
            context.dataStore.edit {
                it[keySortType] = type
            }
        }

        /**
         * 读取是否是降序排列
         */
        fun readSortDescending(): Flow<Boolean> {
            return context.dataStore.data.map { it[keySortDescending] ?: false }
        }

        /**
         * 是否是降序排列
         */
        suspend fun writeSortDescending(desc: Boolean) {
            context.dataStore.edit {
                it[keySortDescending] = desc
            }
        }

        /**
         * 是否包含所有的app
         */
        fun readShowAllApp(): Flow<Boolean> {
            return context.dataStore.data.map { it[keyShowAllApp] ?: false }
        }

        /**
         * 是否包含全部的app
         */
        suspend fun writeShowAllApp(allApp: Boolean) {
            context.dataStore.edit {
                it[keyShowAllApp] = allApp
            }
        }
    }
}