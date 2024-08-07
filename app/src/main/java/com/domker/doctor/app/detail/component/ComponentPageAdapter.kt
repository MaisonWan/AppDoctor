package com.domker.doctor.app.detail.component

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.R
import com.domker.base.addDividerItemDecoration

/**
 * ViewPager的适配器
 *
 * Created by wanlipeng on 2020/6/9 10:05 PM
 */
class ComponentPageAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val componentViewModel: ComponentViewModel,
    private val pageTitleRes: IntArray
) :
    RecyclerView.Adapter<ComponentPageAdapter.PageViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    // 按照数组的方式创建，数据，ViewModel，adapter
    private val data = Array<MutableList<ComponentInfo>>(pageTitleRes.size - 1) { mutableListOf() }
    private val mListAdapter: Array<ComponentListAdapter?> = Array(pageTitleRes.size - 1) { ComponentListAdapter(context, data[it]) }
    private lateinit var metaAdapter: MetaDataAdapter

    // 按照四种类型的数据分别拿到LiveData
    private val liveData: Array<MutableLiveData<List<ComponentInfo>>?> = arrayOf(
        componentViewModel.getActivityInfo(),
        componentViewModel.getServiceInfo(),
        componentViewModel.getProviderInfo(),
        componentViewModel.getReceiverInfo()
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val view = inflater.inflate(R.layout.layout_component_pager, parent, false)
        return PageViewHolder(view)
    }

    override fun getItemCount(): Int = pageTitleRes.size

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        // 最后一位是MetaData
        if (position == itemCount - 1) {
            componentViewModel.getMetaData().observe(lifecycleOwner) {
                updateMetaData(holder, it)
            }
        } else {
            // 四个类型的形式一样，所以用数组存储在一起，便于代码操作
            liveData[position]?.observe(lifecycleOwner) {
                updateComponentData(position, it, holder)
            }
        }

        bindRecyclerView(holder, position)
    }

    private fun updateMetaData(holder: PageViewHolder, data: Map<String, String>) {
        metaAdapter = MetaDataAdapter(context, data)
        holder.recyclerView?.adapter = metaAdapter
        metaAdapter.notifyItemRangeChanged(0, data.size)
        holder.title?.text = context.getString(R.string.total, data.size)
    }

    /**
     * 四大组件的展示，数据更新
     */
    private fun updateComponentData(position: Int, list: List<ComponentInfo>, holder: PageViewHolder) {
        data[position].clear()
        data[position].addAll(list.sortedBy { c -> c.shortName })
        // 点击Item的动作
        mListAdapter[position]?.itemClick { componentViewHolder, i ->
            val bundle = Bundle()
            bundle.putParcelable("component", data[position][i])
            Navigation.findNavController(componentViewHolder.view).navigate(R.id.navigation_component_detail, bundle)
        }
        mListAdapter[position]?.notifyItemRangeChanged(0, list.size)
        holder.title?.text = context.getString(R.string.total, list.size)
    }

    private fun bindRecyclerView(holder: PageViewHolder, position: Int) {
        holder.recyclerView?.let {
            // 最后一个MetaData类型不同，需要用其它类型的adapter
            if (position < itemCount - 1) {
                it.adapter = mListAdapter[position]
            }
            it.layoutManager = LinearLayoutManager(context)
            it.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
            it.setItemViewCacheSize(100)
        }
    }

    class PageViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        var recyclerView: RecyclerView? = view.findViewById(R.id.recyclerViewComponent)
        val title: TextView? = view.findViewById(R.id.pathTitle)
    }
}