package com.domker.doctor.app.applist

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.doctor.R
import com.domker.doctor.databinding.PagerAppListItemBinding
import com.domker.doctor.data.db.AppEntity
import com.domker.doctor.app.detail.home.HomeViewModel
import com.domker.doctor.settings.SettingsViewModel
import com.domker.doctor.data.store.APP_LIST_STYLE_GRID
import com.domker.doctor.data.store.APP_LIST_STYLE_LIST
import com.domker.doctor.data.store.LaunchSetting
import com.domker.doctor.util.Router
import com.domker.doctor.util.log
import com.domker.doctor.widget.ViewBindingFragment
import com.domker.doctor.base.addDividerItemDecoration

/**
 * 展示App的页面
 */
class AppFragment : ViewBindingFragment<PagerAppListItemBinding>() {
    private lateinit var appListViewModel: AppListViewModel
    private lateinit var homeViewModel: HomeViewModel
    private val settingsViewModel: SettingsViewModel by activityViewModels()
    private lateinit var adapter: AppListAdapter
    private var appListStyle = APP_LIST_STYLE_LIST
    private var decoration: RecyclerView.ItemDecoration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addMenuProvider()
        appListViewModel = ViewModelProvider(this)[AppListViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PagerAppListItemBinding {
        loadConfigOnViewCreated = true
        return PagerAppListItemBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        bindLayoutStyle(requireContext(), appListStyle)
        initDataChanged()
        initMenu()
    }

    private fun initView() {
        adapter = AppListAdapter(requireContext())
        // 点击每个程序icon的动作
        adapter.setOnItemClickListener { _, item ->
            ARouter.getInstance()
                .build(Router.DETAIL_ACTIVITY)
                .withString("package_name", item.packageName)
                .navigation()
        }
        binding.recyclerView.adapter = adapter
    }

    private fun bindLayoutStyle(context: Context, style: String) {
        val recyclerView = binding.recyclerView
        if (style == APP_LIST_STYLE_LIST) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            decoration = recyclerView.addDividerItemDecoration(context, R.drawable.inset_recyclerview_divider)
        } else if (style == APP_LIST_STYLE_GRID) {
            recyclerView.layoutManager = GridLayoutManager(context, 4)
            if (decoration != null) {
                recyclerView.removeItemDecoration(decoration!!)
                decoration = null
            }
        }
    }

    private fun initDataChanged() {
        appListViewModel.getAppList().observe(viewLifecycleOwner) {
            adapter.notifyAppList(it)
        }

        // 从系统获取最新的数据
        appListViewModel.updateAppList(appIncludeAll)

        settingsViewModel.getAppListStyleSync(requireContext()) {
            log("getAppListStyleSync $it")
            bindLayoutStyle(requireContext(), it)
            adapter.notifyAppListStyleChanged(it)
        }
    }

    private fun initMenu() {
        adapter.initSorterMap(
            mapOf(
                Pair(R.id.menu_sort_time, AppEntity.sortByTime),
                Pair(R.id.menu_sort_name, AppEntity.sortByName),
                Pair(R.id.menu_sort_size, AppEntity.sortBySize)
            )
        )
    }

    override fun onAppIncludeChanged(includeAll: Boolean) {
        super.onAppIncludeChanged(includeAll)
        appListViewModel.updateAppList(includeAll)
    }

    override fun onLoadLaunchSetting(launchSetting: LaunchSetting) {
        super.onLoadLaunchSetting(launchSetting)
//        binding.viewpager.currentItem = launchSetting.launchPageIndex
        appListStyle = launchSetting.appListStyle
        log("onLoadLaunchSetting ${launchSetting.appListStyle}")
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return adapter.sortByItemId(menuItem.itemId)
    }
}