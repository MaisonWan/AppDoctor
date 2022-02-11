package com.domker.app.doctor.entiy

import com.domker.app.doctor.db.AppEntity
import com.domker.app.doctor.db.AppSignature
import com.domker.app.doctor.detail.container.DETAIL_TYPE_SUBJECT_CONTENT

/**
 * App信息按行显示时
 */
data class AppItemInfo(
    val subject: String?,
    val content: String,
    val extra: String = "",
    var type: Int = DETAIL_TYPE_SUBJECT_CONTENT
) {
    /**
     * app的信息
     */
    var appEntity: AppEntity? = null

    /**
     * 签名组合
     */
    var signatures: Array<AppSignature>? = null

//    /**
//     * 按照指定类型，获取该类型的签名
//     */
//    fun getWarpSignature(type: String): String {
//        return mergeSignature(signature?.get(type)!!)
//    }
}

/**
 * 根据app的签名，做进一步的数据格式的整理
 */
fun mergeSignature(array: Array<String>): String {
    val ans = StringBuffer()
    for (i in array.indices) {
        ans.append(array[i])
        if (i != array.size - 1) {
            ans.append("\n")
        }
    }
    return ans.toString()
}

/**
 * 使用展示的KV来初始化，并且会自动判断Value的空值为默认的NONE
 */
fun appItemOf(key: String, value: String?): AppItemInfo = AppItemInfo(key, value ?: "NONE")