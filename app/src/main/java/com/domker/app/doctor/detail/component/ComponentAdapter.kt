package com.domker.app.doctor.detail.component

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.util.log

/**
 * 显示组件详细信息
 * Created by wanlipeng on 2020/6/7 6:43 PM
 */
class ComponentAdapter(private val context: Context,
                       private val componentList: List<ComponentInfo>) :
        RecyclerView.Adapter<ComponentAdapter.ComponentViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        log("onCreateViewHolder $viewType")
        return when (viewType) {
            ComponentInfo.TYPE_GROUP_TITLE -> {
                val view: View = inflater.inflate(R.layout.detail_label_layout, null)
                ComponentViewHolder(view)
            }
            else -> {
                val view: View = inflater.inflate(R.layout.detail_subject_layout, null)
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
            val bundle = Bundle()
            bundle.putParcelable("component", p)
            Navigation.findNavController(holder.view).navigate(R.id.navigation_component_detail, bundle)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return componentList[position].type
    }

    /**
     * 显示App详细信息的ViewHolder
     */
    class ComponentViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var subject: TextView? = view.findViewById(R.id.textViewSubject)
        var content: TextView? = view.findViewById(R.id.textViewLabel)
        var icon: ImageView? = view.findViewById(R.id.imageViewType)

        init {
            subject?.setTextIsSelectable(false)
            content?.setTextIsSelectable(false)
        }
    }
}