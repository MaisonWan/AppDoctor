package com.domker.doctor.main.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.R
import com.domker.doctor.data.db.AppEntity
import com.domker.doctor.main.dashboard.PackageSizeAdapter.PackageSizeViewHolder
import com.domker.doctor.util.DataFormat
import com.domker.doctor.view.DataSortAdapter
import com.domker.doctor.base.file.AppFileUtils
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch

/**
 * 包大小排名的适配器
 * Created by wanlipeng on 2020/6/15 12:33 AM
 */
class PackageSizeAdapter(private val dashboardContext: DashboardContext) :
    DataSortAdapter<PackageSizeViewHolder, AppEntity>(dashboardContext.context) {

    private val inflater = LayoutInflater.from(dashboardContext.context)

    init {
        setData(dashboardContext.appList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageSizeViewHolder {
        val root = inflater.inflate(R.layout.item_package_size, parent, false)
        return PackageSizeViewHolder(root)
    }

    override fun onViewAttachedToWindow(holder: PackageSizeViewHolder) {
        super.onViewAttachedToWindow(holder)
        val isDesc = dashboardContext.setting.readSortDescending()
        dashboardContext.setting.readSortType().combineTransform(isDesc) { type, desc ->
//            dataProcessor.sortBy(type, desc)
            emit(Unit)
        }
    }

    override fun onBindViewHolder(holder: PackageSizeViewHolder, position: Int) {
        val appEntity = getItem(position)
        holder.icon?.setImageDrawable(appEntity.iconDrawable)
        holder.appName?.text = "${appEntity.appName}(${appEntity.versionName})"
        holder.packageSize?.text = AppFileUtils.formatFileSize(appEntity.sourceApkSize!!)
        holder.systemApp?.visibility = if (appEntity.isSystemApp) View.VISIBLE else View.INVISIBLE
        holder.installTime?.text = DataFormat.getDataFromTimestamp(appEntity.updateTime)
    }

    override fun onEndSort(data: List<AppEntity>, itemId: Int, desc: Boolean) {
        super.onEndSort(data, itemId, desc)
        dashboardContext.fragment.lifecycleScope.launch {
            dashboardContext.setting.writeSortType(itemId)
            dashboardContext.setting.writeSortDescending(desc)
        }
        dashboardContext.fragment.lifecycleScope.launch {
            notifyAllDataChanged()
        }
    }

    class PackageSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName: TextView? = itemView.findViewById(R.id.textViewAppName)
        val packageSize: TextView? = itemView.findViewById(R.id.textViewSize)
        val icon: ImageView? = itemView.findViewById(R.id.imageViewIcon)
        val systemApp: TextView? = itemView.findViewById(R.id.imageViewSystemFlag)
        val installTime: TextView? = itemView.findViewById(R.id.textViewInstallTime)
    }

}