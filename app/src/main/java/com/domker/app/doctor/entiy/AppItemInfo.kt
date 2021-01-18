package com.domker.app.doctor.entiy

/**
 * App信息按行显示时
 */
data class AppItemInfo(val subject: String?, val showLabel: String, val switchShowLabel: String = "", val type: Int = TYPE_SUBJECT_LABEL) {
    companion object {
        const val TYPE_SUBJECT_LABEL = 1
        const val TYPE_SUBJECT = 2
        const val TYPE_LABEL = 3
    }
}
