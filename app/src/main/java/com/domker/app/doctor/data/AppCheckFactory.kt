package com.domker.app.doctor.data

import android.content.Context
import android.graphics.drawable.Drawable
import com.domker.app.doctor.CheckerApplication
import com.domker.base.file.FileUtils

/**
 * Created by wanlipeng on 2018/3/5.
 */
class AppCheckFactory private constructor(private val context: Context) {
    private val db = CheckerApplication.appDatabase.appDataDao()
    private var appList: List<AppEntity>? = null
    val checker = AppChecker(context)

    companion object {
        @Volatile
        private var instance: AppCheckFactory? = null

        fun getInstance(context: Context): AppCheckFactory {
            if (instance == null) {
                synchronized(AppCheckFactory::class) {
                    if (instance == null) {
                        instance = AppCheckFactory(context)
                    }
                }
            }
            return instance!!
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
            updateAppListFromSystem()
        }
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
        db.allAppData().forEach { indexData[it.packageName] = it }
        // 需要升级的
        val needInsert = mutableListOf<AppEntity>()
        val needUpdate = mutableListOf<AppEntity>()
        val installAppPackageNameList = mutableListOf<String>()

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

        // 更新到内存中的icon
        appList?.forEach {
            val icon = indexData[it.packageName]?.iconDrawable
            if (icon != null) {
                it.iconDrawable = icon
            }
        }

        // 挨个更新基本信息和包大小然后存储
        val updateSize: (MutableList<AppEntity>) -> Unit = { list ->
            list.forEachIndexed { index, appEntity ->
                checker.getAppEntity(appEntity.packageName)?.apply {
                    list[index].sourceApkSize = FileUtils.size(this.sourceDir!!)
                }
            }
        }
        updateSize(needInsert)
        updateSize(needUpdate)
        if (needInsert.isNotEmpty()) {
            db.insertAppData(needInsert)
        }
        if (needUpdate.isNotEmpty()) {
            db.updateAppData(needUpdate)
        }
        val deleteList = indexData.values.filter { it.packageName !in installAppPackageNameList }.toList()
        if (deleteList.isNotEmpty()) {
            db.deleteAppData(deleteList)

        }
    }

    /**
     * 从数据库中更新
     */
    fun updateAppListFromDatabase(): List<AppEntity> {
        appList = db.allAppData()
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