package com.domker.app.doctor.main.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import com.domker.app.doctor.store.APP_LIST_STYLE_LIST
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.AppDiffCallBack
import com.domker.base.addDividerItemDecoration
import com.domker.base.thread.AppExecutors

/**
 * 程序列表的分页适配器
 * Created by wanlipeng on 1/29/21 11:52 AM
 */
class AppPageAdapter(
    private val fragment: Fragment
) :
    RecyclerView.Adapter<AppPageAdapter.PageViewHolder>(),
    DataProcessor {
    private val context: Context = fragment.requireContext()

    /**
     * 目前支持的类型和分页
     */
    private val pages = arrayOf(LAYOUT_TYPE_LIST, LAYOUT_TYPE_GRID)
    private var currentLayoutStyle = APP_LIST_STYLE_LIST
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

    class PageViewHolder(val binding: PagerAppListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val begin = System.currentTimeMillis()
        val binding = PagerAppListItemBinding.inflate(inflater, parent, false)
        val end = System.currentTimeMillis()
        println("onCreateViewHolder type=$viewType time=${end - begin}")
        return PageViewHolder(binding)
    }

    override fun getItemCount(): Int = pages.size

    override fun getItemViewType(position: Int): Int {
        return if (currentLayoutStyle == APP_LIST_STYLE_LIST) {
            LAYOUT_TYPE_LIST
        } else {
            LAYOUT_TYPE_GRID
        }
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        bindPager(position, holder.binding.recyclerView, getItemViewType(position))
        fragment.registerForContextMenu(holder.binding.recyclerView)
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
        // 点击每个程序icon的动作
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

    /**
     * 更新页面的风格，List或者Grid的
     */
    fun updateAppListStyle(style: String) {
        currentLayoutStyle = style
        notifyItemRangeChanged(0, pages.size)
        repeat(itemCount) {
            adapters[it]?.notifyItemRangeChanged(0, adapters[it]?.itemCount ?: 0)
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