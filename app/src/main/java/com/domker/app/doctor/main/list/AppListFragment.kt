package com.domker.app.doctor.main.list

import android.os.Bundle
import android.view.*
import androidx.activity.ComponentActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.domker.app.doctor.R
import com.domker.app.doctor.data.SORT_NAME
import com.domker.app.doctor.data.SORT_SIZE
import com.domker.app.doctor.data.SORT_TIME
import com.domker.app.doctor.databinding.FragmentAppListBinding
import com.domker.app.doctor.detail.home.HomeViewModel
import com.domker.app.doctor.settings.SettingsViewModel
import com.domker.app.doctor.store.AppSettings
import com.domker.app.doctor.store.LaunchSetting
import com.domker.app.doctor.widget.BaseAppFragment
import kotlinx.coroutines.launch

/**
 * 展示App列表的页面，多种不同风格的展示
 */
class AppListFragment : BaseAppFragment(), MenuProvider {
    private lateinit var binding: FragmentAppListBinding
    private lateinit var mAdapter: AppPageAdapter
    private lateinit var appListViewModel: AppListViewModel
    private lateinit var homeViewModel: HomeViewModel
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as ComponentActivity).addMenuProvider(this, this, Lifecycle.State.RESUMED)
        appListViewModel = ViewModelProvider(this)[AppListViewModel::class.java]
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppListBinding.inflate(inflater, container, false)
        initViews(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDataChanged()
        initMenu()
    }

    private fun initDataChanged() {
        appListViewModel.getAppList().observe(viewLifecycleOwner) {
            mAdapter.updateAppDataList(it)
        }
        appListViewModel.updateAppList(appIncludeAll)

        settingsViewModel.getAppListStyleSync(requireContext()) {
            println("getAppListStyleSync $it")
            mAdapter.setAppListStyle(it)
        }
    }

    private fun initMenu() {
        registerForContextMenu(binding.viewpager)
        mAdapter.getDataProcessor().initMenuItemSortMap(
            mapOf(
                Pair(R.id.menu_sort_time, SORT_TIME),
                Pair(R.id.menu_sort_name, SORT_NAME),
                Pair(R.id.menu_sort_size, SORT_SIZE)
            )
        )
    }

    private fun initViews(binding: FragmentAppListBinding) {
        mAdapter = AppPageAdapter(this)
        mAdapter.includeSystemApp = appIncludeAll
        binding.viewpager.also { v ->
            v.adapter = mAdapter
            v.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            binding.circlePageIndicator.setViewPager(v)
            // 滑动界面的时候，记录当前最新的位置
            v.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // 异步存储当前的状态
                    lifecycleScope.launch {
                        AppSettings.setLaunchPageIndex(requireContext(), position)
                    }
                }

            })
        }
    }

    override fun onAppIncludeChanged(includeAll: Boolean) {
        super.onAppIncludeChanged(includeAll)
        appListViewModel.updateAppList(includeAll)
    }

    override fun onLoadLaunchSetting(launchSetting: LaunchSetting) {
        super.onLoadLaunchSetting(launchSetting)
        binding.viewpager.currentItem = launchSetting.launchPageIndex
        mAdapter.notifyAppListStyleChanged(launchSetting.appListStyle)
        println("onLoadLaunchSetting ${launchSetting.appListStyle}")
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val p = mAdapter.getDataProcessor()
        return p.sortByItemId(menuItem.itemId)
    }

}