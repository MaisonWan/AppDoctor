package com.domker.app.doctor.entiy

/**
 * App信息按行显示时
 */
data class AppItemInfo(val subject: String?, val content: String, val extra: String = "", var type: Int = TYPE_SUBJECT_CONTENT) {
    companion object {
        /**
         * 有标题和内容的Item
         */
        const val TYPE_SUBJECT_CONTENT = 1

        /**
         * 只展示标题
         */
        const val TYPE_SUBJECT = 2

        /**
         * 包体积专用
         */
        const val TYPE_PACKAGE = 4

        /**
         * 签名专用类型
         */
        const val TYPE_SIGNATURE = 5
    }

    /**
     * 签名组合
     */
    var signature: Map<String, Array<String>>? = null

    /**
     * 按照指定类型，获取该类型的签名
     */
    fun getWarpSignature(type: String): String {
        return getShowSignature(signature?.get(type)!!)
    }

    /**
     * 根据app的签名，做进一步的数据格式的整理
     */
    private fun getShowSignature(array: Array<String>): String {
        val ans = StringBuffer()
        for (i in array.indices) {
            ans.append(array[i])
            if (i != array.size - 1) {
                ans.append("\n")
            }
        }
        return ans.toString()
    }
}

/**
 * 使用展示的KV来初始化，并且会自动判断Value的空值为默认的NONE
 */
fun appItemOf(key: String, value: String?): AppItemInfo = AppItemInfo(key, value ?: "NONE")