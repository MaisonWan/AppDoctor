package com.domker.app.doctor.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.domker.app.doctor.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 数据看板
 */
class DashboardFragment : Fragment() {

    private var isShowAllApp: Boolean = false
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
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort_time -> {
                sectionsPagerAdapter.sortBy(SORT_TIME)
                true
            }
            R.id.menu_sort_name -> {
                sectionsPagerAdapter.sortBy(SORT_NAME)
                true
            }
            R.id.menu_sort_size -> {
                sectionsPagerAdapter.sortBy(SORT_SIZE)
                true
            }
            R.id.menu_show_app -> {
                isShowAllApp = !isShowAllApp
                if (isShowAllApp) {
                    item.setIcon(R.drawable.ic_baseline_border_clear_24)
                } else {
                    item.setIcon(R.drawable.ic_outline_border_all_24)
                }
                sectionsPagerAdapter.showAllApp(isShowAllApp)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}