package com.domker.app.doctor.detail.component

import android.content.pm.ActivityInfo
import android.content.pm.PermissionInfo
import android.content.pm.ProviderInfo
import android.content.pm.ServiceInfo
import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 组件的信息
 * Created by wanlipeng on 2020/6/7 3:21 PM
 */
@Parcelize
class ComponentInfo : Parcelable {
    companion object {
        const val TYPE_NONE: Int = 0x0
        const val TYPE_GROUP_TITLE: Int = 0x1

        const val TYPE_ACTIVITY = 0x10
        const val TYPE_SERVICE = 0x11
        const val TYPE_PROVIDER = 0x12
        const val TYPE_RECEIVER = 0x13
        const val TYPE_PERMISSION = 0x14
    }

    var type: Int = TYPE_NONE
    var name: String? = null
    var shortName: String? = null
    var targetActivity: String? = null
    var packageName: String? = null
    var processName: String? = null
    var launchMode: Int = 0
    var exported: Boolean = false
    var enabled: Boolean = false
    var permissionGroup: String? = null
    var permission: String? = null
    var icon: Drawable? = null

    // Activity
    var screenOrientation: Int = 0
    var taskAffinity: String? = null
    var softInputMode: Int = 0
    var flags: Int = 0
    var configChanges: Int = 0
}

fun ComponentInfo.parseFrom(activityInfo: ActivityInfo): ComponentInfo {
    this.name = activityInfo.name
    this.shortName = shortName(activityInfo.name)
    this.packageName = activityInfo.packageName
    this.processName = activityInfo.processName
    this.exported = activityInfo.exported
    this.enabled = activityInfo.enabled
    this.launchMode = activityInfo.launchMode

    this.taskAffinity = activityInfo.taskAffinity
    this.screenOrientation = activityInfo.screenOrientation
    this.softInputMode = activityInfo.softInputMode
    this.flags = activityInfo.flags
    this.configChanges = activityInfo.configChanges
    return this
//    if (activityInfo.metaData != null && activityInfo.metaData.keySet() != null) {
//        activityInfo.metaData.keySet().forEach { key ->
//            println(activityInfo.metaData[key])
//        }
//    }
}

fun ComponentInfo.parseFrom(providerInfo: ProviderInfo): ComponentInfo {
    this.name = providerInfo.name
    this.shortName = shortName(providerInfo.name)
    this.packageName = providerInfo.packageName
    this.processName = providerInfo.processName
    this.exported = providerInfo.exported
    this.flags = providerInfo.flags
    return this
}

fun ComponentInfo.parseFrom(serviceInfo: ServiceInfo): ComponentInfo {
    this.name = serviceInfo.name
    this.shortName = shortName(serviceInfo.name)
    this.packageName = serviceInfo.packageName
    this.processName = serviceInfo.processName
    this.exported = serviceInfo.exported
    this.enabled = serviceInfo.enabled
    this.flags = serviceInfo.flags
    this.permission = serviceInfo.permission
    return this
}

fun ComponentInfo.parseFrom(permissionInfo: PermissionInfo): ComponentInfo {
    this.name = permissionInfo.name
    this.packageName = permissionInfo.packageName
    this.permissionGroup = permissionInfo.group
    return this
}

/**
 * 创建一个Component对象，设定类型
 */
fun componentOfType(type: Int) : ComponentInfo {
    val c = ComponentInfo()
    c.type = type
    return c
}

private fun shortName(name: String): String {
    val index = name.lastIndexOf(".")
    if (index >= 0) {
        return name.substring(IntRange(index + 1, name.length - 1))
    }
    return name
}
