package com.domker.doctor.util

import androidx.exifinterface.media.ExifInterface
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by wanlipeng on 2018/2/6.
 */
object DataFormat {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
    private val dateWeekFormat = SimpleDateFormat("yyyy-MM-dd E HH:mm:ss", Locale.CHINA)
    private val decimalFormat = DecimalFormat("#0.0000")

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

    /**
     * 曝光模式
     */
    fun exposureMode(exposure: Short): String {
        return when (exposure) {
            ExifInterface.EXPOSURE_PROGRAM_MANUAL -> "手动模式"
            ExifInterface.EXPOSURE_PROGRAM_NORMAL -> "普通模式"
            ExifInterface.EXPOSURE_PROGRAM_APERTURE_PRIORITY -> "光圈优先"
            ExifInterface.EXPOSURE_PROGRAM_SHUTTER_PRIORITY -> "快门优先"
            ExifInterface.EXPOSURE_PROGRAM_CREATIVE -> "创意模式"
            ExifInterface.EXPOSURE_PROGRAM_ACTION -> "运动模式"
            ExifInterface.EXPOSURE_PROGRAM_PORTRAIT_MODE -> "竖屏模式"
            ExifInterface.EXPOSURE_PROGRAM_LANDSCAPE_MODE -> "横屏模式"
            else -> "未定义"
        }
    }

    /**
     * 测光模式
     */
    fun meteringMode(metering: Short): String {
        return when (metering) {
            ExifInterface.METERING_MODE_AVERAGE -> "评价测光"
            ExifInterface.METERING_MODE_CENTER_WEIGHT_AVERAGE -> "中央重点测光"
            ExifInterface.METERING_MODE_SPOT -> "点测光"
            ExifInterface.METERING_MODE_MULTI_SPOT -> "多点测光"
            ExifInterface.METERING_MODE_PATTERN -> "模式测光"
            ExifInterface.METERING_MODE_PARTIAL -> "局部测光"
            ExifInterface.METERING_MODE_OTHER -> "其它模式"
            else -> "未定义"
        }
    }

    /**
     * 色彩空间
     */
    fun colorSpace(color: Int): String {
        return if (color == ExifInterface.COLOR_SPACE_S_RGB) {
            "sRGB"
        } else {
            "未指定"
        }
    }
}