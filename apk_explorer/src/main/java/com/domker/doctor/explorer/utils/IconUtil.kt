package com.domker.doctor.explorer.utils

import com.domker.doctor.base.getExtensionName
import com.domker.doctor.explorer.R
import java.io.File
import java.util.Locale

/**
 * 图标相关处理类
 * Created by wanlipeng on 12/30/20 2:11 PM
 */
object IconUtil {

    /**
     * 通过文件的扩展名判断文件的类型，返回该类型文件的图标资源
     */
    fun justFileIcon(file: File): Int {
        return when (file.getExtensionName().lowercase(Locale.getDefault())) {
            "dex" -> R.drawable.format_app
            "txt", "xml", "properties", "json", "ini" -> R.drawable.format_text
            "png", "jpg", "jpeg", "webp" -> R.drawable.format_picture
            "html", "js" -> R.drawable.format_html
            "zip", "7z" -> R.drawable.format_zip
            "apk" -> R.drawable.format_app
            "chm" -> R.drawable.format_chm
            "pdf" -> R.drawable.format_pdf
            "so" -> R.drawable.format_lock
            "mp3", "ogg", "wav" -> R.drawable.format_music
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