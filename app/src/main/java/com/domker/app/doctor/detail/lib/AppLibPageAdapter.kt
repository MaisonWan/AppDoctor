package com.domker.app.doctor.detail.lib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.base.addDividerItemDecoration
import com.domker.base.file.ZipFileItem

/**
 * App使用的Native库的分类，以及每个类型里面的数据都包含什么
 */
class AppLibPageAdapter(
    val context: Context,
    private val titles: List<String>,
    private val dataMap: Map<String, List<ZipFileItem>>
) : RecyclerView.Adapter<AppLibPageAdapter.PageViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = inflater.inflate(R.layout.layout_component_pager, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        // 四个类型的形式一样，所以用数组存储在一起，便于代码操作
        holder.recyclerView?.let {
            val data = dataMap[titles[position]]!!
            it.adapter = AppLibItemListAdapter(context, data)
            it.layoutManager = LinearLayoutManager(context)
            it.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
            it.setItemViewCacheSize(100)
            it.adapter?.notifyItemRangeChanged(0, data.size)
            holder.title?.text = "共${data.size}条"
        }
    }

    override fun getItemCount(): Int = titles.size


    class PageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var recyclerView: RecyclerView? = view.findViewById(R.id.recyclerViewComponent)
        val title: TextView? = view.findViewById(R.id.pathTitle)
    }
}