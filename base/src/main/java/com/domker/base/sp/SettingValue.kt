package com.domker.base.sp

import android.content.SharedPreferences
import java.lang.Exception

/**
 * 设置的值，包括Key值，泛型
 * Created by wanlipeng on 2022/8/12 16:16
 */
class SettingValue<T>(val key: String, val value: T) {

    fun toBoolean() = value as Boolean

    override fun toString() = value as String

    fun toInt() = value as Int

    fun toLong() = value as Long
}

/**
 * 创建实体类
 */
fun <T> SharedPreferences.settingValueOf(key: String, defaultValue: T): SettingValue<T> {
    return when (defaultValue) {
        is String -> SettingValue(key, this.getString(key, defaultValue) as T)
        is Boolean -> SettingValue(key, this.getBoolean(key, defaultValue) as T)
        is Int -> SettingValue(key, this.getInt(key, defaultValue) as T)
        is Long -> SettingValue(key, this.getLong(key, defaultValue) as T)
        is Float -> SettingValue(key, this.getFloat(key, defaultValue) as T)
        else -> throw Exception("Value Type Error.")
    }
}