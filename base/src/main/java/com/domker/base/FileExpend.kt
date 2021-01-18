package com.domker.base

import android.content.Context
import com.domker.base.file.ZipFileItem
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream

/**
 * 文件方面的扩展方法
 * Created by wanlipeng on 2020/10/25 6:23 PM
 */
fun File.getExtensionName(): String {
    val filename = this.absolutePath
    if (filename.isNotEmpty()) {
        val dot: Int = filename.lastIndexOf('.')
        if (dot > -1 && dot < filename.length - 1) {
            return filename.substring(dot + 1)
        }
    }
    return ""
}


/**
 * 读取压缩文件的列表
 */
fun File.readZipFileList(): List<ZipFileItem> {
    var input: BufferedInputStream? = null
    var zipInputStream: ZipInputStream? = null
    val result = mutableListOf<ZipFileItem>()
    return try {
        input = BufferedInputStream(FileInputStream(this))
        zipInputStream = ZipInputStream(input)
        var zipEntry: ZipEntry? = zipInputStream.nextEntry
        while (zipEntry != null) {
            val item = ZipFileItem(File(zipEntry.name), !zipEntry.isDirectory)
            result.add(item)
            zipEntry = zipInputStream.nextEntry
        }
        result
    } catch (e: Exception) {
        result
    } finally {
        input?.close()
        zipInputStream?.close()
    }
}

/**
 * 解压到临时目录
 */
fun File.unzip(context: Context, file: File): File? {
    var inputStream: InputStream? = null
    var outputStream: BufferedOutputStream? = null
    val zipFile: ZipFile?
    return try {
        val apkFolderName = if (this.name == "base.apk") {
            this.parentFile!!.name
        } else {
            this.name
        }
        val outFile = File(context.cacheDir.absolutePath + File.separator + apkFolderName, file.absolutePath)
        checkPath(outFile)
        zipFile = ZipFile(this)
        val e = zipFile.entries()
        while (e.hasMoreElements()) {
            println(e.nextElement().name)
        }
        // 路径和zip里面的路径的匹配
        val entry = zipFile.getEntry(zipPath(file.absolutePath))
        inputStream = zipFile.getInputStream(entry)

        // 输出
        outputStream = BufferedOutputStream(FileOutputStream(outFile))
        // 解压文件
        unzipFIle(inputStream, outputStream)
        outFile
    } catch (e: Exception) {
        null
    } finally {
        inputStream?.close()
        outputStream?.close()
    }
}

private fun unzipFIle(inputStream: InputStream, outputStream: BufferedOutputStream) {
    val readContent = ByteArray(1024 * 8)
    var readCount = inputStream.read(readContent)
    while (readCount != -1) {
        outputStream.write(readContent, 0, readCount)
        readCount = inputStream.read(readContent)
    }
}

private fun checkPath(file: File) {
    file.parentFile?.apply {
        if (!this.exists()) {
            this.mkdirs()
        }
    }
}

private fun zipPath(path: String): String {
    return if (path.startsWith("/")) {
        path.substring(1)
    } else {
        path
    }
}