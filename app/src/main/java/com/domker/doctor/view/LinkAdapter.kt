package com.domker.doctor.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.R

/**
 * 用于链接点击的适配器
 */
open class LinkAdapter<T : Link>(private val context: Context) :
    DataSortAdapter<LinkAdapter.LinkViewHolder, T>(context) {

    protected val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = inflater.inflate(R.layout.item_tool_link, parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        bindView(holder, position)
    }

    private fun bindView(holder: LinkViewHolder, position: Int) {
        val item = getItem(position)
        holder.icon?.setImageResource(item.icon)
        holder.mainText?.text = item.name
        holder.subText?.text = item.description
    }


    class LinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mainText: TextView? = itemView.findViewById(R.id.textViewName)
        val subText: TextView? = itemView.findViewById(R.id.textViewDesc)
        val icon: ImageView? = itemView.findViewById(R.id.imageViewIcon)
    }
}