package com.domker.base.file

import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.DecimalFormat

/**
 * 文件相关操作
 * Created by wanlipeng on 2020/6/11 10:44 AM
 */
object FileUtils {

    /**
     * 获取文件尺寸
     */
    fun size(path: String): Long {
        try {
            val file = File(path)
            if (file.exists()) {
                val fis = FileInputStream(file)
                return fis.available().toLong()
            }
        } catch (e: Exception) {
        }
        return 0
    }

    /**
     * 格式化展示文件尺寸
     */
    fun formatFileSize(fileS: Long): String {
        val df = DecimalFormat("#.00")
        val wrongSize = "0B"
        if (fileS == 0L) {
            return wrongSize
        }
        return when {
            fileS < 1024 -> df.format(fileS.toDouble()).toString() + "B"
            fileS < 1048576 -> df.format(fileS.toDouble() / 1024).toString() + "KB"
            fileS < 1073741824 -> df.format(fileS.toDouble() / 1048576).toString() + "MB"
            else -> df.format(fileS.toDouble() / 1073741824).toString() + "GB"
        }
    }

    /**
     * 读取文件中的文本内容
     */
    fun readFile(file: String): String {
        val content = StringBuffer()
        val reader = BufferedReader(InputStreamReader(FileInputStream(file)))
        reader.readLines().forEach {
            content.append(it)
        }
        return content.toString()
    }
}