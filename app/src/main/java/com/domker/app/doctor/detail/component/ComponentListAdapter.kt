package com.domker.app.doctor.detail.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.domker.app.doctor.R
import com.domker.app.doctor.detail.DetailItemViewHolder
import com.domker.app.doctor.util.log
import com.domker.app.doctor.widget.BaseItemListAdapter

/**
 * 显示组件详细信息
 * Created by wanlipeng on 2020/6/7 6:43 PM
 */
class ComponentListAdapter(
    private val context: Context,
    private val componentList: List<ComponentInfo>
) : BaseItemListAdapter<DetailItemViewHolder>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailItemViewHolder {
        log("onCreateViewHolder $viewType")
        return when (viewType) {
            ComponentInfo.TYPE_GROUP_TITLE -> {
                val view: View = inflater.inflate(R.layout.detail_label_layout, parent, false)
                DetailItemViewHolder(view)
            }
            ComponentInfo.TYPE_GROUP_TITLE_WITH_ICON -> {
                // create view
                val view: View = inflater.inflate(R.layout.item_subject_content_with_icons, parent, false)
                DetailItemViewHolder(view)
            }
            else -> {
                val view: View = inflater.inflate(R.layout.item_subject_content, parent, false)
                DetailItemViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int = componentList.size

    override fun onBindViewHolder(holder: DetailItemViewHolder, position: Int) {
        val p = componentList[position]
        holder.subject?.text = p.shortName
        holder.content?.text = p.name
        holder.icon?.visibility = View.VISIBLE
        holder.view.setOnClickListener {
            invokeItemClick(holder, position)
        }

        // 根据类型绑定不同指示的图标
        when (p.layoutType) {
            ComponentInfo.TYPE_GROUP_TITLE_WITH_ICON ->  {
                if (p.exported) {
                    holder.addFlagIcon(context, R.drawable.ic_outline_exit_to_app_24_blue)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return componentList[position].layoutType
    }
}