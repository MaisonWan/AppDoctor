package com.domker.app.doctor.data

import android.content.ComponentName
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import com.domker.app.doctor.db.AppEntity
import com.domker.app.doctor.db.AppSignature
import com.domker.app.doctor.db.parseFrom
import com.domker.app.doctor.detail.component.ComponentInfo
import com.domker.app.doctor.detail.component.ComponentInfo.Companion.TYPE_ACTIVITY
import com.domker.app.doctor.detail.component.ComponentInfo.Companion.TYPE_GROUP_TITLE_WITH_ICON
import com.domker.app.doctor.detail.component.ComponentInfo.Companion.TYPE_PROVIDER
import com.domker.app.doctor.detail.component.ComponentInfo.Companion.TYPE_RECEIVER
import com.domker.app.doctor.detail.component.ComponentInfo.Companion.TYPE_SERVICE
import com.domker.app.doctor.detail.component.componentOfType
import com.domker.app.doctor.detail.component.parseFrom
import java.security.MessageDigest

const val SIGNATURE_SHA256 = "sha256"
const val SIGNATURE_SHA1 = "sha1"
const val SIGNATURE_MD5 = "md5"

/**
 * 检测App信息
 * Created by wanlipeng on 2018/2/6.
 */
class AppChecker(private val context: Context) {

    /**
     * 获取安装程序列表
     */
    fun getPackageList(includeSystemApp: Boolean = false): List<AppEntity> {
        val appList = mutableListOf<AppEntity>()
        val packages = context.packageManager.getInstalledPackages(0)
        packages.map {
            AppEntity().parseFrom(context.packageManager, it, true)
        }.filter {
            if (it == null) {
                false
            } else {
                val isSelf = it.packageName == context.packageName
                !isSelf and (includeSystemApp or !it.isSystemApp)
            }
        }.forEach {
            it?.let { app ->
                appList.add(app)
            }
        }
        return appList
    }

    /**
     * 获取app的详细信息
     */
    fun getAppEntity(packageName: String): AppEntity? {
        return try {
            val info = context.packageManager.getPackageInfo(packageName, 0)
            AppEntity().parseFrom(context.packageManager, info, true)
        } catch (e: java.lang.Exception) {
            null
        }
    }

    /**
     * 获取应用的签名
     */
    fun getAppSignature(packageName: String): Map<String, Array<String>> {
        try {
            val cert = getAppSignatureCert(packageName)

            val sha256 = parserSignature(cert, MessageDigest.getInstance("SHA256"))
            val sha1 = parserSignature(cert, MessageDigest.getInstance("SHA1"))
            val md5 = parserSignature(cert, MessageDigest.getInstance("MD5"), false)
            return mapOf(
                Pair(SIGNATURE_SHA256, sha256),
                Pair(SIGNATURE_SHA1, sha1),
                Pair(SIGNATURE_MD5, md5)
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return emptyMap()
    }

    /**
     * 指定包名，返回该app的所有签名实体
     */
    fun getAppSignatures(packageName: String): Array<AppSignature> {
        try {
            val result = mutableListOf<AppSignature>()
            getAppSignatureCert(packageName).forEach { signature ->
                val s = AppSignature()
                // 转化为二进制之后
                val byteArray = signature.toByteArray()
                s.packageName = packageName
                s.originSignature = byteArray
                s.sha256Signature = certToString(MessageDigest.getInstance("SHA256"), byteArray)
                s.sha1Signature = certToString(MessageDigest.getInstance("SHA1"), byteArray)
                s.md5Signature = certToString(MessageDigest.getInstance("MD5"), byteArray, false)
                result.add(s)
            }
            println("getAppSignatures count = ${result.size}")
            return result.toTypedArray()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return emptyArray()
    }

    private fun getAppSignatureCert(packageName: String): Array<Signature> {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            PackageManager.GET_SIGNING_CERTIFICATES
        } else {
            PackageManager.GET_SIGNATURES
        }
        val info = context.packageManager.getPackageInfo(packageName, flag)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.signingInfo.apkContentsSigners
        } else {
            info.signatures
        }
    }

    /**
     * 把签名的信息，根据md的类型，转化为字符串
     */
    private fun parserSignature(
        cert: Array<Signature>,
        md: MessageDigest,
        splitChar: Boolean = true
    ): Array<String> {
        // 创建结果
        val signatures = Array(cert.size) { "" }
        cert.forEachIndexed { index, signature ->
            signatures[index] = certToString(md, signature.toByteArray(), splitChar)
        }
        return signatures
    }

    /**
     * 根据给定的加密方式，把签名转化为字符串
     */
    private fun certToString(
        md: MessageDigest,
        signature: ByteArray,
        splitChar: Boolean = true
    ): String {
        val publicKey: ByteArray = md.digest(signature)
        val hexString = StringBuilder()
        for (i in publicKey.indices) {
            val appendString = Integer.toHexString(publicKey[i].toInt().and(0xFF)).uppercase()
            if (appendString.length == 1) {
                hexString.append("0")
            }
            hexString.append(appendString)
            if (splitChar && i != publicKey.size - 1) {
                hexString.append(":")
            }
        }
        return hexString.toString()
    }

    /**
     * 获取Activity的信息，时间比较长，单独抽象方法
     */
    fun getActivityListInfo(packageName: String): List<ComponentInfo> {
        val list = mutableListOf<ComponentInfo>()
        try {
            // activity list
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                .activities?.forEach {
                    val componentInfo = ComponentInfo()
                    componentInfo.type = TYPE_ACTIVITY
                    componentInfo.layoutType = TYPE_GROUP_TITLE_WITH_ICON
                    list.add(componentInfo.parseFrom(it))
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 获取详细信息
     */
    fun getActivityInfo(packageName: String, activityName: String): ComponentInfo {
        val info = componentOfType(TYPE_ACTIVITY)
        try {
            val componentName = ComponentName(packageName, activityName)
            val activityInfo = context.packageManager.getActivityInfo(componentName, 0)
            info.icon = activityInfo.loadLogo(context.packageManager)
            if (info.icon == null) {
                info.icon = activityInfo.applicationInfo.loadIcon(context.packageManager)
            }
            activityInfo.metaData?.keySet()?.forEach {
                info.metaData[it] = activityInfo.metaData[it]?.toString() ?: ""
            }
            info.parseFrom(activityInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return info
    }

    /**
     * 获取Service的信息，时间比较长，单独抽象方法
     */
    fun getServiceListInfo(packageName: String): List<ComponentInfo> {
        val list = mutableListOf<ComponentInfo>()
        try {
            // service list
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_SERVICES)
                .services?.forEach {
                    val componentInfo = ComponentInfo()
                    componentInfo.type = TYPE_SERVICE
                    list.add(componentInfo.parseFrom(it))
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 服务信息
     */
    fun getServiceInfo(packageName: String, serviceName: String): ComponentInfo {
        val info = componentOfType(TYPE_SERVICE)
        try {
            val componentName = ComponentName(packageName, serviceName)
            val serviceInfo = context.packageManager.getServiceInfo(componentName, 0)
            info.icon = serviceInfo.loadLogo(context.packageManager)
            if (info.icon == null) {
                info.icon = serviceInfo.applicationInfo.loadIcon(context.packageManager)
            }
            info.parseFrom(serviceInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return info
    }

    /**
     * Providers
     */
    fun getProvidersListInfo(packageName: String): List<ComponentInfo> {
        val list = mutableListOf<ComponentInfo>()
        try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_PROVIDERS)
                .providers?.forEach {
                    val componentInfo = ComponentInfo()
                    componentInfo.type = TYPE_PROVIDER
                    list.add(componentInfo.parseFrom(it))
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 获取Provider的信息
     */
    fun getProviderInfo(packageName: String, providerName: String): ComponentInfo {
        val info = componentOfType(TYPE_PROVIDER)
        try {
            val componentName = ComponentName(packageName, providerName)
            val providerInfo = context.packageManager.getProviderInfo(componentName, 0)
            info.icon = providerInfo.loadLogo(context.packageManager)
            if (info.icon == null) {
                info.icon = providerInfo.applicationInfo.loadIcon(context.packageManager)
            }
            info.parseFrom(providerInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return info
    }

    /**
     * 获取广播的信息
     */
    fun getReceiversListInfo(packageName: String): List<ComponentInfo> {
        val list = mutableListOf<ComponentInfo>()
        try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_RECEIVERS)
                .receivers?.forEach {
                    val componentInfo = ComponentInfo()
                    componentInfo.type = TYPE_RECEIVER
                    list.add(componentInfo.parseFrom(it))
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    /**
     * 获取广播接收器的信息
     */
    fun getReceiverInfo(packageName: String, receiverName: String): ComponentInfo {
        val info = componentOfType(TYPE_RECEIVER)
        try {
            val componentName = ComponentName(packageName, receiverName)
            val receiverInfo = context.packageManager.getReceiverInfo(componentName, 0)
            info.icon = receiverInfo.loadLogo(context.packageManager)
            if (info.icon == null) {
                info.icon = receiverInfo.applicationInfo.loadIcon(context.packageManager)
            }
            info.parseFrom(receiverInfo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return info
    }

    /**
     * 获取AndroidManifest里面的元数据
     */
    fun getMetaData(packageName: String): Map<String, String> {
        val map = mutableMapOf<String, String>()
        val applicationInfo: ApplicationInfo =
            context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val metadata = applicationInfo.metaData
        metadata?.keySet()?.forEach {
            map[it] = metadata[it].toString()
        }
        return map
    }

    /**
     * 获取应用的权限列表
     */
    fun getPermissions(packageName: String): List<ComponentInfo> {
        val list = mutableListOf<ComponentInfo>()
        try {
            val p =
                context.packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            p.requestedPermissions?.map {
                val componentInfo = componentOfType(ComponentInfo.TYPE_PERMISSION)
                componentInfo.name = it
                componentInfo
            }?.forEach {
                list.add(it)
            }
            p.permissions?.map {
                componentOfType(ComponentInfo.TYPE_PERMISSION).parseFrom(it)
            }?.forEach {
                list.add(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
