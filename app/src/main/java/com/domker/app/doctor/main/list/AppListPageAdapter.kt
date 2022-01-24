package com.domker.app.doctor.main.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.data.AppDataProcessor
import com.domker.app.doctor.data.AppEntity
import com.domker.app.doctor.data.DataProcessor
import com.domker.app.doctor.databinding.PagerAppListItemBinding
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
class AppListPageAdapter(private val context: Context,
                         private val lifecycleOwner: LifecycleOwner,
                         viewModelStoreOwner: ViewModelStoreOwner) :
        RecyclerView.Adapter<AppListPageAdapter.PageViewHolder>(),
        DataProcessor {
    private var adapters: Array<AppListAdapter?> = arrayOfNulls(2)
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val app = AppCheckFactory.getInstance(context)
    private val dataProcessor = AppDataProcessor()
    private val viewModel = ViewModelProvider(viewModelStoreOwner).get(AppListViewModel::class.java)

    var includeSystemApp = false

    init {
        dataProcessor.setOnSortedCallback { type, desc ->
            repeat(2) {
                notifyData(it)
            }
        }
    }

    class PageViewHolder(val binding: PagerAppListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val begin = System.currentTimeMillis()
        val binding: PagerAppListItemBinding = PagerAppListItemBinding.inflate(inflater, parent, false)
        initViewModel()
        val end = System.currentTimeMillis()
        println("onCreateViewHolder type=$viewType time=${end - begin}")
        return PageViewHolder(binding)
    }

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        when (position) {
            LAYOUT_TYPE_LIST -> {
                val begin = System.currentTimeMillis()
                initAppList(position, holder.binding.recyclerView, LAYOUT_TYPE_LIST)
                fetchAppList(position)
                val end = System.currentTimeMillis()
                println("onCreateViewHolder position=$position time=${end - begin}")
            }
            LAYOUT_TYPE_GRID -> {
                val begin = System.currentTimeMillis()
                initAppList(position, holder.binding.recyclerView, LAYOUT_TYPE_GRID)
                fetchAppList(position)
                val end = System.currentTimeMillis()
                println("onCreateViewHolder position=$position time=${end - begin}")
            }
        }
    }


    private fun initViewModel() {
        viewModel.appListData.observe(lifecycleOwner, {
            updateAppList(it.first, it.second)
        })
    }

    /**
     * 初始化app列表
     */
    private fun initAppList(index: Int, recyclerView: RecyclerView, layoutType: Int) {
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
    private fun updateAppList(index: Int, newAppList: List<AppEntity>) {
        dataProcessor.resetData(newAppList)
        repeat(2) { pageIndex ->
            notifyData(pageIndex)
        }

        // 更新一下最新信息到数据库
        AppExecutors.executor.execute {
            AppCheckFactory.getInstance(context).updateInfoToDatabase()
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

    /**
     * 获取最新的信息，是否包含系统应用
     */
    private fun fetchAppList(index: Int) {
        AppExecutors.executor.execute {
            val newAppList = app.getAppList(includeSystemApp)
            viewModel.appListData.postValue(Pair(index, newAppList))
        }
    }

    /**
     * @param index 当前界面的index
     * @param includeAll 是否包含所有app
     */
    fun fetchAppList(index: Int, includeAll: Boolean) {
        includeSystemApp = includeAll
        fetchAppList(index)
    }

    override fun getDataProcessor(): AppDataProcessor = dataProcessor
}