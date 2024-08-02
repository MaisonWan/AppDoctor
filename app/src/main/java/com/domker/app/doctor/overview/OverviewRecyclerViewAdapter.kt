package com.domker.app.doctor.overview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R


/**
 * 菜单栏里面布局的样式，每行两个或者多个button的样式
 */
class OverviewRecyclerViewAdapter(
    private val values: List<DeviceItem>, context: Context
) : RecyclerView.Adapter<OverviewRecyclerViewAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.mainTextView?.text = item.typeId.toString()
        holder.secondaryTextView?.text = item.layoutType.toString()
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconView: ImageView? = itemView.findViewById(R.id.imageViewIcon)
        val mainTextView: TextView? = itemView.findViewById(R.id.textViewMain)
        val secondaryTextView: TextView? = itemView.findViewById(R.id.textViewSecondary)
    }

    override fun getItemViewType(position: Int): Int {
        return values[position].layoutResId
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        val manager = recyclerView.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (values[position].layoutType) {
                        DeviceItem.LAYOOUT_LONG -> 6
                        else -> 3
                    }
                }
            }
        }

    }
}