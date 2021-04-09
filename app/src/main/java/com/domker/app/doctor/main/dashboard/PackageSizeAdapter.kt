package com.domker.app.doctor.main.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppDataProcessor
import com.domker.app.doctor.data.DataProcessor
import com.domker.app.doctor.main.dashboard.PackageSizeAdapter.PackageSizeViewHolder
import com.domker.app.doctor.util.DateUtil
import com.domker.base.file.FileUtils
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.launch

/**
 * 包大小排名的适配器
 * Created by wanlipeng on 2020/6/15 12:33 AM
 */
class PackageSizeAdapter(private val dashboardContext: DashboardContext) :
        RecyclerView.Adapter<PackageSizeViewHolder>(), DataProcessor {

    private val dataProcessor = AppDataProcessor(dashboardContext.appList)
    private val inflater = LayoutInflater.from(dashboardContext.context)

    init {
        dataProcessor.setOnSortedCallback { type, desc ->
            dashboardContext.fragment.lifecycleScope.launch {
                dashboardContext.setting.writeSortType(type)
                dashboardContext.setting.writeSortDescending(desc)
            }
            dashboardContext.fragment.lifecycleScope.launch {
                notifyDataSetChanged()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageSizeViewHolder {
        val root = inflater.inflate(R.layout.item_package_size, parent, false)
        return PackageSizeViewHolder(root)
    }

    override fun onViewAttachedToWindow(holder: PackageSizeViewHolder) {
        super.onViewAttachedToWindow(holder)
        val isDesc = dashboardContext.setting.readSortDescending()
        dashboardContext.setting.readSortType().combineTransform(isDesc) { type, desc ->
            dataProcessor.sortBy(type, desc)
            emit(Unit)
        }
    }

    override fun getItemCount(): Int {
        return dataProcessor.size()
    }

    override fun onBindViewHolder(holder: PackageSizeViewHolder, position: Int) {
        val appEntity = dataProcessor[position]
        holder.icon?.setImageDrawable(appEntity.iconDrawable)
        holder.appName?.text = "${appEntity.appName}(${appEntity.versionName})"
        holder.packageSize?.text = FileUtils.formatFileSize(appEntity.sourceApkSize!!)
        holder.systemApp?.visibility = if (appEntity.isSystemApp) View.VISIBLE else View.INVISIBLE
        holder.installTime?.text = DateUtil.getDataFromTimestamp(appEntity.updateTime)
    }

    class PackageSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName: TextView? = itemView.findViewById(R.id.textViewAppName)
        val packageSize: TextView? = itemView.findViewById(R.id.textViewSize)
        val icon: ImageView? = itemView.findViewById(R.id.imageViewIcon)
        val systemApp: TextView? = itemView.findViewById(R.id.imageViewSystemFlag)
        val installTime: TextView? = itemView.findViewById(R.id.textViewInstallTime)
    }

    override fun getDataProcessor(): AppDataProcessor = dataProcessor
}