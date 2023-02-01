package com.domker.app.doctor.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

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

    /**
     * 创建其它应用的页面启动intent
     */
    fun createLaunchActivityIntent(packageName: String, clazzName: String): Intent {
        return Intent().also {
            it.component = ComponentName(packageName, clazzName)
        }
    }

    /**
     * 创建卸载app的Intent
     */
    fun createUninstallAppIntent(packageName: String): Intent {
        val uri: Uri = Uri.parse("package:$packageName")
        return Intent(Intent.ACTION_DELETE, uri)
    }

    /**
     * 创建打开系统设置界面
     */
    fun createOpenSettingIntent(packageName: String): Intent {
        return Intent().also {
            it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            it.data = Uri.fromParts("package", packageName, null)
        }
    }

    /**
     * 打开系统设置的Intent
     */
    fun systemSettings(): Intent {
        return Intent(Settings.ACTION_SETTINGS)
    }
}