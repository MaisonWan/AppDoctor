package com.domker.app.doctor.detail.lib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.detail.component.ComponentAdapter
import com.domker.app.doctor.detail.component.ComponentInfo
import com.domker.base.addDividerItemDecoration

/**
 * App使用的Native库的分类，以及每个类型里面的数据都包含什么
 */
class AppLibPageAdapter(
    val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val titles: List<String>
) : RecyclerView.Adapter<AppLibPageAdapter.PageViewHolder>() {
    private val inflater = LayoutInflater.from(context)

    private val data = Array<MutableList<ComponentInfo>>(titles.size) { mutableListOf() }
    private val adapters: Array<ComponentAdapter> = Array(titles.size) {
        ComponentAdapter(context, data[it])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = inflater.inflate(R.layout.layout_component_pager, parent, false)
        return PageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        if (position in titles.indices) {
            // 四个类型的形式一样，所以用数组存储在一起，便于代码操作
//            liveData[position]?.observe(lifecycleOwner, {
//                data[position].clear()
//                data[position].addAll(it.sortedBy { c -> c.shortName })
//                adapter[position]?.notifyDataSetChanged()
//                holder.title?.text = "共${it.size}条"
//            })

            holder.recyclerView?.let {
                it.adapter = adapters[position]
                it.layoutManager = LinearLayoutManager(context)
                it.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
                it.setItemViewCacheSize(100)
            }
        }
    }

    override fun getItemCount(): Int = titles.size


    class PageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var recyclerView: RecyclerView? = view.findViewById(R.id.recyclerViewComponent)
        val title: TextView? = view.findViewById(R.id.pathTitle)
    }
}