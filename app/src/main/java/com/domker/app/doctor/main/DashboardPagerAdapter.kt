package com.domker.app.doctor.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.app.doctor.R
import com.domker.base.addItemDecoration


/**
 * 统计展示的分页Adapter
 */
class DashboardPagerAdapter(private val dashboardContext: DashboardContext,
                            private val tabTitleRes: IntArray) :
        RecyclerView.Adapter<DashboardPagerAdapter.PageViewHolder>() {
    private val context = dashboardContext.context
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var packageSizeAdapter = PackageSizeAdapter(dashboardContext)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val root = inflater.inflate(R.layout.fragment_app_dashboard, parent, false)
        return PageViewHolder(root)
    }

    override fun getItemCount(): Int = tabTitleRes.size

    override fun getItemViewType(position: Int): Int {
        return tabTitleRes[position]
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        when (tabTitleRes[position]) {
            R.string.item_package_size -> {
                bindPackageSize(holder)
            }
        }
    }

    private fun bindPackageSize(holder: PageViewHolder) {
        holder.recyclerView?.apply {
            this.adapter = packageSizeAdapter
            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(context, R.drawable.inset_recyclerview_divider)
            this.setItemViewCacheSize(100)
            packageSizeAdapter.notifyDataSetChanged()
        }
    }

    /**
     * 按照指定类型排序
     *
     * @param type
     */
    fun sortBy(type: Int) {
        packageSizeAdapter.sortBy(type)
    }

    fun showAllApp(show: Boolean) {
        packageSizeAdapter.showAllApp(show)
    }

    class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView? = view.findViewById(R.id.recyclerViewPackageSize)
    }
}