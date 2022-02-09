package com.domker.app.doctor.detail.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.domker.app.doctor.data.AppChecker
import com.domker.app.doctor.data.AppEntity
import com.domker.app.doctor.detail.container.DETAIL_TYPE_PACKAGE
import com.domker.app.doctor.detail.container.DETAIL_TYPE_SIGNATURE
import com.domker.app.doctor.entiy.AppItemInfo
import com.domker.app.doctor.entiy.appItemOf
import com.domker.app.doctor.util.ApkViewer
import com.domker.app.doctor.util.DataFormat
import com.domker.base.SystemVersion
import com.domker.base.file.FileUtils
import com.domker.base.file.ZipFileItem
import com.domker.base.thread.AppExecutors
import com.domker.base.toChinese

/**
 * App详情的首页的ViewModel
 */
class HomeViewModel : ViewModel() {

    private val liveData = MutableLiveData<HomeDetail>()

    // 安装包里面的LiveData
    private val apkLiveData = MutableLiveData<Map<String, List<ZipFileItem>>>()

    fun getAppInfo(): MutableLiveData<HomeDetail> = liveData

    /**
     * 获取apk里面的信息，需要详细分析apk安装包，比较耗时
     */
    fun getApkDetail(): MutableLiveData<Map<String, List<ZipFileItem>>> = apkLiveData

    /**
     * 更新数据
     */
    fun updateData(appChecker: AppChecker, appPackageName: String) {
        AppExecutors.executor.execute {
            // 异步获取app的信息
            appChecker.getAppEntity(appPackageName)?.also { entity ->
                // 获取apk签名
                entity.signatures = appChecker.getAppSignatures(appPackageName)
                val homeDetail = HomeDetail(entity, warpAppEntity(entity))
                liveData.postValue(homeDetail)

                // 分析apk安装包内部详细信息
                parserApkInfo(entity)
            }
        }
    }

    /**
     * 系统拿到的原始数据，转化为我们需要关注的具体数据行
     */
    private fun warpAppEntity(appEntity: AppEntity): List<AppItemInfo> {
        val detailList = mutableListOf<AppItemInfo>()
        detailList.add(appItemOf("版本名", appEntity.versionName))
        detailList.add(appItemOf("版本号", appEntity.versionCode.toString()))
        val t = SystemVersion.getVersion(appEntity.targetSdkVersion)
        detailList.add(appItemOf("目标版本号", SystemVersion.getShowLabel(t)))
        val v = SystemVersion.getVersion(appEntity.minSdkVersion)
        detailList.add(appItemOf("最低支持系统版本", SystemVersion.getShowLabel(v)))
        detailList.add(appItemOf("系统应用", appEntity.isSystemApp.toChinese()))
        detailList.add(appItemOf("首次安装时间", DataFormat.getAppInstallTime(appEntity.installTime)))
        detailList.add(appItemOf("最近更新时间", DataFormat.getDataFromTimestamp(appEntity.updateTime)))
        detailList.add(appItemOf("Application名称", appEntity.applicationName!!))
        detailList.add(AppItemInfo("源文件路径", appEntity.sourceDir!!, type = DETAIL_TYPE_PACKAGE))
        detailList.add(appItemOf("源文件大小", FileUtils.formatFileSize(appEntity.sourceApkSize!!)))
        detailList.add(appItemOf("Native库路径", appEntity.nativeLibraryDir))
        detailList.add(appItemOf("备份代理类", appEntity.backupAgentName))
        detailList.add(appItemOf("主要CPU架构", appEntity.primaryCpuAbi))
        detailList.add(appItemOf("Data路径", appEntity.dataDir))
        detailList.add(appItemOf("保护Data路径", appEntity.deviceProtectedDataDir))
        detailList.add(appItemOf("主进程名", appEntity.processName))
        // 默认展示第一个
        appEntity.signatures?.first { appSignature ->
            detailList.add(appItemOf("SHA256签名", appSignature.sha256Signature).also {
                it.type = DETAIL_TYPE_SIGNATURE
                it.signatures = appEntity.signatures
            })
        }

        detailList.add(appItemOf("User ID", appEntity.uid.toString()))
        detailList.add(appItemOf("Flag", appEntity.flag))
        // 关联
        detailList.forEach {
            it.appEntity = appEntity
        }
        return detailList
    }

    private fun parserApkInfo(appEntity: AppEntity) {
        appEntity.sourceDir?.apply {
            val viewer = ApkViewer(this)
            apkLiveData.postValue(viewer.getLibFiles())
        }
    }

    data class HomeDetail(val appEntity: AppEntity, val itemList: List<AppItemInfo>)
}