package com.domker.app.doctor.main.dashboard

import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.domker.app.doctor.R
import com.domker.app.doctor.data.SORT_NAME
import com.domker.app.doctor.data.SORT_SIZE
import com.domker.app.doctor.data.SORT_TIME
import com.domker.app.doctor.widget.BaseAppFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 数据看板
 */
class DashboardFragment : BaseAppFragment() {

    private lateinit var sectionsPagerAdapter: DashboardPagerAdapter
    private lateinit var slideshowViewModel: DashboardViewModel

    private lateinit var dashboardContext: DashboardContext

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_main_dashboard, container, false)
        dashboardContext = DashboardContext(this)
        initViews(root)
        return root
    }

    private fun initViews(rootView: View) {
        val tabTitle = intArrayOf(R.string.item_package_size, R.string.item_install_time)
        sectionsPagerAdapter = DashboardPagerAdapter(dashboardContext, tabTitle)
        val viewPager: ViewPager2 = rootView.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = rootView.findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setText(tabTitle[position])
        }.attach()
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onAppIncludeChanged(includeAll: Boolean) {
        super.onAppIncludeChanged(includeAll)
        val list = dashboardContext.reloadAppList(includeAll)
        sectionsPagerAdapter.getDataProcessor().resetData(list)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val p = sectionsPagerAdapter.getDataProcessor()
        return when (item.itemId) {
            R.id.menu_sort_time -> {
                p.sortBy(SORT_TIME)
                true
            }
            R.id.menu_sort_name -> {
                p.sortBy(SORT_NAME)
                true
            }
            R.id.menu_sort_size -> {
                p.sortBy(SORT_SIZE)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}