package com.domker.app.doctor.util

import android.content.Context
import android.content.Intent

/**
 * Created by wanlipeng on 2018/2/6.
 */
object IntentUtil {
    const val INTENT_KEY_PACKAGE = "package_name"

    /**
     * 创建启动App的Intent
     */
    fun createLaunchIntent(context: Context, packageName: String): Intent {
        return context.packageManager.getLaunchIntentForPackage(packageName)!!
    }
}