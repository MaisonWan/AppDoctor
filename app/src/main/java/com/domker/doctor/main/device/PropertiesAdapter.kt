package com.domker.doctor.main.device

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.R

/**
 * 属性适配器
 * Created by wanlipeng on 2/15/21 1:59 AM
 */
class PropertiesAdapter(private val context: Context,
                        private val data: List<Pair<String, String>>) :
        RecyclerView.Adapter<PropertiesAdapter.PropertiesViewHolder>() {
    private val inflater = LayoutInflater.from(context)
    private var showData = data

    class PropertiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewKey: TextView = itemView.findViewById(R.id.textViewKey)
        val textViewValue: TextView = itemView.findViewById(R.id.textViewValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertiesViewHolder {
        val root = inflater.inflate(R.layout.item_key_value, parent, false)
        return PropertiesViewHolder(root)
    }

    override fun onBindViewHolder(holder: PropertiesViewHolder, position: Int) {
        val property = showData[position]

        holder.textViewKey.text = property.first
        holder.textViewValue.text = property.second
    }

    override fun getItemCount(): Int = showData.size

    /**
     * 根据关键词搜索
     */
    fun search(keyWords: String?) {
        showData = if (keyWords.isNullOrBlank()) {
            data
        } else {
            data.filter { it.first.contains(keyWords) }
        }
    }
}