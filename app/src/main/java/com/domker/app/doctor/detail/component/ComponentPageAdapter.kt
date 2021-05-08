package com.domker.app.doctor.detail.component

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.app.doctor.detail.AppDetailActivity
import com.domker.base.addItemDecoration

/**
 * ViewPager的适配器
 *
 * Created by wanlipeng on 2020/6/9 10:05 PM
 */
class ComponentPageAdapter(private val context: Context,
                           private val lifecycleOwner: LifecycleOwner,
                           private val pageTitleRes: IntArray) : RecyclerView.Adapter<ComponentPageAdapter.PageViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    // 按照数组的方式创建，数据，ViewModel，adapter
    private val data = Array<MutableList<ComponentInfo>>(pageTitleRes.size) { mutableListOf() }
    private val adapter: Array<ComponentAdapter?> = Array(pageTitleRes.size) { ComponentAdapter(context, data[it]) }
    private lateinit var liveData: Array<MutableLiveData<List<ComponentInfo>>?>

    init {
        AppDetailActivity.componentViewModel?.let {
            liveData = arrayOf(it.activityInfo, it.serviceInfo, it.providerInfo, it.receiverInfo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = inflater.inflate(R.layout.layout_component_pager, parent, false)
        return PageViewHolder(view)
    }

    override fun getItemCount(): Int = pageTitleRes.size

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        if (position in pageTitleRes.indices) {
            // 四个类型的形式一样，所以用数组存储在一起，便于代码操作
            liveData[position]?.observe(lifecycleOwner, {
                data[position].clear()
                data[position].addAll(it.sortedBy { c -> c.shortName })
                adapter[position]?.notifyDataSetChanged()
                holder.title?.text = "共${it.size}条"
            })
            holder.recyclerView?.let {
                it.adapter = adapter[position]
                it.layoutManager = LinearLayoutManager(context)
                it.addItemDecoration(context, R.drawable.inset_recyclerview_divider)
                it.setItemViewCacheSize(100)
            }
        }
    }

    class PageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var recyclerView: RecyclerView? = view.findViewById(R.id.recyclerViewComponent)
        val title: TextView? = view.findViewById(R.id.pathTitle)
    }
}