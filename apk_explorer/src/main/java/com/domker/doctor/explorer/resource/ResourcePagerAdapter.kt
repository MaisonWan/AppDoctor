package com.domker.doctor.explorer.resource

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.explorer.R

/**
 * 资源查看器的分页
 */
class ResourcePagerAdapter(private val context: Context, private val titles: Array<String>)
    : RecyclerView.Adapter<ResourcePagerAdapter.ResourceViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    class ResourceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResourceViewHolder {
        val view = inflater.inflate(R.layout.layout_explorer_pager, parent, false)
        return ResourceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = titles.size

}