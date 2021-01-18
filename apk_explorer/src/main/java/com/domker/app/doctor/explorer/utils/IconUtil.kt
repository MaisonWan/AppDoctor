package com.domker.app.doctor.explorer.utils

import com.domker.base.getExtensionName
import com.domker.app.doctor.explorer.R
import java.io.File
import java.util.*

/**
 * 图标相关处理类
 * Created by wanlipeng on 12/30/20 2:11 PM
 */
object IconUtil {

    /**
     * 通过文件的扩展名判断文件的类型，返回该类型文件的图标资源
     */
    fun justFileIcon(file: File): Int {
        return when (file.getExtensionName().toLowerCase(Locale.getDefault())) {
            "dex" -> R.drawable.format_app
            "txt", "xml", "properties", "json", "ini" -> R.drawable.format_text
            "png", "jpg", "jpeg", "webp" -> R.drawable.format_picture
            "html" -> R.drawable.format_html
            "zip" -> R.drawable.format_zip
            "apk" -> R.drawable.format_app
            "chm" -> R.drawable.format_chm
            "pdf" -> R.drawable.format_pdf
            "mp3", "ogg" -> R.drawable.format_media
            "xls", "xlsx" -> R.drawable.format_excel
            "doc", "docx" -> R.drawable.format_word
            "ppt", "pptx" -> R.drawable.format_ppt
            else -> R.drawable.format_unkown
        }
    }

    /**
     * 判断是否是图片类型
     */
    fun isImageType(resId: Int): Boolean {
        return resId == R.drawable.format_picture
    }
}