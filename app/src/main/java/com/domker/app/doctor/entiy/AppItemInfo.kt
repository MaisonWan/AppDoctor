package com.domker.app.doctor.entiy

/**
 * App信息按行显示时
 */
data class AppItemInfo(val subject: String?, val showLabel: String, val switchShowLabel: String = "", val type: Int = TYPE_SUBJECT_LABEL) {
    companion object {
        const val TYPE_SUBJECT_LABEL = 1
        const val TYPE_SUBJECT = 2
        const val TYPE_LABEL = 3
        const val TYPE_PACKAGE = 4
    }
}

/**
 * 使用展示的KV来初始化，并且会自动判断Value的空值为默认的NONE
 */
fun appItemOf(key: String, value: String?): AppItemInfo = AppItemInfo(key, value ?: "NONE")