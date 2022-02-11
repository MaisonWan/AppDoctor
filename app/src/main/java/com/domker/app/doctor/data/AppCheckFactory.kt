package com.domker.app.doctor.data

import android.content.Context
import android.graphics.drawable.Drawable
import com.domker.app.doctor.CheckerApplication
import com.domker.app.doctor.db.AppEntity
import com.domker.base.file.FileUtils

/**
 * 单例方法，集合checker的一些操作
 * Created by wanlipeng on 2018/3/5.
 */
class AppCheckFactory private constructor(private val context: Context) {
    private val appDataDao = CheckerApplication.appDatabase.appDataDao()
    private val appSignatureDao = CheckerApplication.appDatabase.appSignatureDao()

    private var appList: List<AppEntity>? = null
    val checker = AppChecker(context)

    companion object {
        // 懒加载
        val instance: AppCheckFactory by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppCheckFactory(CheckerApplication.applicationContext)
        }
    }


    /**
     * 更新列表
     */
    fun updateAppListFromSystem() {
        appList = checker.getPackageList(true)
    }

    /**
     * 获取程序列表
     */
    fun getAppList(includeSystemApp: Boolean = false): List<AppEntity> {
        if (appList == null) {
            // 默认获取系统所有的应用
            updateAppListFromSystem()
        }
        // 如果包含系统应用，则返回默认即可，否则过滤一次
        if (includeSystemApp) {
            return appList!!
        }
        return appList!!.filter {
            !it.isSystemApp
        }
    }

    /**
     * 从系统更新信息，需要更新的到数据库中。获取最新图标
     */
    fun updateInfoToDatabase() {
        val indexData = HashMap<String, AppEntity>()
        appDataDao.allAppData().forEach { indexData[it.packageName] = it }
        // 需要新增的
        val needInsert = mutableListOf<AppEntity>()
        // 需要升级的
        val needUpdate = mutableListOf<AppEntity>()
        // 系统目前安装的app的包名
        val installAppPackageNameList = mutableListOf<String>()

        // 从系统中查询最新的app安装情况，然后和数据库里面对比，区分出来新增和需要升级的
        checker.getPackageList(true).forEach { item ->
            installAppPackageNameList.add(item.packageName)
            if (item.packageName in indexData) {
                val entity = indexData[item.packageName]
                entity?.iconDrawable = item.iconDrawable
                if (entity?.updateTime != item.updateTime) {
                    needUpdate.add(item)
                }
            } else {
                needInsert.add(item)
            }
        }
        // 需要移除的数据
        val deleteList =
            indexData.values.filter { it.packageName !in installAppPackageNameList }.toList()

        // 更新到内存中的icon
        updateIconToMemory(indexData)

        // 挨个更新基本信息和包大小然后存储
        updateSize(needInsert)
        updateSignature(needInsert)

        updateSize(needUpdate)
        updateSignature(needUpdate)

        saveToDataBase(needInsert, needUpdate, deleteList)
    }

    private fun saveToDataBase(
        needInsert: MutableList<AppEntity>,
        needUpdate: MutableList<AppEntity>,
        deleteList: List<AppEntity>
    ) {
        if (needInsert.isNotEmpty()) {
            appDataDao.insertAppData(needInsert)
            needInsert.forEach { appEntity ->
                appEntity.signatures?.also {
                    appSignatureDao.insertAppSignatures(it)
                }
            }
        }
        if (needUpdate.isNotEmpty()) {
            appDataDao.updateAppData(needUpdate)
            needInsert.forEach { appEntity ->
                appEntity.signatures?.also {
                    appSignatureDao.insertAppSignatures(it)
                }
            }
        }
        if (deleteList.isNotEmpty()) {
            appDataDao.deleteAppData(deleteList)
        }
    }

    /**
     * 更新到内存中的icon
     */
    private fun updateIconToMemory(indexData: HashMap<String, AppEntity>) {
        appList?.forEach { entity ->
            //
            indexData[entity.packageName]?.iconDrawable?.also {
                entity.iconDrawable = it
            }
        }
    }

    /**
     * 更新列表中的包体积大小的数据
     */
    private fun updateSize(list: MutableList<AppEntity>) {
        list.forEachIndexed { index, appEntity ->
            checker.getAppEntity(appEntity.packageName)?.apply {
                list[index].sourceApkSize = FileUtils.size(this.sourceDir!!)
            }
        }
    }

    private fun updateSignature(list: MutableList<AppEntity>) {
        list.forEach { appEntity ->
            appEntity.signatures = checker.getAppSignatures(appEntity.packageName)
        }
    }

    /**
     * 从数据库中更新
     */
    fun fetchAppListFromDatabase(): List<AppEntity> {
        appList = appDataDao.allAppData()
        return appList!!
    }

    /**
     * 获取应用的图标
     */
    fun getAppIcon(packageName: String): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            null
        }
    }
}