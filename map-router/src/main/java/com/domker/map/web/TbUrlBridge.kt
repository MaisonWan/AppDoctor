package com.domker.map.web

import android.app.Activity

/**
 * 所有需要对WebView提供url参数支持的，都统一在这里配置
 *
 * @author zhaoxianlie
 */
object TbUrlBridge {
    @JvmStatic
	fun overrideUrl(activity: Activity, url: String): Boolean {
        // 关闭webview
        if (url.contains("jump=closewebview")) {
            activity.finish()
        }
        return false
    }
}