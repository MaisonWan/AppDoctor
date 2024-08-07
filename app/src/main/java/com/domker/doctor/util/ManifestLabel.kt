package com.domker.doctor.util

import android.content.pm.ActivityInfo
import android.content.pm.ProviderInfo
import android.content.pm.ServiceInfo
import android.os.Build
import android.view.WindowManager.LayoutParams

/**
 * Manifest内部的一些值的解读
 * Created by wanlipeng on 2020/7/10 3:26 PM
 */
object ManifestLabel {

    /**
     * int类型10进制和16进制展示
     */
    fun intToHex(n: Int): String {
        return "$n (0x${Integer.toHexString(n)})"
    }

    /**
     * 屏幕方向的展示内容
     */
    fun screen(flags: Int): String {
        return when (flags) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE -> "SCREEN_ORIENTATION_LANDSCAPE"
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT -> "SCREEN_ORIENTATION_PORTRAIT"
            ActivityInfo.SCREEN_ORIENTATION_USER -> "SCREEN_ORIENTATION_USER"
            ActivityInfo.SCREEN_ORIENTATION_BEHIND -> "SCREEN_ORIENTATION_BEHIND"
            ActivityInfo.SCREEN_ORIENTATION_SENSOR -> "SCREEN_ORIENTATION_SENSOR"
            ActivityInfo.SCREEN_ORIENTATION_NOSENSOR -> "SCREEN_ORIENTATION_NOSENSOR"
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE -> "SCREEN_ORIENTATION_SENSOR_LANDSCAPE"
            ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT -> "SCREEN_ORIENTATION_SENSOR_PORTRAIT"
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE -> "SCREEN_ORIENTATION_REVERSE_LANDSCAPE"
            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT -> "SCREEN_ORIENTATION_REVERSE_PORTRAIT"
            ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR -> "SCREEN_ORIENTATION_FULL_SENSOR"
            ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE -> "SCREEN_ORIENTATION_USER_LANDSCAPE"
            ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT -> "SCREEN_ORIENTATION_USER_PORTRAIT"
            ActivityInfo.SCREEN_ORIENTATION_FULL_USER -> "SCREEN_ORIENTATION_FULL_USER"
            ActivityInfo.SCREEN_ORIENTATION_LOCKED -> "SCREEN_ORIENTATION_LOCKED"
            else -> "SCREEN_ORIENTATION_UNSPECIFIED"
        }
    }

    /**
     * 输入法
     */
    fun inputMethod(flags: Int): String {
        if (flags == LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
            return "SOFT_INPUT_STATE_UNSPECIFIED"
        }
        var result = when (LayoutParams.SOFT_INPUT_MASK_STATE.and(flags)) {
            LayoutParams.SOFT_INPUT_STATE_UNCHANGED -> "SOFT_INPUT_STATE_UNCHANGED"
            LayoutParams.SOFT_INPUT_STATE_HIDDEN -> "SOFT_INPUT_STATE_HIDDEN"
            LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN -> "SOFT_INPUT_STATE_ALWAYS_HIDDEN"
            LayoutParams.SOFT_INPUT_STATE_VISIBLE -> "SOFT_INPUT_STATE_VISIBLE"
            LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE -> "SOFT_INPUT_STATE_ALWAYS_VISIBLE"
            else -> "SOFT_INPUT_STATE_UNSPECIFIED"
        }
        result += "\n"
        result += when (LayoutParams.SOFT_INPUT_MASK_ADJUST.and(flags)) {
            LayoutParams.SOFT_INPUT_ADJUST_RESIZE -> "SOFT_INPUT_ADJUST_RESIZE"
            LayoutParams.SOFT_INPUT_ADJUST_PAN -> "SOFT_INPUT_ADJUST_PAN"
            LayoutParams.SOFT_INPUT_ADJUST_NOTHING -> "SOFT_INPUT_ADJUST_NOTHING"
            LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION -> "SOFT_INPUT_IS_FORWARD_NAVIGATION"
            else -> "SOFT_INPUT_ADJUST_UNSPECIFIED"
        }
        return result
    }

    /**
     * 配置
     */
    fun config(flags: Int): String {
        val result = StringBuffer()
        val configValue = intArrayOf(
                ActivityInfo.CONFIG_MCC,
                ActivityInfo.CONFIG_MNC,
                ActivityInfo.CONFIG_LOCALE,
                ActivityInfo.CONFIG_TOUCHSCREEN,
                ActivityInfo.CONFIG_KEYBOARD,
                ActivityInfo.CONFIG_KEYBOARD_HIDDEN,
                ActivityInfo.CONFIG_NAVIGATION,
                ActivityInfo.CONFIG_ORIENTATION,
                ActivityInfo.CONFIG_SCREEN_LAYOUT,
                ActivityInfo.CONFIG_UI_MODE,
                ActivityInfo.CONFIG_SCREEN_SIZE,
                ActivityInfo.CONFIG_SMALLEST_SCREEN_SIZE,
                ActivityInfo.CONFIG_DENSITY,
                ActivityInfo.CONFIG_LAYOUT_DIRECTION,
                ActivityInfo.CONFIG_FONT_SCALE)
        val configName = arrayOf("CONFIG_MCC",
                "CONFIG_MNC", "CONFIG_LOCALE", "CONFIG_TOUCHSCREEN", "CONFIG_KEYBOARD",
                "CONFIG_KEYBOARD_HIDDEN", "CONFIG_NAVIGATION", "CONFIG_ORIENTATION", "CONFIG_SCREEN_LAYOUT",
                "CONFIG_UI_MODE", "CONFIG_SCREEN_SIZE", "CONFIG_SMALLEST_SCREEN_SIZE", "CONFIG_DENSITY",
                "CONFIG_LAYOUT_DIRECTION", "CONFIG_FONT_SCALE")
        result.append(convertMask(flags, configValue, configName))
        if (Build.VERSION.SDK_INT >= 26 && ActivityInfo.CONFIG_COLOR_MODE.and(flags) != 0) {
            result.append("CONFIG_COLOR_MODE").append("\n")
        }
        return checkNone(result.toString())
    }

    /**
     * Service内部的Flag
     */
    fun serviceFlag(flags: Int): String {
        val values = intArrayOf(ServiceInfo.FLAG_STOP_WITH_TASK,
                ServiceInfo.FLAG_ISOLATED_PROCESS,
                ServiceInfo.FLAG_SINGLE_USER)
        val names = arrayOf("FLAG_STOP_WITH_TASK", "FLAG_ISOLATED_PROCESS", "FLAG_SINGLE_USER")
        val s = convertMask(flags, values, names)
        return checkNone(s)
    }

    /**
     * Activity的flag解读
     */
    fun activityFlag(flags: Int): String {
        val values = intArrayOf(ActivityInfo.FLAG_MULTIPROCESS,
                ActivityInfo.FLAG_FINISH_ON_TASK_LAUNCH,
                ActivityInfo.FLAG_CLEAR_TASK_ON_LAUNCH,
                ActivityInfo.FLAG_ALWAYS_RETAIN_TASK_STATE,
                ActivityInfo.FLAG_STATE_NOT_NEEDED,
                ActivityInfo.FLAG_EXCLUDE_FROM_RECENTS,
                ActivityInfo.FLAG_ALLOW_TASK_REPARENTING,
                ActivityInfo.FLAG_NO_HISTORY,
                ActivityInfo.FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS,
                ActivityInfo.FLAG_HARDWARE_ACCELERATED,
                ActivityInfo.FLAG_SINGLE_USER)
        val names = arrayOf("FLAG_MULTIPROCESS",
                "FLAG_FINISH_ON_TASK_LAUNCH",
                "FLAG_CLEAR_TASK_ON_LAUNCH",
                "FLAG_ALWAYS_RETAIN_TASK_STATE",
                "FLAG_STATE_NOT_NEEDED",
                "FLAG_EXCLUDE_FROM_RECENTS",
                "FLAG_ALLOW_TASK_REPARENTING",
                "FLAG_NO_HISTORY",
                "FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS",
                "FLAG_HARDWARE_ACCELERATED",
                "FLAG_SINGLE_USER")
        return checkNone(convertMask(flags, values, names))
    }

    /**
     * Provider的信息
     */
    fun providerFlag(flags: Int): String {
        val s = convertMask(flags, intArrayOf(ProviderInfo.FLAG_SINGLE_USER), arrayOf("FLAG_SINGLE_USER"))
        return checkNone(s)
    }

    private fun checkNone(s: String): String {
        if (s.isBlank()) {
            return "NONE"
        }
        return if (s.endsWith("\n")) {
            s.substring(0, s.length - 1)
        } else {
            s
        }
    }

    /**
     * 通过bits标记位，对应的产出展示名称，外加换行符
     */
    private fun convertMask(flags: Int, values: IntArray, names: Array<String>): String {
        val result = StringBuffer()
        values.forEachIndexed { index, i ->
            if (flags.and(i) != 0) {
                result.append(names[index]).append("\n")
            }
        }
        return result.toString()
    }
}