package com.domker.map.web

import android.app.Activity
import android.text.TextUtils
import android.widget.Toast

/**
 * 所有WebView对外公开的JavascriptInterface都在这里统一进行维护
 *
 * @mark 必须在proguard.cfg文件中进行如下配置： <br></br>
 * -keep public class com.baidufe.libs.TbJsBridge { *;}
 *
 * @author zhaoxianlie
 */
class TbJsBridge(private val mActivity: Activity) {
    /**
     * 关闭WebView页面 接口像这样写就可以了，参数什么的，随便定义（just a interface with FE）
     *
     * @param str
     */
    fun closePage(str: String?) {
        if (!TextUtils.isEmpty(str)) {
            Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show()
        }
        mActivity.finish()
    }
}