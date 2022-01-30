package com.domker.app.doctor.detail.permission

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.detail.DetailItemViewHolder
import com.domker.app.doctor.detail.component.ComponentInfo
import com.domker.app.doctor.detail.detailItemViewHolderOf

/**
 * 显示权限的列表
 * Created by wanlipeng on 2020/6/15 2:41 PM
 */
class PermissionListAdapter(
    private val context: Context,
    private val componentList: List<ComponentInfo>
) :
    RecyclerView.Adapter<DetailItemViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private val permissions = HashMap<String, PermissionDescription>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailItemViewHolder {
        val root = inflater.inflate(R.layout.item_subject_content, parent, false)
        return detailItemViewHolderOf(root, View.VISIBLE)
    }

    override fun getItemCount(): Int = componentList.size

    override fun onBindViewHolder(holder: DetailItemViewHolder, position: Int) {
        val name = componentList[position].name
        holder.subject?.text = name
        val p = if (name in permissions) {
            permissions[name]!!
        } else {
            getPermissionDescriptionFromSystem(
                context.packageManager, context.resources,
                componentList[position].name ?: ""
            )
        }
        holder.content?.text = p.description
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(PERMISSION_NAME, p.name)
            bundle.putString(GROUP_NAME, p.groupName)
            bundle.putString(DESCRIPTION, p.description)
            bundle.putString(PERMISSION_DETAIL, p.detail)
            Navigation.findNavController(it).navigate(R.id.navigation_permission_detail, bundle)
        }
    }
}