package com.domker.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.util.*

/**
 * 打开不同类型文件的Intent
 * Created by wanlipeng on 2020/12/11 9:26 PM
 */
object FileIntent {

    /**
     * 打开文本文件
     */
    fun createHtmlFileIntent(context: Context, file: File): Intent {
        return createFileIntent(context, "text/html", file)
    }

    /**
     * 打开图片文件
     */
    fun createImageFileIntent(context: Context, file: File): Intent {
        return createFileIntent(context, "image/*", file)
    }

    private fun createFileIntent(context: Context, dataType: String, file: File): Intent {
        val apkUri: Uri = FileProvider.getUriForFile(context, "com.domker.doctor.fileprovider", file)
        val intent = Intent("android.intent.action.VIEW")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(apkUri, dataType)
        return intent
    }

    /**
     * 创建打开文件的Intent
     */
    fun createFileIntent(context: Context, file: File): Intent? {
        return when (file.extension.toLowerCase(Locale.getDefault())) {
            "png", "jpg", "jpeg", "webp" -> createImageFileIntent(context, file)
            "txt", "json", "ini" -> createHtmlFileIntent(context, file)
            else -> null
        }
    }

}