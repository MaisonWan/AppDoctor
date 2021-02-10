package com.domker.app.doctor.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppEntity
import com.domker.app.doctor.main.PackageSizeAdapter.PackageSizeViewHolder
import com.domker.app.doctor.util.DateUtil
import com.domker.base.file.FileUtils
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch

const val SORT_NAME = 1
const val SORT_TIME = 2
const val SORT_SIZE = 3

/**
 * 包大小排名的适配器
 * Created by wanlipeng on 2020/6/15 12:33 AM
 */
class PackageSizeAdapter(private val dashboardContext: DashboardContext) :
        RecyclerView.Adapter<PackageSizeViewHolder>() {

    private var appList: List<AppEntity> = dashboardContext.appList
    private val inflater = LayoutInflater.from(dashboardContext.context)
    private var maxSize: Long = 0

    // 当前排序的类型
    private var currentSortType = SORT_SIZE
    private var currentSortDesc = true

    init {
        sortBy(currentSortType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageSizeViewHolder {
        val root = inflater.inflate(R.layout.item_package_size, parent, false)
        return PackageSizeViewHolder(root)
    }

    override fun onViewAttachedToWindow(holder: PackageSizeViewHolder) {
        super.onViewAttachedToWindow(holder)
        val isDesc = dashboardContext.setting.readSortDescending()
        dashboardContext.setting.readSortType().combineTransform(isDesc) { type, desc ->
            currentSortDesc = desc
            sortBy(type)
            currentSortType = type
            emit(Unit)
        }
    }

    override fun getItemCount(): Int {
        maxSize = appList.maxByOrNull { it.sourceApkSize!! }?.sourceApkSize ?: 1
        return appList.size
    }

    override fun onBindViewHolder(holder: PackageSizeViewHolder, position: Int) {
        val appEntity = appList[position]
        holder.icon?.setImageDrawable(appEntity.iconDrawable)
        holder.appName?.text = "${appEntity.appName}（${appEntity.versionName}）"
        holder.packageSize?.text = FileUtils.formatFileSize(appEntity.sourceApkSize!!)
        holder.systemApp?.visibility = if (appEntity.isSystemApp) View.VISIBLE else View.INVISIBLE
        holder.installTime?.text = DateUtil.getDataFromTimestamp(appEntity.updateTime)
    }

    /**
     * 按照指定类型排序
     *
     * @param type
     */
    fun sortBy(type: Int, ignoreDesc: Boolean = false) {
        // 判断当年需要排序的类型
        if (currentSortType == type && !ignoreDesc) {
            currentSortDesc = currentSortDesc.not()
        } else {
            currentSortType = type
        }

        appList = if (currentSortDesc) {
            when (type) {
                SORT_NAME -> dashboardContext.appList.sortedByDescending { it.appName }
                SORT_SIZE -> dashboardContext.appList.sortedByDescending { it.sourceApkSize }
                SORT_TIME -> dashboardContext.appList.sortedByDescending { it.installTime }
                else -> dashboardContext.appList
            }
        } else {
            when (type) {
                SORT_NAME -> dashboardContext.appList.sortedBy { it.appName }
                SORT_SIZE -> dashboardContext.appList.sortedBy { it.sourceApkSize }
                SORT_TIME -> dashboardContext.appList.sortedBy { it.installTime }
                else -> dashboardContext.appList
            }
        }

        dashboardContext.fragment.lifecycleScope.launch {
            dashboardContext.setting.writeSortType(currentSortType)
            dashboardContext.setting.writeSortDescending(currentSortDesc)
        }
        dashboardContext.fragment.lifecycleScope.launch {
            notifyDataSetChanged()
        }
    }

    fun showAllApp(show: Boolean) {
        dashboardContext.reloadAppList(show)
        sortBy(currentSortType, true)
    }

    class PackageSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName: TextView? = itemView.findViewById(R.id.textViewAppName)
        val packageSize: TextView? = itemView.findViewById(R.id.textViewSize)
        val icon: ImageView? = itemView.findViewById(R.id.imageViewIcon)
        val systemApp: TextView? = itemView.findViewById(R.id.imageViewSystemFlag)
        val installTime: TextView? = itemView.findViewById(R.id.textViewInstallTime)
    }

}