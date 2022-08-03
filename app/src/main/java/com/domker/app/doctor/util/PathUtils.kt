package com.domker.app.doctor.util

import android.os.Environment
import java.io.File

/**
 * 路径工具类
 * Created by wanlipeng on 2022/8/3 14:33
 */
object PathUtils {
    /**
     * 整个应用的根文件夹
     */
    fun appBasePath() = File(Environment.getExternalStorageDirectory(), "AppDoctor")

    /**
     * 导出app的文件夹
     */
    fun exportAppPath() = File(appBasePath(), "App")
}