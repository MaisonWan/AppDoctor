package com.domker.doctor.main.photo

/**
 * 展示Exif的属性
 *
 * Created by wanlipeng on 2021/5/18 4:50 下午
 */
data class ExifItem(
    // 分类
    val subject: String,

    // 展示内容
    val content: String,

    // 类型
    val type: Int = TYPE_NORMAL,

    // 扩展
    val expend: Any? = null
)
