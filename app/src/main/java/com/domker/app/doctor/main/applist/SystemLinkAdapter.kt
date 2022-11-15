package com.domker.app.doctor.main.applist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.domker.app.doctor.R
import com.domker.app.doctor.view.DataSortAdapter

/**
 * 系统链接的适配器
 */
class SystemLinkAdapter(context: Context) :
    DataSortAdapter<SystemLinkViewHolder, SystemLink>(context) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemLinkViewHolder {
        val view: View = inflater.inflate(R.layout.app_item_grid_layout, parent, false)
        return SystemLinkViewHolder(view)
    }

}