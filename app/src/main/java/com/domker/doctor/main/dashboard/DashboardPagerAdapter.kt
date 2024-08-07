package com.domker.doctor.main.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domker.doctor.R
import com.domker.base.addDividerItemDecoration


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
        val root = inflater.inflate(R.layout.fragment_main_page_dashboard, parent, false)
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
            this.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
            this.setItemViewCacheSize(100)
            packageSizeAdapter.notifyAllDataChanged()
        }
    }

    class PageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recyclerView: RecyclerView? = view.findViewById(R.id.recyclerViewPackageSize)
    }

    fun getDataProcessor() = packageSizeAdapter.getDataProcessor()
}