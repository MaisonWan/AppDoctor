package com.domker.app.doctor.data

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.domker.base.RefInvoke
import com.domker.base.file.FileUtils
import org.jetbrains.annotations.NotNull

/**
 *
 * Created by wanlipeng on 2020/6/11 9:53 PM
 */
@Entity(tableName = "app_data")
class AppEntity {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "package_name")
    @NotNull
    var packageName: String = ""

    @ColumnInfo(name = "app_name")
    var appName: String = ""

    @ColumnInfo(name = "update_time")
    var updateTime: Long = 0

    @ColumnInfo(name = "install_time")
    var installTime: Long = 0

    @ColumnInfo(name = "version_code")
    var versionCode: Long = 0

    @ColumnInfo(name = "version_name")
    var versionName: String = ""

    @ColumnInfo(name = "process_name")
    var processName: String? = null

    @Ignore
    var iconDrawable: Drawable? = null

    @ColumnInfo(name = "is_system_app")
    var isSystemApp: Boolean = false

    @ColumnInfo(name = "application_name")
    var applicationName: String? = null

    @ColumnInfo(name = "source_dir")
    var sourceDir: String? = null

    @ColumnInfo(name = "source_apk_size")
    var sourceApkSize: Long? = 0

    @ColumnInfo(name = "signature")
    var signature: String? = null

    @Ignore
    var signatureMap: Map<String, Array<String>>? = null

    @ColumnInfo(name = "native_lib_dir")
    var nativeLibraryDir: String? = null

    @ColumnInfo(name = "data_dir")
    var dataDir: String? = null

    @ColumnInfo(name = "min_sdk_version")
    var minSdkVersion: Int = 0

    @Ignore
    var flag: String? = null

    @Ignore
    var targetSdkVersion = 0

    @Ignore
    var primaryCpuAbi: String? = null

    @Ignore
    var uid = 0

    @Ignore
    var backupAgentName: String? = null

    @Ignore
    var deviceProtectedDataDir: String? = null
}


fun AppEntity.parseFrom(
    packageManager: PackageManager,
    packageInfo: PackageInfo,
    allInfo: Boolean = false
): AppEntity? {
    try {
        this.appName = packageInfo.applicationInfo.loadLabel(packageManager).toString()
        this.packageName = packageInfo.packageName
        this.versionName = packageInfo.versionName
        this.versionCode =
            if (Build.VERSION.SDK_INT >= 28) packageInfo.longVersionCode else packageInfo.versionCode.toLong()
        this.iconDrawable = packageInfo.applicationInfo.loadIcon(packageManager)
        this.isSystemApp = isSystemApp(packageInfo.applicationInfo.flags)
        this.updateTime = packageInfo.lastUpdateTime
        this.installTime = packageInfo.firstInstallTime
        // 只获取基本信息
        if (!allInfo) {
            return this
        }
        // applicationInfo
        val applicationInfo = packageInfo.applicationInfo
        if (applicationInfo.className.isNullOrEmpty()) {
            this.applicationName = "android.app.Application"
        } else {
            this.applicationName = applicationInfo.className
        }
        this.sourceDir = applicationInfo.sourceDir
        this.sourceApkSize = FileUtils.size(this.sourceDir!!)
        this.nativeLibraryDir = applicationInfo.nativeLibraryDir
        this.processName = applicationInfo.processName
        this.dataDir = applicationInfo.dataDir
        this.minSdkVersion = applicationInfo.minSdkVersion
        this.targetSdkVersion = applicationInfo.targetSdkVersion
        this.primaryCpuAbi = RefInvoke.getFieldObject(applicationInfo, "primaryCpuAbi") as? String
        this.uid = applicationInfo.uid
        this.flag = Integer.toHexString(applicationInfo.flags)
        this.backupAgentName = applicationInfo.backupAgentName
        this.deviceProtectedDataDir = applicationInfo.deviceProtectedDataDir
        return this
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

/**
 * 判断是否是系统应用
 */
private fun isSystemApp(flags: Int): Boolean {
    val isSysApp = (flags and ApplicationInfo.FLAG_SYSTEM) == 1
    val isSysUpdate = (flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1
    return isSysApp or isSysUpdate
}