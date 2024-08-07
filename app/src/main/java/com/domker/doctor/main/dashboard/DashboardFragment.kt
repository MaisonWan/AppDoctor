package com.domker.doctor.main.dashboard

import android.os.Bundle
import android.view.*
import androidx.activity.ComponentActivity
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import com.domker.doctor.R
import com.domker.doctor.databinding.FragmentMainDashboardBinding
import com.domker.doctor.widget.ViewBindingFragment
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 数据看板
 */
class DashboardFragment : ViewBindingFragment<FragmentMainDashboardBinding>(), MenuProvider {

    private lateinit var sectionsPagerAdapter: DashboardPagerAdapter
    private lateinit var slideshowViewModel: DashboardViewModel

    private lateinit var dashboardContext: DashboardContext

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainDashboardBinding {
        slideshowViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        dashboardContext = DashboardContext(this)
        loadConfigOnViewCreated = true
        return FragmentMainDashboardBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initMenu()
    }

    private fun initViews() {
        val tabTitle = intArrayOf(R.string.item_package_size, R.string.item_install_time)
        sectionsPagerAdapter = DashboardPagerAdapter(dashboardContext, tabTitle)

        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.setText(tabTitle[position])
        }.attach()
    }

    private fun initMenu() {
        (activity as ComponentActivity).addMenuProvider(this, viewLifecycleOwner)
    }

    override fun onAppIncludeChanged(includeAll: Boolean) {
        super.onAppIncludeChanged(includeAll)
        val list = dashboardContext.reloadAppList(includeAll)
        sectionsPagerAdapter.getDataProcessor().setData(list)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val p = sectionsPagerAdapter.getDataProcessor()
        return when (menuItem.itemId) {
            R.id.menu_sort_time -> {
//                p.sortBy(SORT_TIME)
                true
            }
            R.id.menu_sort_name -> {
//                p.sortBy(SORT_NAME)
                true
            }
            R.id.menu_sort_size -> {
//                p.sortBy(SORT_SIZE)
                true
            }
            else -> false
        }
    }
}