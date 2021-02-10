package com.domker.app.doctor.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.launcher.ARouter
import com.domker.app.doctor.R
import com.domker.app.doctor.databinding.FragmentAppListBinding
import com.domker.app.doctor.util.Router

/**
 * 展示App列表的页面，多种不同风格的展示
 */
class AppListFragment : Fragment() {
    private lateinit var adapter: AppListPageAdapter

    // 是否展示所有app，true包含系统应用
    private var isShowAllApp = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
            isShowAllApp = it.getBoolean("show_all_app", false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentAppListBinding.inflate(inflater, container, false)
        initViews(binding)
        return binding.root
    }

    private fun initViews(binding: FragmentAppListBinding) {
        val viewPager = binding.viewpager
        adapter = AppListPageAdapter(requireContext(), this, this)
        adapter.includeSystemApp = isShowAllApp
        viewPager.adapter = adapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        binding.circlePageIndicator.setViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        // 清除Activity的菜单
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_dashboard -> {
                ARouter.getInstance()
                        .build(Router.DASHBOARD_ACTIVITY)
                        .withBoolean("show_all_app", isShowAllApp)
                        .navigation()
                true
            }
            R.id.menu_show_app -> {
                isShowAllApp = !isShowAllApp
                if (isShowAllApp) {
                    item.setIcon(R.drawable.ic_baseline_border_clear_24)
                } else {
                    item.setIcon(R.drawable.ic_outline_border_all_24)
                }
                adapter.fetchAppList(isShowAllApp)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}