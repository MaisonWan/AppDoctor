package com.domker.app.doctor.util

import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wanlipeng on 2018/2/6.
 */
object DataFormat {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    private val dateWeekFormat = SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.CHINA)
    private val decimalFormat = DecimalFormat("#0.00")

    fun getDate(date: Date): String = dateFormat.format(date)

    fun getDataFromTimestamp(timestamp: Long): String = getDate(Date(timestamp))

    fun getAppInstallTime(timestamp: Long): String {
        return if (timestamp == 0L) {
            "系统预装"
        } else {
            getDataFromTimestamp(timestamp)
        }
    }

    /**
     * 展示带星期的全时间
     */
    fun getFormatFullDate(timestamp: Long): String {
        return if (timestamp == 0L) {
            "未知时间"
        } else {
            dateWeekFormat.format(Date(timestamp))
        }
    }

    /**
     * 展示两位小数点
     */
    fun getDoubleShort(n: Double): String {
        return decimalFormat.format(n)
    }
}