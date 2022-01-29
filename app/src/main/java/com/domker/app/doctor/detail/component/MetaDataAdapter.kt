package com.domker.app.doctor.detail.component

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.domker.app.doctor.R
import com.domker.app.doctor.widget.BaseItemListAdapter

/**
 * 展示metaData的适配器
 */
class MetaDataAdapter(
    context: Context,
    private val metaDataMap: Map<String, String>
) : BaseItemListAdapter<ComponentViewHolder>(context) {
    private val dataKeys = metaDataMap.keys.sorted()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        val view: View = inflater.inflate(R.layout.item_detail_subject, parent, false)
        return ComponentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        val key = dataKeys[position]
        holder.subject?.text = key
        holder.content?.text = metaDataMap[key]
//        holder.icon?.visibility = View.VISIBLE
        holder.view.setOnClickListener {
            invokeItemClick(holder, position)
        }
    }

    override fun getItemCount(): Int = metaDataMap.size
}