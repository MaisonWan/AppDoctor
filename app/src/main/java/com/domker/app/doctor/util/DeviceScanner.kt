package com.domker.app.doctor.util

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
    fun getSystemProperties(): List<String> {
        return ShellCommand().run("getprop", 1000).getResult()
    }
}