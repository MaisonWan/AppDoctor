package com.domker.app.doctor.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wanlipeng on 2018/2/6.
 */
object DateUtil {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun getDate(date: Date): String = dateFormat.format(date)

    fun getDataFromTimestamp(timestamp: Long): String = getDate(Date(timestamp))
}