package com.domker.app.doctor.explorer

import android.content.Context
import com.domker.base.file.ZipFileItem
import com.domker.app.doctor.explorer.main.ExplorerItem
import java.io.File

/**
 * APK浏览内部逻辑处理
 * Created by wanlipeng on 2020/7/13 7:43 PM
 */
class ApkExplorer(private val context: Context) {
    private val buffer: MutableList<String> = mutableListOf()
    private val apkFileList: MutableList<ZipFileItem> = mutableListOf()

    /**
     * 更新最新的数据，该数据主要是压缩包中所有的文件
     */
    fun updateData(list: List<ZipFileItem>) {
        // 避免自己添加自己的时候
        if (apkFileList != list) {
            apkFileList.clear()
            apkFileList.addAll(list)
        }
    }

    /**
     * 判断数据是否是空
     */
    fun isDataEmpty(): Boolean = apkFileList.isEmpty()

    /**
     * 获取数据
     */
    fun getData(): List<ZipFileItem> = apkFileList

    /**
     * 根据给定的路径，过滤出该目录下面的文件和文件夹
     */
    fun filterItems(folderPath: String): List<ExplorerItem> {
        buffer.clear()
        val items: MutableList<ExplorerItem> = mutableListOf()
        apkFileList.forEach {
            val file = checkNeedInclude(it, folderPath)
            if (file != null) {
                items.add(file)
            }
        }
        return items
    }

    /**
     * zip里面按照路径所有的都列出来，所以判断当年的目录层级是否包含在这个路径上
     */
    private fun checkNeedInclude(zipFile: ZipFileItem, folderPath: String): ExplorerItem? {
        val file = zipFile.file
        println("${file.path} : ${file.parent}")

        // 根目录
        if (folderPath == File.separator) {
            if (file.parent == null) {
                return ExplorerItem(file, zipFile.isFile)
            }
        } else {
            if (file.parent == folderPath) {
                return ExplorerItem(file, zipFile.isFile)
            } else if (file.parent == null) {
                return null
            }
        }
        // 长目录里面发现文件夹
        val folder = if (!folderPath.startsWith(File.separator)) {
            "${File.separator}$folderPath"
        } else {
            folderPath
        }
        var parent = file.parentFile
        var child = file
        // 如果有向上一层遍历那么一定是目录，不是文件
        var isFile = true
        while (parent != null && parent.absolutePath != folder) {
            child = parent
            parent = parent.parentFile
            isFile = false
        }

        if ((folderPath == File.separator || child.absolutePath.startsWith(folder))
                && !buffer.contains(child.absolutePath)) {
            buffer.add(child.absolutePath)
            return ExplorerItem(child, isFile)
        }
        return null
    }

    companion object {

        /**
         * 判断是否是资源文件
         */
        fun isResourceFile(file: File): Boolean {
            return file.name == "resources.arsc"
        }

        /**
         * 是否是Json类型的文件
         */
        fun isJsonFile(file: File): Boolean {
            return file.extension == "json"
        }

        /**
         * 判断是否是AndroidManifest文件
         */
        fun isAndroidManifestFile(file: File): Boolean {
            return file.name == "AndroidManifest.xml"
        }
    }
}