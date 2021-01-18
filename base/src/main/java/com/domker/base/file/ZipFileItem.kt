package com.domker.base.file

import java.io.File

/**
 * 压缩文件子项
 * Created by wanlipeng on 2020/10/29 7:45 PM
 */
data class ZipFileItem(val file: File, val isFile: Boolean = true)