package com.domker.doctor.base.file

import java.io.File

/**
 * 压缩文件子项
 * Created by wanlipeng on 2020/10/29 7:45 PM
 */
data class ZipFileItem(val file: File, val isFile: Boolean = true) {
    val fileName = file.name
    var compressedSize: Long = 0
    var uncompressedSize: Long = 0
}