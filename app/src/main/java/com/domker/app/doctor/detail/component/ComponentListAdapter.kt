package com.domker.app.doctor.detail.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.domker.app.doctor.R
import com.domker.app.doctor.util.log
import com.domker.app.doctor.widget.BaseItemListAdapter

/**
 * 显示组件详细信息
 * Created by wanlipeng on 2020/6/7 6:43 PM
 */
class ComponentListAdapter(
    context: Context,
    private val componentList: List<ComponentInfo>
) : BaseItemListAdapter<ComponentViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        log("onCreateViewHolder $viewType")
        return when (viewType) {
            ComponentInfo.TYPE_GROUP_TITLE -> {
                val view: View = inflater.inflate(R.layout.detail_label_layout, null)
                ComponentViewHolder(view)
            }
            else -> {
                val view: View = inflater.inflate(R.layout.item_detail_subject, null)
                ComponentViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = componentList.size

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val p = componentList[position]
        holder.subject?.text = p.shortName
        holder.content?.text = p.name
        holder.icon?.visibility = View.VISIBLE
        holder.view.setOnClickListener {
            invokeItemClick(holder, position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return componentList[position].type
    }
}