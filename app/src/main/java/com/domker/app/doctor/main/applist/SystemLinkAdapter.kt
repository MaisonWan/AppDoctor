package com.domker.app.doctor.main.applist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.domker.app.doctor.R
import com.domker.app.doctor.view.DataSortAdapter

/**
 * 系统链接的适配器
 */
class SystemLinkAdapter(private val context: Context) :
    DataSortAdapter<SystemLinkViewHolder, SystemLink>(context) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SystemLinkViewHolder {
        val view: View = inflater.inflate(R.layout.app_item_grid_layout, parent, false)
        return SystemLinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: SystemLinkViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val data = getItem(position)

        setOnItemClickListener { view, item ->
            try {
                context.startActivity(item.intent)
            } catch (e: java.lang.Exception) {
                Toast.makeText(context, "机型适配问题，打开${item.name}失败", Toast.LENGTH_SHORT).show()
            }
        }

        holder.setIcon(data.iconRes)
        holder.setName(data.name)
    }

}