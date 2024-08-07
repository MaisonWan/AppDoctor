package com.domker.doctor.util

import android.content.Context

/**
 * 系统信息扫描
 *
 * Created by wanlipeng on 2/15/21 1:23 AM
 */
class DeviceScanner(context: Context) {

    /**
     * 获取系统属性列表
     */
    fun getSystemProperties(): List<Pair<String, String>> {
        return ShellCommand().run("getprop", 1000).getResult().map {
            unpackProperty(it)
        }
    }

    /**
     * 解析属性，氛围Key和Value两部分
     */
    fun unpackProperty(line: String): Pair<String, String> {
        var left = line.indexOfFirst { it == '[' } + 1
        var right = line.indexOfFirst { it == ']' }
        val key = line.substring(left, right)

        var value = line.substring(right + 1)
        left = value.indexOfFirst { it == '[' } + 1
        right = value.indexOfFirst { it == ']' }
        value = value.substring(left, right)
        return Pair(key, value)
    }
}