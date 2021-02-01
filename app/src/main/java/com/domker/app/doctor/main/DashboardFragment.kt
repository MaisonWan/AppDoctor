package com.domker.app.doctor.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.domker.app.doctor.R
import com.domker.app.doctor.data.AppCheckFactory
import com.domker.app.doctor.data.AppEntity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

/**
 * 数据看板
 */
class DashboardFragment : Fragment() {

    private lateinit var appList: List<AppEntity>
    private lateinit var slideshowViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        slideshowViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        appList = AppCheckFactory.getInstance(requireContext()).getAppList()
        initViews(root)
        return root
    }

    private fun initViews(rootView: View) {
        val tabTitle = intArrayOf(R.string.item_package_size, R.string.item_install_time)
        val sectionsPagerAdapter = DashboardPagerAdapter(requireContext(), tabTitle, appList)
        val viewPager: ViewPager2 = rootView.findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = rootView.findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.setText(tabTitle[position])
        }.attach()

    }

}