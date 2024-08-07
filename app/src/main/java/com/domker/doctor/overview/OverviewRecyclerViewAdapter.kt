package com.domker.doctor.overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.R


/**
 * 菜单栏里面布局的样式，每行两个或者多个button的样式
 */
class OverviewRecyclerViewAdapter(
    private val values: List<DeviceItem>, private val fragment: Fragment
) : RecyclerView.Adapter<OverviewRecyclerViewAdapter.ViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(fragment.requireContext())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = layoutInflater.inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        when (item.typeId) {
            DeviceItem.DEVICE_TYPE_MODEL, DeviceItem.DEVICE_TYPE_SCREEN -> {
                DeviceBasicBinder(fragment.requireContext()).bindByType(holder, item, item.typeId)
            }

            DeviceItem.DEVICE_TYPE_SYSTEM -> DeviceSystemBinder().bind(holder, item)
            DeviceItem.DEVICE_TYPE_BATTERY -> DeviceBatteryBinder(fragment).bind(holder, item)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {

            }
        }

        val iconView: ImageView? = itemView.findViewById(R.id.imageViewIcon)
        val mainTextView: TextView? = itemView.findViewById(R.id.textViewMain)
        val secondaryTextView: TextView? = itemView.findViewById(R.id.textViewSecondary)
        val progressBar: ProgressBar? = itemView.findViewById(R.id.progressBar)
        val chargeTextView: TextView? = itemView.findViewById(R.id.textViewCharge)
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