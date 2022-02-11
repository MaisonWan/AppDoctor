package com.domker.app.doctor.main.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.data.AppDataProcessor
import com.domker.app.doctor.data.DataProcessor
import com.domker.app.doctor.databinding.PagerAppListItemBinding
import com.domker.app.doctor.db.AppEntity
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.AppDiffCallBack
import com.domker.app.doctor.widget.AppListAdapter
import com.domker.app.doctor.widget.LAYOUT_TYPE_GRID
import com.domker.app.doctor.widget.LAYOUT_TYPE_LIST
import com.domker.base.addDividerItemDecoration
import com.domker.base.thread.AppExecutors

/**
 * 程序列表的分页适配器
 * Created by wanlipeng on 1/29/21 11:52 AM
 */
class AppPageAdapter(
    private val context: Context,
) :
    RecyclerView.Adapter<AppPageAdapter.PageViewHolder>(),
    DataProcessor {
    /**
     * 目前支持的类型和分页
     */
    private val pages = arrayOf(LAYOUT_TYPE_LIST, LAYOUT_TYPE_GRID)

    private var adapters: Array<AppListAdapter?> = arrayOfNulls(pages.size)
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val dataProcessor = AppDataProcessor()

    var includeSystemApp = false

    init {
        dataProcessor.setOnSortedCallback { type, desc ->
            repeat(itemCount) {
                notifyData(it)
            }
        }
    }

    class PageViewHolder(val binding: PagerAppListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val begin = System.currentTimeMillis()
        val binding: PagerAppListItemBinding = PagerAppListItemBinding.inflate(inflater, parent, false)
        val end = System.currentTimeMillis()
        println("onCreateViewHolder type=$viewType time=${end - begin}")
        return PageViewHolder(binding)
    }

    override fun getItemCount(): Int = pages.size

    override fun getItemViewType(position: Int): Int {
        return pages[position]
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        bindPager(position, holder.binding.recyclerView, getItemViewType(position))
        notifyData(position)
    }

    /**
     * 初始化app列表
     */
    private fun bindPager(index: Int, recyclerView: RecyclerView, layoutType: Int) {
        if (layoutType == LAYOUT_TYPE_LIST) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
        } else if (layoutType == LAYOUT_TYPE_GRID) {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
        }
        adapters[index] = AppListAdapter(context, layoutType)
        recyclerView.adapter = adapters[index]
        adapters[index]?.setOnItemClickListener { _, packageName ->
            ARouter.getInstance()
                .build(Router.DETAIL_ACTIVITY)
                .withString("package_name", packageName)
                .navigation()
        }
    }

    /**
     * 刷新app列表
     */
    fun updateAppDataList(newAppList: List<AppEntity>) {
        dataProcessor.resetData(newAppList)
        repeat(itemCount) { pageIndex ->
            notifyData(pageIndex)
        }

        // 更新一下最新信息到数据库
        AppExecutors.executor.execute {
            AppCheckFactory.instance.updateInfoToDatabase()
        }
    }

    private fun notifyData(pageIndex: Int) {
        adapters[pageIndex]?.let {
            val oldAppList = it.getAppList()
            val list = dataProcessor.cloneData()
            val diffResult = DiffUtil.calculateDiff(AppDiffCallBack(oldAppList, list))
            it.setAppList(list)
            diffResult.dispatchUpdatesTo(it)
        }
    }

    override fun getDataProcessor(): AppDataProcessor = dataProcessor
}