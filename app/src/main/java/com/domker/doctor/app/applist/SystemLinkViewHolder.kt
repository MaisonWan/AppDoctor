package com.domker.doctor.app.applist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.R

/**
 * 展示系统链接的VH
 */
class SystemLinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val icon: ImageView = itemView.findViewById(R.id.imageViewIcon)
    val name: TextView = itemView.findViewById(R.id.textViewAppVersion)

    init {
        itemView.findViewById<TextView>(R.id.textViewAppName).visibility = View.GONE
        itemView.findViewById<TextView>(R.id.imageViewSystemFlag).visibility = View.GONE
    }

    /**
     * 设置图标
     */
    fun setIcon(resId: Int) {
        icon.setImageResource(resId)
    }

    /**
     * 设置应用名称
     */
    fun setName(appName: String) {
        name.text = appName
    }
}