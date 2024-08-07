package com.domker.doctor.app.detail.permission

import android.content.pm.PackageManager
import android.content.res.Resources

/**
 * 权限描述
 * @param name 权限名字
 * @param groupName 权限组名字
 * @param description 权限详细描述
 */
data class PermissionDescription(val name: String, val description: String, val groupName: String = "", val detail: String = "")

/**
 * 从系统中获取权限的详情
 */
fun getPermissionDescriptionFromSystem(pm: PackageManager, resources: Resources, permission: String): PermissionDescription {
    return try {
        // get permission info by name
        val permissionInfo = pm.getPermissionInfo(permission, 0)
        // get permission group
        if (permissionInfo.group != null) {
            val permissionGroupInfo = pm.getPermissionGroupInfo(permissionInfo.group!!, 0)
            PermissionDescription(permission,
                    permissionInfo.loadLabel(pm).toString(),
                    permissionGroupInfo.loadLabel(pm).toString(),
                    permissionInfo.loadDescription(pm)?.toString() ?: "")
        } else {
            PermissionDescription(permission,
                    permissionInfo.loadLabel(pm).toString(),
                    detail = permissionInfo.loadDescription(pm)?.toString() ?: "")
        }
    } catch (e: PackageManager.NameNotFoundException) {
        val desc = getPermissionDetail(resources, permission)
        PermissionDescription(permission, desc)
    }
}

