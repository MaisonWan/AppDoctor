package com.domker.app.doctor.main

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.data.AppEntity
import com.domker.app.doctor.databinding.PagerAppListItemBinding
import com.domker.app.doctor.util.Router
import com.domker.app.doctor.widget.AppDiffCallBack
import com.domker.app.doctor.widget.AppListAdapter
import com.domker.base.addItemDecoration
import com.domker.base.thread.AppExecutors

/**
 * 程序列表的分页适配器
 * Created by wanlipeng on 1/29/21 11:52 AM
 */
class AppListPageAdapter(private val context: Context,
                         private val lifecycleOwner: LifecycleOwner,
                         private val viewModelStoreOwner: ViewModelStoreOwner) :
        RecyclerView.Adapter<AppListPageAdapter.PageViewHolder>() {
    private lateinit var adapter: AppListAdapter
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val app = AppCheckFactory.getInstance(context)
    private lateinit var viewModel: AppListViewModel
    var includeSystemApp = false

    class PageViewHolder(val binding: PagerAppListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding: PagerAppListItemBinding = PagerAppListItemBinding.inflate(inflater, parent, false)
        initViewModel()
        return PageViewHolder(binding)
    }

    override fun getItemCount(): Int = 2

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        when (position) {
            0, 1 -> {
                initAppList(holder.binding.recyclerView)
                fetchAppList()
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(viewModelStoreOwner).get(AppListViewModel::class.java)
        viewModel.appListData.observe(lifecycleOwner, {
            updateAppList(it)
        })
    }

    /**
     * 初始化app列表
     */
    private fun initAppList(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(context, R.drawable.inset_recyclerview_divider)
        adapter = AppListAdapter(context)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { _, packageName ->
            ARouter.getInstance()
                    .build(Router.DETAIL_ACTIVITY)
                    .withString("package_name", packageName)
                    .navigation()
        }
    }

    /**
     * 刷新app列表
     */
    private fun updateAppList(newAppList: List<AppEntity>) {
        val oldAppList = adapter.getAppList()
        val diffResult = DiffUtil.calculateDiff(AppDiffCallBack(oldAppList, newAppList))
        adapter.setAppList(newAppList)
        diffResult.dispatchUpdatesTo(adapter)

        // 更新一下最新信息到数据库
        AppExecutors.executor.execute {
            AppCheckFactory.getInstance(context).updateInfoToDatabase()
        }
    }

    /**
     * 获取最新的信息，是否包含系统应用
     */
    private fun fetchAppList() {
        AppExecutors.executor.execute {
            val newAppList = app.getAppList(includeSystemApp)
            viewModel.appListData.postValue(newAppList)
        }
    }

    fun fetchAppList(include: Boolean) {
        includeSystemApp = include
        fetchAppList()
    }
}