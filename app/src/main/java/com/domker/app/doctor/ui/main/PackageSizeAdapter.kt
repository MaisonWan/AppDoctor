package com.domker.app.doctor.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppEntity
import com.domker.app.doctor.ui.main.PackageSizeAdapter.PackageSizeViewHolder
import com.domker.base.file.FileUtils

/**
 * 包大小排名的适配器
 * Created by wanlipeng on 2020/6/15 12:33 AM
 */
class PackageSizeAdapter(private val context: Context,
                         private val appList: List<AppEntity>) :
        RecyclerView.Adapter<PackageSizeViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var maxSize: Long = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageSizeViewHolder {
        val root = inflater.inflate(R.layout.item_package_size, null)
        return PackageSizeViewHolder(root)
    }

    override fun getItemCount(): Int {
        maxSize = appList.maxBy { it.sourceApkSize!! }?.sourceApkSize ?: 1
        return appList.size
    }

    override fun onBindViewHolder(holder: PackageSizeViewHolder, position: Int) {
        val appEntity = appList[position]
        holder.progressBar?.apply {
            this.max = 100
            this.progress = (appEntity.sourceApkSize?.times(100f)?.div(maxSize))?.toInt() ?: 0
        }
        holder.appName?.text = appEntity.appName
        holder.packageSize?.text = FileUtils.formatFileSize(appEntity.sourceApkSize!!)
    }

    class PackageSizeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appName: TextView? = itemView.findViewById(R.id.textViewAppName)
        val packageSize: TextView? = itemView.findViewById(R.id.textViewSize)
        val progressBar: ProgressBar? = itemView.findViewById(R.id.progressBar)
    }


}