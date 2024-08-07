package com.domker.doctor.util

import com.domker.doctor.base.file.ZipFileItem
import com.domker.doctor.base.readZipFileItemList
import com.domker.doctor.base.readZipFileList
import java.io.File

/**
 * APK安装包内部浏览操作，检测是否包含什么元素，指定目录
 */
class ApkViewer(private val apkSourcePath: String) {

    fun getAllFilePath() {
        val file = File(apkSourcePath)
        file.readZipFileList().filter {
            it.file.absolutePath.startsWith("/lib/")
        }.forEach {
            println(it.file.absolutePath)
        }
    }

    /**
     * 获取Lib文件夹下面的类库
     */
    fun getLibFiles(): Map<String, List<ZipFileItem>> {
        val map = mutableMapOf<String, MutableList<ZipFileItem>>()

        val file = File(apkSourcePath)
        file.readZipFileItemList().filter {
            it.isFile && it.file.absolutePath.startsWith("/lib/")
        }.forEach { item ->
            item.file.parentFile?.let {
                val cpuTypeName = it.name
                map[cpuTypeName] = (map[cpuTypeName] ?: mutableListOf()).also { list ->
                    list.add(item)
                }
            }
        }
        return map
    }
}